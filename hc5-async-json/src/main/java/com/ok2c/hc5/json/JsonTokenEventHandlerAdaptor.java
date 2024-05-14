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
import com.fasterxml.jackson.core.JsonTokenId;

/**
 * {@link JsonTokenConsumer} implementation that converts JSON tokens into
 * event signals for {@link JsonTokenEventHandler}.
 */
public final class JsonTokenEventHandlerAdaptor implements JsonTokenConsumer {

    private final JsonTokenEventHandler eventHandler;

    public JsonTokenEventHandlerAdaptor(JsonTokenEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public void accept(int tokenId, JsonParser jsonParser) throws IOException {
        switch (tokenId) {
            case JsonTokenId.ID_START_OBJECT:
                eventHandler.objectStart();
                break;
            case JsonTokenId.ID_END_OBJECT:
                eventHandler.objectEnd();
                break;
            case JsonTokenId.ID_START_ARRAY:
                eventHandler.arrayStart();
                break;
            case JsonTokenId.ID_END_ARRAY:
                eventHandler.arrayEnd();
                break;
            case JsonTokenId.ID_FIELD_NAME:
                eventHandler.field(jsonParser.getText());
                break;
            case JsonTokenId.ID_STRING:
                eventHandler.value(jsonParser.getText());
                break;
            case JsonTokenId.ID_NUMBER_INT:
                final JsonParser.NumberType numberType = jsonParser.getNumberType();
                final Number numberValue = jsonParser.getNumberValue();
                if (numberType == JsonParser.NumberType.LONG) {
                    eventHandler.value(numberValue.longValue());
                } else {
                    eventHandler.value(numberValue.intValue());
                }
                break;
            case JsonTokenId.ID_NUMBER_FLOAT:
                eventHandler.value(jsonParser.getDoubleValue());
                break;
            case JsonTokenId.ID_TRUE:
                eventHandler.value(true);
                break;
            case JsonTokenId.ID_FALSE:
                eventHandler.value(false);
                break;
            case JsonTokenId.ID_NULL:
                eventHandler.valueNull();
                break;
            case JsonTokenId.ID_EMBEDDED_OBJECT:
                eventHandler.embeddedObject(jsonParser.getEmbeddedObject());
                break;
            case JsonTokenId.ID_NO_TOKEN:
                eventHandler.endOfStream();
                break;
            default:
                throw new IllegalStateException("Unexpected JSON token id: " + tokenId);
        }

    }

}
