package com.ok2c.hc5.json.http;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.hc.client5.http.HttpResponseException;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.Message;
import org.apache.hc.core5.http.impl.BasicEntityDetails;
import org.apache.hc.core5.http.message.BasicHttpResponse;
import org.apache.hc.core5.http.nio.AsyncResponseConsumer;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ok2c.hc5.json.JsonTokenConsumer;

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
        assertThat(resultFuture)
                .failsWithin(Duration.ofMinutes(1))
                .withThrowableThat()
                .isInstanceOf(ExecutionException.class)
                .withCauseInstanceOf(HttpResponseException.class);
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
        assertThat(resultFuture)
                .failsWithin(Duration.ofMinutes(1))
                .withThrowableThat()
                .isInstanceOf(ExecutionException.class)
                .withCauseInstanceOf(HttpResponseException.class);
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
        assertThat(resultFuture)
                .failsWithin(Duration.ofMinutes(1))
                .withThrowableThat()
                .isInstanceOf(ExecutionException.class)
                .withCauseInstanceOf(HttpResponseException.class);
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

    @Test
    void testJsonTokenConsumer() throws Exception {
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonTokenConsumer mockJsonTokenConsumer = Mockito.mock(JsonTokenConsumer.class);
        AsyncResponseConsumer<Void> responseConsumer = JsonResponseConsumers.create(
                jsonFactory, null,
                mockJsonTokenConsumer);
        HttpResponse mockHttpResponse = Mockito.mock(HttpResponse.class);
        EntityDetails mockEntityDetails = Mockito.mock(EntityDetails.class);
        Mockito.when(mockEntityDetails.getContentType()).thenReturn(ContentType.APPLICATION_JSON.getMimeType());
        HttpContext mockHttpContext = Mockito.mock(HttpContext.class);
        //noinspection unchecked
        responseConsumer.consumeResponse(mockHttpResponse, mockEntityDetails, mockHttpContext, null);
        ByteBuffer data = ByteBuffer.wrap("{\"foo\":\"bar\"}".getBytes(StandardCharsets.UTF_8));
        responseConsumer.consume(data);
        responseConsumer.streamEnd(Collections.emptyList());
        Mockito.verify(mockJsonTokenConsumer).accept(
                Mockito.eq(JsonTokenId.ID_START_OBJECT), Mockito.any(JsonParser.class));
        Mockito.verify(mockJsonTokenConsumer).accept(
                Mockito.eq(JsonTokenId.ID_FIELD_NAME), Mockito.any(JsonParser.class));
        Mockito.verify(mockJsonTokenConsumer).accept(
                Mockito.eq(JsonTokenId.ID_STRING), Mockito.any(JsonParser.class));
        Mockito.verify(mockJsonTokenConsumer).accept(
                Mockito.eq(JsonTokenId.ID_END_OBJECT), Mockito.any(JsonParser.class));
        Mockito.verify(mockJsonTokenConsumer).accept(
                Mockito.eq(JsonTokenId.ID_NO_TOKEN), Mockito.any(JsonParser.class));
        Mockito.verifyNoMoreInteractions(mockJsonTokenConsumer);
    }
}
