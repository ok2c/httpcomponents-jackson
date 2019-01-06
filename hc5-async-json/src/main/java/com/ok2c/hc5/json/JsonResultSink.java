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

import java.util.function.Consumer;

/**
 * Represents a recipient of zero or many results of JSON message processing.
 *
 * @param <T> the type of object that represents a result of JSON message processing.
 */
public interface JsonResultSink<T> extends Consumer<T> {

    /**
     * Signals the beginning of the stream of result objects.
     *
     * @param sizeHint the expected size of the object stream, if known.
     * {@code -1} otherwise.
     */
    default void begin(int sizeHint) {
    }

    /**
     * Signals the end of the stream of result objects.
     */
    default void end() {
    }

}
