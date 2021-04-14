package com.ok2c.hc5.json.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.HttpResponseException;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.Message;
import org.apache.hc.core5.http.impl.BasicEntityDetails;
import org.apache.hc.core5.http.message.BasicHttpResponse;
import org.apache.hc.core5.http.nio.AsyncResponseConsumer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonResponseConsumerTest {

    static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testNonJsonErrorResponseBodyIgnored() throws Exception {
        String errorBody = "Unexpected internal failure";

        AsyncResponseConsumer<Message<HttpResponse, RequestData>> consumer =
                JsonResponseConsumers.create(objectMapper, RequestData.class);

        CompletableFuture<RequestData> resultFuture = new CompletableFuture<>();
        consumer.consumeResponse(
                new BasicHttpResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR),
                new BasicEntityDetails(errorBody.length(), ContentType.TEXT_PLAIN),
                HttpClientContext.create(),
                new FutureCallback<Message<HttpResponse, RequestData>>() {
                    @Override
                    public void completed(Message<HttpResponse, RequestData> result) {
                        // NOTE: The error body is lost, we would need a custom consumer to retrieve that.
                        handleResponseResult(result, resultFuture);
                    }

                    @Override
                    public void failed(Exception ex) {
                        resultFuture.completeExceptionally(ex);
                    }

                    @Override
                    public void cancelled() {
                        resultFuture.cancel(false);
                    }
                });
        consumer.consume(ByteBuffer.wrap(errorBody.getBytes(StandardCharsets.UTF_8)));
        consumer.streamEnd(null);

        // SUCCESS: Non-JSON content-type is ignored by the consumer, it returns a 'null' to the callback.
        assertThat(resultFuture).hasFailedWithThrowableThat()
                .isInstanceOf(HttpResponseException.class);
    }

    @Test
    void testJsonErrorResponseBodyNotMatchingResponseClass() throws Exception {
        String errorBody = "{\"code\": 500, \"message\": \"Unexpected internal failure\"}";

        AsyncResponseConsumer<Message<HttpResponse, RequestData>> consumer =
                JsonResponseConsumers.create(objectMapper, RequestData.class);

        CompletableFuture<RequestData> resultFuture = new CompletableFuture<>();
        consumer.consumeResponse(
                new BasicHttpResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR),
                new BasicEntityDetails(errorBody.length(), ContentType.APPLICATION_JSON),
                HttpClientContext.create(),
                new FutureCallback<Message<HttpResponse, RequestData>>() {
                    @Override
                    public void completed(Message<HttpResponse, RequestData> result) {
                        // NOTE: The error body is lost, we would need a custom consumer to retrieve that. Actually
                        // it might also be mapped accidentally to the response class.
                        handleResponseResult(result, resultFuture);
                    }

                    @Override
                    public void failed(Exception ex) {
                        resultFuture.completeExceptionally(ex);
                    }

                    @Override
                    public void cancelled() {
                        resultFuture.cancel(false);
                    }
                });
        consumer.consume(ByteBuffer.wrap(errorBody.getBytes(StandardCharsets.UTF_8)));
        consumer.streamEnd(null);

        // FAILS: consumer attempts to parse the error body as it was a successful response and throws a parsing error
        assertThat(resultFuture).hasFailedWithThrowableThat()
                .isInstanceOf(HttpResponseException.class);
    }

    @Test
    void testJsonErrorResponseBodyNotMatchingResponseArray() throws Exception {
        String errorBody = "{\"code\": 500, \"message\": \"Unexpected internal failure\"}";

        AsyncResponseConsumer<Message<HttpResponse, RequestData[]>> consumer =
                JsonResponseConsumers.create(objectMapper, RequestData[].class);

        CompletableFuture<RequestData[]> resultFuture = new CompletableFuture<>();
        consumer.consumeResponse(
                new BasicHttpResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR),
                new BasicEntityDetails(errorBody.length(), ContentType.APPLICATION_JSON),
                HttpClientContext.create(),
                new FutureCallback<Message<HttpResponse, RequestData[]>>() {
                    @Override
                    public void completed(Message<HttpResponse, RequestData[]> result) {
                        // NOTE: The error body is lost, we would need a custom consumer to retrieve that. Actually
                        // it might also be mapped accidentally to the response class.
                        handleResponseResult(result, resultFuture);
                    }

                    @Override
                    public void failed(Exception ex) {
                        resultFuture.completeExceptionally(ex);
                    }

                    @Override
                    public void cancelled() {
                        resultFuture.cancel(false);
                    }
                });
        consumer.consume(ByteBuffer.wrap(errorBody.getBytes(StandardCharsets.UTF_8)));
        consumer.streamEnd(null);

        // FAILS: consumer attempts to parse the error body as it was a successful response and throws a parsing error
        assertThat(resultFuture).hasFailedWithThrowableThat()
                .isInstanceOf(HttpResponseException.class);
    }

    private static <T> void handleResponseResult(Message<HttpResponse, T> result, CompletableFuture<T> resultFuture) {
        int code = result.getHead().getCode();
        if (code >= HttpStatus.SC_SUCCESS && code < HttpStatus.SC_REDIRECTION) {
            resultFuture.complete(result.getBody());
        } else {
            resultFuture.completeExceptionally(new HttpResponseException(code,
                    result.getHead().getReasonPhrase()));
        }
    }
}
