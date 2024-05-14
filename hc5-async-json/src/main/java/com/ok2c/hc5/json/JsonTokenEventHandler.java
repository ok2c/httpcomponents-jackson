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

/**
 * Represents a handler of JSON token events.
 */
public interface JsonTokenEventHandler {

    /**
     * Triggered to signal the beginning of an object.
     */
    void objectStart();

    /**
     * Triggered to signal the end of an object.
     */
    void objectEnd();

    /**
     * Triggered to signal the beginning of an array.
     */
    void arrayStart();

    /**
     * Triggered to signal the end of an array.
     */
    void arrayEnd();

    /**
     * Triggered to signal occurrence of a field with the specified name.
     */
    void field(String name);

    /**
     * Triggered to signal occurrence of an embedded object.
     */
    void embeddedObject(Object object);

    /**
     * Triggered to signal occurrence of a textual value.
     */
    void value(String value);

    /**
     * Triggered to signal occurrence of an integer value.
     */
    void value(int value);

    /**
     * Triggered to signal occurrence of a long value.
     */
    void value(long value);

    /**
     * Triggered to signal occurrence of an double precision value.
     */
    void value(double value);

    /**
     * Triggered to signal occurrence of an boolean value.
     */
    void value(boolean value);

    /**
     * Triggered to signal occurrence of a null value.
     */
    void valueNull();

    /**
     * Triggered to signal the end of the token stream.
     */
    void endOfStream();

}
