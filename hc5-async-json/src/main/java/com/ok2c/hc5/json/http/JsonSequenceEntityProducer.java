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
import java.io.OutputStream;
import java.util.Set;

import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.nio.AsyncEntityProducer;
import org.apache.hc.core5.http.nio.DataStreamChannel;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * {@link org.apache.hc.core5.http.nio.AsyncEntityProducer} implementation that
 * generates a message body containing serialized content of a sequence of JSON objects.
 *
 * @param <T> type of objects used by this class.
 */
public class JsonSequenceEntityProducer<T> implements AsyncEntityProducer {

    private enum State { ACTIVE, FLUSHING, END_STREAM }

    private final ObjectMapper objectMapper;
    private final int initSize;
    private final InternalBuffer buffer;
    private final ObjectProducer<T> objectProducer;
    private final OutputStream outputStream;

    private volatile State state;

    public JsonSequenceEntityProducer(ObjectMapper objectMapper, int initSize, ObjectProducer<T> objectProducer) {
        this.objectMapper = objectMapper;
        this.initSize = initSize;
        this.buffer = new InternalBuffer(initSize);
        this.objectProducer = objectProducer;
        this.state = State.ACTIVE;
        this.outputStream = new OutputStream() {

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                buffer.write(b, off, len);
            }

            @Override
            public void write(int b) throws IOException {
                buffer.write(b);
            }

        };
    }

    public JsonSequenceEntityProducer(ObjectMapper objectMapper, ObjectProducer<T> objectProducer) {
        this(objectMapper, 4096, objectProducer);
    }

    @Override
    public final long getContentLength() {
        return -1;
    }

    @Override
    public final Set<String> getTrailerNames() {
        return null;
    }

    @Override
    public final String getContentType() {
        return ContentType.APPLICATION_JSON.toString();
    }

    @Override
    public final String getContentEncoding() {
        return null;
    }

    @Override
    public boolean isRepeatable() {
        return false;
    }

    @Override
    public boolean isChunked() {
        return false;
    }

    @Override
    public final int available() {
        synchronized (buffer) {
            return buffer.length();
        }
    }

    @Override
    public final void produce(final DataStreamChannel channel) throws IOException {
        synchronized (buffer) {
            if (state == State.ACTIVE && buffer.length() < initSize) {
                objectProducer.produce(new ObjectChannel<T>() {

                    @Override
                    public int write(T obj) throws IOException {
                        synchronized (buffer) {
                            objectMapper.writeValue(outputStream, obj);
                            channel.write(buffer.getByteBuffer());
                            return 1;
                        }
                    }

                    @Override
                    public void endStream() throws IOException {
                        synchronized (buffer) {
                            if (buffer.hasData()) {
                                channel.write(buffer.getByteBuffer());
                            }
                            if (buffer.hasData()) {
                                channel.requestOutput();
                                state = State.FLUSHING;
                            } else {
                                channel.endStream(null);
                                state = State.END_STREAM;
                            }
                        }
                    }

                });
            }
            if (state.compareTo(State.END_STREAM) < 0 && buffer.hasData()) {
                channel.write(buffer.getByteBuffer());
            }
            if (state == State.FLUSHING && !buffer.hasData()) {
                channel.endStream(null);
                state = State.END_STREAM;
            }
        }

    }

    @Override
    public void failed(Exception cause) {
    }

    @Override
    public void releaseResources() {
        buffer.clear();
        state = State.ACTIVE;
    }

}
