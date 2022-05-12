package com.ok2c.hc5.json.http;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequests;
import org.apache.hc.client5.http.async.methods.SimpleRequestProducer;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.nio.AsyncResponseConsumer;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ok2c.hc5.json.JsonConsumer;
import com.ok2c.hc5.json.JsonTokenEventHandler;

class AbstractJsonEntityConsumerTest {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testRequestWithINVALIDrequest() throws Exception {
        // Verify that a request to self with an invalid url still calls the completion.
        try (CloseableHttpAsyncClient httpClient = HttpAsyncClients.createHttp2Default()) {
            httpClient.start();
            SimpleHttpRequest request = SimpleHttpRequests.get(URI.create("http://localhost/INVALID"));
            JsonFactory jsonFactory = mapper.getFactory();
            JsonTokenEventHandler mockJsonTokenEventHandler = Mockito.mock(JsonTokenEventHandler.class);
            @SuppressWarnings("unchecked")
            JsonConsumer<HttpResponse> mockJsonConsumer = Mockito.mock(JsonConsumer.class);
            AsyncResponseConsumer<Void> responseConsumer = JsonResponseConsumers.create(
                    jsonFactory, mockJsonConsumer, mockJsonTokenEventHandler);
            CompletableFuture<Void> completableFuture = new CompletableFuture<>();
            httpClient.execute(SimpleRequestProducer.create(request), responseConsumer,
                    new CompletableFutureCallback<>(completableFuture));
            completableFuture
                    .handle((result, throwable) -> {
                        Assertions.assertNotNull(throwable);
                        Assertions.assertNotEquals(NullPointerException.class, throwable.getClass());
                        return result;
                    })
                    .join();
            Mockito.verifyNoMoreInteractions(mockJsonConsumer, mockJsonTokenEventHandler);
        }
    }
}