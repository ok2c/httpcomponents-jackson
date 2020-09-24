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

import java.io.IOException;

import org.apache.hc.core5.annotation.Contract;
import org.apache.hc.core5.annotation.ThreadingBehavior;

/**
 * Abstract object stream channel.
 * <p>
 * Implementations are expected to be thread-safe.
 * </p>
 *
 * @param <T> object type accepted by the channel.
 */
@Contract(threading = ThreadingBehavior.SAFE)
public interface ObjectChannel<T> {

    /**
     * Writes serialized object into the underlying data stream.
     *
     * @param obj object
     * @return The number of elements written, possibly zero
     */
    int write(T obj) throws IOException;

    /**
     * Terminates the underlying data stream and optionally writes
     * a closing sequence.
     */
    void endStream() throws IOException;
}
