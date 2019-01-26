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
import java.nio.ByteBuffer;

import org.apache.hc.core5.http.impl.nio.ExpandableBuffer;

class InternalBuffer extends ExpandableBuffer {

    public InternalBuffer(int bufferSize) {
        super(bufferSize);
    }

    void write(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            return;
        }
        setInputMode();
        int requiredCapacity = buffer().position() + len;
        ensureCapacity(requiredCapacity);
        buffer().put(b, off, len);
    }

    void write(int b) throws IOException {
        setInputMode();
        int requiredCapacity = buffer().position() + 1;
        ensureCapacity(requiredCapacity);
        buffer().put((byte) b);
    }


    @Override
    public void clear() {
        super.clear();
    }

    ByteBuffer getByteBuffer() {
        setOutputMode();
        return buffer();
    }

}
