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
package com.ok2c.hc5.json.bulk;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.ok2c.hc5.json.JsonAsyncTokenizer;
import com.ok2c.hc5.json.JsonResultSink;
import com.ok2c.hc5.json.TokenBufferAssembler;
import com.ok2c.hc5.json.TopLevelArrayTokenFilter;

/**
 * Event-driven bulk JSON reader that can read arrays of objects while buffering only a single
 * array element in memory.
 */
public final class JsonBulkArrayReader {

    private final ObjectMapper objectMapper;
    private final JsonAsyncTokenizer jsonTokenizer;

    public JsonBulkArrayReader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.jsonTokenizer = new JsonAsyncTokenizer(objectMapper.getFactory());
    }

    public <T> void initialize(TypeReference<T> typeReference, JsonResultSink<T> resultSink) throws IOException {
        this.jsonTokenizer.initialize(new TopLevelArrayTokenFilter(new TokenBufferAssembler(new JsonResultSink<TokenBuffer>() {

            @Override
            public void begin(int sizeHint) {
                resultSink.begin(sizeHint);
            }

            @Override
            public void accept(TokenBuffer tokenBuffer) {
                try {
                    JsonParser jsonParser = tokenBuffer != null ? tokenBuffer.asParserOnFirstToken() : null;
                    T result = jsonParser != null ? objectMapper.readValue(jsonParser, typeReference) : null;
                    if (result != null) {
                        resultSink.accept(result);
                    }
                } catch (IOException ex) {
                    throw new UncheckedIOException(ex);
                }
            }

            @Override
            public void end() {
                resultSink.end();
            }

        })));
    }

    public void consume(ByteBuffer data) throws IOException {
        try {
            jsonTokenizer.consume(data);
        } catch (UncheckedIOException ex) {
            throw ex.getCause();
        }
    }

    public void streamEnd() throws IOException {
        jsonTokenizer.streamEnd();
    }

}
