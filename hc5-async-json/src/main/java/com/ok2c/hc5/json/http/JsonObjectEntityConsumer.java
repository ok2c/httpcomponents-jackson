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
import java.util.function.Consumer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ok2c.hc5.json.JsonTokenConsumer;
import com.ok2c.hc5.json.TokenBufferAssembler;

/**
 * {@link org.apache.hc.core5.http.nio.AsyncEntityConsumer} implementation that
 * de-serializes incoming HTTP message entity into an instance of the given class.
 *
 * @param <T> type of objects produced by this class.
 */
public class JsonObjectEntityConsumer<T> extends AbstractJsonEntityConsumer<T> {

    private final ReadJsonValue<T> readJsonValue;

    public JsonObjectEntityConsumer(ObjectMapper objectMapper, JavaType javaType) {
        super(Objects.requireNonNull(objectMapper, "Object mapper").getFactory());
        this.readJsonValue = jsonParser -> objectMapper.readValue(jsonParser, javaType);
    }

    public JsonObjectEntityConsumer(ObjectMapper objectMapper, Class<T> objectClazz) {
        this(Objects.requireNonNull(objectMapper, "Object mapper"), objectMapper.getTypeFactory().constructType(objectClazz));
    }

    public JsonObjectEntityConsumer(ObjectMapper objectMapper, TypeReference<T> typeReference) {
        this(Objects.requireNonNull(objectMapper, "Object mapper"), objectMapper.getTypeFactory().constructType(typeReference));
    }

    @Override
    protected JsonTokenConsumer createJsonTokenConsumer(Consumer<T> resultConsumer) {
        return new TokenBufferAssembler(tokenBuffer -> {
            try {
                JsonParser jsonParser = tokenBuffer != null ? tokenBuffer.asParserOnFirstToken() : null;
                T result = jsonParser != null ? readJsonValue.readValue(jsonParser) : null;
                resultConsumer.accept(result);
            } catch (IOException ex) {
                failed(ex);
            }
        });
    }

    @FunctionalInterface
    private interface ReadJsonValue<T> {
        T readValue(JsonParser jsonParser) throws IOException;
    }
}
