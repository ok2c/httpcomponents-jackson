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
import java.util.Objects;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;

/**
 * {@link JsonTokenConsumer} decorator that filters out top level opening and closing array tokens.
 */
public final class TopLevelArrayTokenFilter implements JsonTokenConsumer {

    private final JsonTokenConsumer tokenConsumer;
    private int depth;

    public TopLevelArrayTokenFilter(JsonTokenConsumer tokenConsumer) {
        Objects.requireNonNull(tokenConsumer, "Consumer");
        this.tokenConsumer = tokenConsumer;
    }

    public void accept(int tokenId, JsonParser jsonParser) throws IOException {
        if (depth == 0 &&
                (tokenId == JsonTokenId.ID_START_ARRAY || tokenId == JsonTokenId.ID_END_ARRAY)) {
            return;
        }
        switch (tokenId) {
            case JsonTokenId.ID_START_OBJECT:
            case JsonTokenId.ID_START_ARRAY:
                depth++;
                break;
            case JsonTokenId.ID_END_OBJECT:
            case JsonTokenId.ID_END_ARRAY:
                depth--;
                break;
        }
        tokenConsumer.accept(tokenId, jsonParser);
    }

}
