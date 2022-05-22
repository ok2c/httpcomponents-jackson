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

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.ok2c.hc5.json.JsonResultSink;
import com.ok2c.hc5.json.JsonTokenConsumer;
import com.ok2c.hc5.json.TokenBufferAssembler;

/**
 * {@link org.apache.hc.core5.http.nio.AsyncEntityConsumer} implementation that
 * converts incoming HTTP message entity into a sequence of instances
 * of the given class and passes those objects to a {@link JsonResultSink}.
 *
 * @param <T> type of objects produced by this class.
 */
public class JsonSequenceEntityConsumer<T> extends AbstractJsonEntityConsumer<Long> {

    private final ReadJsonValue<T> readJsonValue;
    private final JsonResultSink<T> resultSink;
    private final AtomicLong count;

    public JsonSequenceEntityConsumer(ObjectMapper objectMapper, JavaType javaType, JsonResultSink<T> resultSink) {
        super(Objects.requireNonNull(objectMapper, "Object mapper").getFactory());
        this.readJsonValue = jsonParser -> objectMapper.readValue(jsonParser, javaType);
        this.resultSink = Objects.requireNonNull(resultSink, "Result sink");
        this.count = new AtomicLong(0);
    }

    public JsonSequenceEntityConsumer(ObjectMapper objectMapper, Class<T> objectClazz, JsonResultSink<T> resultSink) {
        this(Objects.requireNonNull(objectMapper, "Object mapper"),
                objectMapper.getTypeFactory().constructType(objectClazz),
                resultSink);
    }

    public JsonSequenceEntityConsumer(ObjectMapper objectMapper, TypeReference<T> typeReference, JsonResultSink<T> resultSink) {
        this(Objects.requireNonNull(objectMapper, "Object mapper"),
                objectMapper.getTypeFactory().constructType(typeReference),
                resultSink);
    }

    @Override
    protected JsonTokenConsumer createJsonTokenConsumer(Consumer<Long> resultConsumer) {
        return new TokenBufferAssembler(new JsonResultSink<TokenBuffer>() {


            @Override
            public void begin(int sizeHint) {
                resultSink.begin(sizeHint);
            }

            @Override
            public void accept(TokenBuffer tokenBuffer) {
                try {
                    JsonParser jsonParser = tokenBuffer != null ? tokenBuffer.asParserOnFirstToken() : null;
                    T result = jsonParser != null ? readJsonValue.readValue(jsonParser) : null;
                    if (result != null) {
                        count.incrementAndGet();
                        resultSink.accept(result);
                    }
                } catch (IOException ex) {
                    failed(ex);
                }
            }

            @Override
            public void end() {
                resultSink.end();
                resultConsumer.accept(count.get());
            }

        });
    }

    @FunctionalInterface
    private interface ReadJsonValue<T> {
        T readValue(JsonParser jsonParser) throws IOException;
    }
}
