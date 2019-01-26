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

import java.util.function.Consumer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.ok2c.hc5.json.JsonNodeAssembler;
import com.ok2c.hc5.json.JsonTokenConsumer;

/**
 * {@link org.apache.hc.core5.http.nio.AsyncEntityConsumer} implementation that
 * de-serializes incoming HTTP message entity into an {@link JsonNode} instance.
 */
public class JsonNodeEntityConsumer extends AbstractJsonEntityConsumer<JsonNode> {

    public JsonNodeEntityConsumer(JsonFactory jsonFactory) {
        super(jsonFactory);
    }

    @Override
    protected JsonTokenConsumer createJsonTokenConsumer(Consumer<JsonNode> resultConsumer) {
        return JsonNodeAssembler.createTokenConsumer(resultConsumer);
    }

}
