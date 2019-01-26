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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * {@link org.apache.hc.core5.http.nio.AsyncEntityProducer} implementation that
 * generates a message body containing serialized content of the given {@link JsonNode}
 * object.
 */
public class JsonNodeEntityProducer extends AbstractJsonEntityProducer {

    private final JsonNode jsonNode;
    private final ObjectMapper objectMapper;

    public JsonNodeEntityProducer(JsonNode jsonNode, ObjectMapper objectMapper) {
        super(4096);
        this.jsonNode = jsonNode;
        this.objectMapper = objectMapper;
    }

    @Override
    final void generateJson(OutputStream outputStream) throws IOException {
        objectMapper.writeValue(outputStream, jsonNode);
        outputStream.close();
    }

    @Override
    public void failed(Exception cause) {
    }

}
