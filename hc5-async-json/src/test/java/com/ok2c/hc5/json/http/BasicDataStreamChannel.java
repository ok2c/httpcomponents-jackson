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
import java.nio.channels.WritableByteChannel;
import java.util.List;

import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.nio.DataStreamChannel;

public class BasicDataStreamChannel implements DataStreamChannel {

    private final WritableByteChannel byteChannel;

    public BasicDataStreamChannel(final WritableByteChannel byteChannel) {
        this.byteChannel = byteChannel;
    }

    @Override
    public void requestOutput() {
    }

    @Override
    public int write(final ByteBuffer src) throws IOException {
        return byteChannel.write(src);
    }

    @Override
    public void endStream() throws IOException {
        if (byteChannel.isOpen()) {
            byteChannel.close();
        }
    }

    @Override
    public void endStream(final List<? extends Header> trailers) throws IOException {
        endStream();
    }

}
