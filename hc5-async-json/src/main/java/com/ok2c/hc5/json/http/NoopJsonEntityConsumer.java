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
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.nio.AsyncEntityConsumer;
import org.apache.hc.core5.http.nio.CapacityChannel;

final class NoopJsonEntityConsumer<T> implements AsyncEntityConsumer<T> {

    private final AtomicReference<FutureCallback<T>> resultCallbackRef;

    NoopJsonEntityConsumer() {
        resultCallbackRef = new AtomicReference<>();
    }

    @Override
    public void streamStart(EntityDetails entityDetails, FutureCallback<T> resultCallback) throws HttpException, IOException {
        resultCallbackRef.set(resultCallback);
    }

    @Override
    public void updateCapacity(CapacityChannel capacityChannel) throws IOException {
        capacityChannel.update(Integer.MAX_VALUE);
    }

    @Override
    public void consume(ByteBuffer data) throws IOException {
    }

    @Override
    public void streamEnd(List<? extends Header> trailers) throws HttpException, IOException {
        FutureCallback<T> callback = resultCallbackRef.get();
        if (callback != null) {
            callback.completed(null);
        }
    }

    @Override
    public void failed(Exception cause) {
        FutureCallback<T> resultCallback = resultCallbackRef.getAndSet(null);
        if (resultCallback != null) {
            resultCallback.failed(cause);
        }
    }

    @Override
    public T getContent() {
        return null;
    }

    @Override
    public void releaseResources() {
    }

}
