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

import org.apache.hc.core5.util.Args;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonTokenId;
import com.ok2c.hc5.json.JsonTokenConsumer;
import com.ok2c.hc5.json.JsonTokenEventHandler;
import com.ok2c.hc5.json.JsonTokenEventHandlerAdaptor;

public class JsonTokenEntityConsumer extends AbstractJsonEntityConsumer<Void> {

    private final JsonTokenEventHandler eventHandler;

    public JsonTokenEntityConsumer(JsonFactory jsonFactory, JsonTokenEventHandler eventHandler) {
        super(jsonFactory);
        this.eventHandler = Args.notNull(eventHandler, "JSON event handler");
    }

    @Override
    protected JsonTokenConsumer createJsonTokenConsumer(Consumer<Void> resultConsumer) {
        JsonTokenEventHandlerAdaptor callbackAdaptor = new JsonTokenEventHandlerAdaptor(eventHandler);
        return (tokenId, jsonParser) -> {
            callbackAdaptor.accept(tokenId, jsonParser);
            if (tokenId == JsonTokenId.ID_NO_TOKEN) {
                resultConsumer.accept(null);
            }
        };
    }

}
