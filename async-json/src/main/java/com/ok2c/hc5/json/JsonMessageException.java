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

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Signals a failure processing a malformed or unexpected JSON message
 */
public class JsonMessageException extends JsonProcessingException {

    public JsonMessageException(String msg) {
        super(msg);
    }

    public JsonMessageException(String msg, Throwable rootCause) {
        super(msg, rootCause);
    }

    public JsonMessageException(Throwable rootCause) {
        super(rootCause);
    }

}
