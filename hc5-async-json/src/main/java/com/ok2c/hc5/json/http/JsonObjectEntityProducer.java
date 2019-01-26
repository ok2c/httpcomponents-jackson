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
import java.io.OutputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * {@link org.apache.hc.core5.http.nio.AsyncEntityProducer} implementation that
 * generates a message body containing serialized content of the given JSON object.
 *
 * @param <T> type of objects used by this class.
 */
public class JsonObjectEntityProducer<T> extends AbstractJsonEntityProducer {

    private final T jsonObject;
    private final ObjectMapper objectMapper;

    public JsonObjectEntityProducer(T jsonObject, ObjectMapper objectMapper) {
        super(4096);
        this.jsonObject = jsonObject;
        this.objectMapper = objectMapper;
    }

    @Override
    final void generateJson(OutputStream outputStream) throws IOException {
        objectMapper.writeValue(outputStream, jsonObject);
        outputStream.close();
    }

    @Override
    public void failed(Exception cause) {
    }

}
