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

import com.fasterxml.jackson.core.JsonParser;

/**
 * Represents an event recipient that can react to a stream of consecutive JSON tokens.
 */
@FunctionalInterface
public interface JsonTokenConsumer {

    /**
     * Triggered to signal {@link JsonParser} transition to a new token.
     *
     * @param tokenId Id representing of the current token held by the parser.
     * @param jsonParser the JSON parser.
     *
     * @see com.fasterxml.jackson.core.JsonTokenId
     */
    void accept(int tokenId, JsonParser jsonParser) throws IOException;

}
