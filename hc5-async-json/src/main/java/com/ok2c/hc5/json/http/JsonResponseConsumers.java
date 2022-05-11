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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ok2c.hc5.json.JsonConsumer;
import com.ok2c.hc5.json.JsonResultSink;
import com.ok2c.hc5.json.JsonTokenConsumer;
import com.ok2c.hc5.json.JsonTokenEventHandler;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.Message;
import org.apache.hc.core5.http.nio.AsyncResponseConsumer;

/**
 * Factory class for JSON {@link AsyncResponseConsumer}s.
 */
public final class JsonResponseConsumers {

    /**
     * Creates {@link AsyncResponseConsumer} that produces a {@link Message} object
     * consisting of the {@link HttpResponse} head and the de-serialized JSON body.
     *
     * @param objectMapper the object mapper to be used to de-serialize JSON content.
     * @param javaType     the java type of the de-serialized object.
     * @param <T>          the type of result objects produced by the consumer.
     * @return the response consumer.
     */
    public static <T> AsyncResponseConsumer<Message<HttpResponse, T>> create(ObjectMapper objectMapper,
                                                                             JavaType javaType) {
        return new JsonResponseObjectConsumer<>(() -> new JsonObjectEntityConsumer<>(objectMapper, javaType));
    }

    /**
     * Creates {@link AsyncResponseConsumer} that produces a {@link Message} object
     * consisting of the {@link HttpResponse} head and the de-serialized JSON body.
     *
     * @param objectMapper the object mapper to be used to de-serialize JSON content.
     * @param objectClazz  the class of the de-serialized object.
     * @param <T>          the type of result objects produced by the consumer.
     * @return the response consumer.
     */
    public static <T> AsyncResponseConsumer<Message<HttpResponse, T>> create(ObjectMapper objectMapper,
                                                                             Class<T> objectClazz) {
        return new JsonResponseObjectConsumer<>(() -> new JsonObjectEntityConsumer<>(objectMapper, objectClazz));
    }

    /**
     * Creates {@link AsyncResponseConsumer} that produces a {@link Message} object
     * consisting of the {@link HttpResponse} head and the de-serialized JSON body.
     *
     * @param objectMapper  the object mapper to be used to de-serialize JSON content.
     * @param typeReference the type reference of the de-serialized object.
     * @param <T>           the type of result objects produced by the consumer.
     * @return the response consumer.
     */
    public static <T> AsyncResponseConsumer<Message<HttpResponse, T>> create(ObjectMapper objectMapper,
                                                                             TypeReference<T> typeReference) {
        return new JsonResponseObjectConsumer<>(() -> new JsonObjectEntityConsumer<>(objectMapper, typeReference));
    }

    /**
     * Creates {@link AsyncResponseConsumer} that produces a {@link Message} object
     * consisting of the {@link HttpResponse} head and the {@link JsonNode} body.
     *
     * @param jsonFactory JSON factory.
     * @return the response consumer.
     */
    public static AsyncResponseConsumer<Message<HttpResponse, JsonNode>> create(JsonFactory jsonFactory) {
        return new JsonResponseObjectConsumer<>(() -> new JsonNodeEntityConsumer(jsonFactory));
    }

    /**
     * Creates {@link AsyncResponseConsumer} that converts incoming HTTP message
     * into a sequence of instances of the given class and passes those objects
     * to the given {@link JsonResultSink}.
     *
     * @param objectMapper    the object mapper to be used to de-serialize JSON content.
     * @param javaType        the java type of the de-serialized object.
     * @param messageConsumer optional operation that accepts the message head as input.
     * @param resultSink      the recipient of result objects.
     * @param <T>             type of result objects produced by the consumer.
     * @return the response consumer.
     */
    public static <T> AsyncResponseConsumer<Long> create(ObjectMapper objectMapper,
                                                         JavaType javaType,
                                                         JsonConsumer<HttpResponse> messageConsumer,
                                                         JsonResultSink<T> resultSink) {
        return new JsonResponseStreamConsumer<>(
                () -> new JsonSequenceEntityConsumer<>(objectMapper, javaType, resultSink),
                messageConsumer);
    }

    /**
     * Creates {@link AsyncResponseConsumer} that converts incoming HTTP message
     * into a sequence of instances of the given class and passes those objects
     * to the given {@link JsonResultSink}.
     *
     * @param objectMapper    the object mapper to be used to de-serialize JSON content.
     * @param objectClazz     the class of the de-serialized object.
     * @param messageConsumer optional operation that accepts the message head as input.
     * @param resultSink      the recipient of result objects.
     * @param <T>             type of result objects produced by the consumer.
     * @return the response consumer.
     */
    public static <T> AsyncResponseConsumer<Long> create(ObjectMapper objectMapper,
                                                         Class<T> objectClazz,
                                                         JsonConsumer<HttpResponse> messageConsumer,
                                                         JsonResultSink<T> resultSink) {
        return new JsonResponseStreamConsumer<>(
                () -> new JsonSequenceEntityConsumer<>(objectMapper, objectClazz, resultSink),
                messageConsumer);
    }

    /**
     * Creates {@link AsyncResponseConsumer} that converts incoming HTTP message
     * into a sequence of instances of the given class and passes those objects
     * to the given {@link JsonResultSink}.
     *
     * @param objectMapper    the object mapper to be used to de-serialize JSON content.
     * @param typeReference   the type reference of the de-serialized object.
     * @param messageConsumer optional operation that accepts the message head as input.
     * @param resultSink      the recipient of result objects.
     * @param <T>             type of result objects produced by the consumer.
     * @return the response consumer.
     */
    public static <T> AsyncResponseConsumer<Long> create(ObjectMapper objectMapper,
                                                         TypeReference<T> typeReference,
                                                         JsonConsumer<HttpResponse> messageConsumer,
                                                         JsonResultSink<T> resultSink) {
        return new JsonResponseStreamConsumer<>(
                () -> new JsonSequenceEntityConsumer<>(objectMapper, typeReference, resultSink),
                messageConsumer);
    }

    /**
     * Creates {@link AsyncResponseConsumer} that converts incoming HTTP message
     * into a sequence of JSON tokens passed as events to the given {@link JsonTokenEventHandler}.
     *
     * @param jsonFactory     JSON factory.
     * @param messageConsumer optional operation that accepts the message head as input.
     * @param eventHandler    JSON event handler
     * @return the response consumer.
     */
    public static AsyncResponseConsumer<Void> create(JsonFactory jsonFactory,
                                                     JsonConsumer<HttpResponse> messageConsumer,
                                                     JsonTokenEventHandler eventHandler) {
        return new JsonResponseStreamConsumer<>(
                () -> new JsonTokenEntityConsumer(jsonFactory, eventHandler),
                messageConsumer);
    }

    /**
     * Creates {@link AsyncResponseConsumer} that converts incoming HTTP message
     * into a sequence of JSON tokens passed as events to the given {@link JsonTokenConsumer}.
     *
     * @param jsonFactory     JSON factory.
     * @param messageConsumer optional operation that accepts the message head as input.
     * @param tokenConsumer   JSON token Consumer
     * @return the response consumer.
     */
    public static <T> AsyncResponseConsumer<Void> create(JsonFactory jsonFactory,
                                                         JsonConsumer<HttpResponse> messageConsumer,
                                                         JsonTokenConsumer tokenConsumer) {
        return new JsonResponseStreamConsumer<>(
                () -> new JsonTokenEntityConsumer(jsonFactory, tokenConsumer),
                messageConsumer);
    }
}
