/*
 * Copyright 2018, OK2 Consulting Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ok2c.hc5.json.http;

import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.Message;
import org.apache.hc.core5.http.nio.AsyncRequestConsumer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ok2c.hc5.json.JsonConsumer;
import com.ok2c.hc5.json.JsonResultSink;
import com.ok2c.hc5.json.JsonTokenEventHandler;

/**
 * Factory class for JSON {@link AsyncRequestConsumer}s.
 */
public final class JsonRequestConsumers {

    /**
     * Creates {@link AsyncRequestConsumer} that produces a {@link Message} object
     * consisting of the {@link HttpRequest} head and the de-serialized JSON body.
     *
     * @param objectMapper the object mapper to be used to de-serialize JSON content.
     * @param javaType the java type of the de-serialized object.
     * @param <T> the type of result objects produced by the consumer.
     * @return the request consumer.
     */
    public static <T> AsyncRequestConsumer<Message<HttpRequest, T>> create(ObjectMapper objectMapper,
                                                                           JavaType javaType) {
        return new JsonRequestObjectConsumer<>(() -> new JsonObjectEntityConsumer<>(objectMapper, javaType));
    }

    /**
     * Creates {@link AsyncRequestConsumer} that produces a {@link Message} object
     * consisting of the {@link HttpRequest} head and the de-serialized JSON body.
     *
     * @param objectMapper the object mapper to be used to de-serialize JSON content.
     * @param objectClazz the class of the de-serialized object.
     * @param <T> the type of result objects produced by the consumer.
     * @return the request consumer.
     */
    public static <T> AsyncRequestConsumer<Message<HttpRequest, T>> create(ObjectMapper objectMapper,
                                                                           Class<T> objectClazz) {
        return new JsonRequestObjectConsumer<>(() -> new JsonObjectEntityConsumer<>(objectMapper, objectClazz));
    }

    /**
     * Creates {@link AsyncRequestConsumer} that produces a {@link Message} object
     * consisting of the {@link HttpRequest} head and the de-serialized JSON body.
     *
     * @param objectMapper the object mapper to be used to de-serialize JSON content.
     * @param typeReference the type reference of the de-serialized object.
     * @param <T> the type of result objects produced by the consumer.
     * @return the request consumer.
     */
    public static <T> AsyncRequestConsumer<Message<HttpRequest, T>> create(ObjectMapper objectMapper,
                                                                           TypeReference<T> typeReference) {
        return new JsonRequestObjectConsumer<>(() -> new JsonObjectEntityConsumer<>(objectMapper, typeReference));
    }

    /**
     * Creates {@link AsyncRequestConsumer} that produces a {@link Message} object
     * consisting of the {@link HttpRequest} head and the {@link JsonNode} body.
     *
     * @param jsonFactory JSON factory.
     * @return the request consumer.
     */
    public static AsyncRequestConsumer<Message<HttpRequest, JsonNode>> create(JsonFactory jsonFactory) {
        return new JsonRequestObjectConsumer<>(() -> new JsonNodeEntityConsumer(jsonFactory));
    }

    /**
     * Creates {@link AsyncRequestConsumer} that converts incoming HTTP message
     * into a sequence of instances of the given class and passes those objects
     * to the given {@link JsonResultSink}.
     *
     * @param objectMapper the object mapper to be used to de-serialize JSON content.
     * @param javaType the java type of the de-serialized object.
     * @param messageConsumer optional operation that accepts the message head as input.
     * @param resultSink the recipient of result objects.
     * @param <T> type of result objects produced by the consumer.
     * @return the request consumer.
     */
    public static <T> AsyncRequestConsumer<Long> create(ObjectMapper objectMapper,
                                                        JavaType javaType,
                                                        JsonConsumer<HttpRequest> messageConsumer,
                                                        JsonResultSink<T> resultSink) {
        return new JsonRequestStreamConsumer<>(
                () -> new JsonSequenceEntityConsumer<>(objectMapper, javaType, resultSink),
                messageConsumer);
    }

    /**
     * Creates {@link AsyncRequestConsumer} that converts incoming HTTP message
     * into a sequence of instances of the given class and passes those objects
     * to the given {@link JsonResultSink}.
     *
     * @param objectMapper the object mapper to be used to de-serialize JSON content.
     * @param objectClazz the class of the de-serialized object.
     * @param messageConsumer optional operation that accepts the message head as input.
     * @param resultSink the recipient of result objects.
     * @param <T> type of result objects produced by the consumer.
     * @return the request consumer.
     */
    public static <T> AsyncRequestConsumer<Long> create(ObjectMapper objectMapper,
                                                        Class<T> objectClazz,
                                                        JsonConsumer<HttpRequest> messageConsumer,
                                                        JsonResultSink<T> resultSink) {
        return new JsonRequestStreamConsumer<>(
                () -> new JsonSequenceEntityConsumer<>(objectMapper, objectClazz, resultSink),
                messageConsumer);
    }

    /**
     * Creates {@link AsyncRequestConsumer} that converts incoming HTTP message
     * into a sequence of instances of the given class and passes those objects
     * to the given {@link JsonResultSink}.
     *
     * @param objectMapper the object mapper to be used to de-serialize JSON content.
     * @param typeReference the type reference of the de-serialized object.
     * @param messageConsumer optional operation that accepts the message head as input.
     * @param resultSink the recipient of result objects.
     * @param <T> type of result objects produced by the consumer.
     * @return the request consumer.
     */
    public static <T> AsyncRequestConsumer<Long> create(ObjectMapper objectMapper,
                                                        TypeReference<T> typeReference,
                                                        JsonConsumer<HttpRequest> messageConsumer,
                                                        JsonResultSink<T> resultSink) {
        return new JsonRequestStreamConsumer<>(
                () -> new JsonSequenceEntityConsumer<>(objectMapper, typeReference, resultSink),
                messageConsumer);
    }

    /**
     * Creates {@link AsyncRequestConsumer} that converts incoming HTTP message
     * into a sequence of JSON tokens passed as events to the given {@link JsonTokenEventHandler}.
     *
     * @param jsonFactory JSON factory.
     * @param messageConsumer optional operation that accepts the message head as input.
     * @param eventHandler JSON event handler
     * @return the request consumer.
     */
    public static AsyncRequestConsumer<Void> create(JsonFactory jsonFactory,
                                                    JsonConsumer<HttpRequest> messageConsumer,
                                                    JsonTokenEventHandler eventHandler) {
        return new JsonRequestStreamConsumer<>(
                () -> new JsonTokenEntityConsumer(jsonFactory, eventHandler),
                messageConsumer);
    }

}
