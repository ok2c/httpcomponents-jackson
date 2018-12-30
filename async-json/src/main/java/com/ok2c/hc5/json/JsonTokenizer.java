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
package com.ok2c.hc5.json;

import java.io.IOException;
import java.io.InputStream;

import org.apache.hc.core5.util.Args;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;

/**
 * Classic (blocking) JSON tokenizer that consumes data from {@link InputStream}
 * and emits events to {@link JsonTokenConsumer}.
 */
public final class JsonTokenizer {

    private final JsonFactory jsonFactory;

    public JsonTokenizer(JsonFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }

    public void tokenize(InputStream inputStream,
                         JsonTokenConsumer consumer) throws IOException {
        Args.notNull(consumer, "Consumer");
        JsonParser parser = jsonFactory.createParser(inputStream);
        while (!parser.isClosed()) {
            JsonToken jsonToken = parser.nextToken();
            if (jsonToken != null) {
                consumer.accept(jsonToken.id(), parser);
            }
        }
        consumer.accept(JsonTokenId.ID_NO_TOKEN, parser);
    }

}
