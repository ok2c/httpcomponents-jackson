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
import java.util.function.Consumer;

import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.nio.AsyncEntityConsumer;
import org.apache.hc.core5.http.nio.CapacityChannel;

import com.fasterxml.jackson.core.JsonFactory;
import com.ok2c.hc5.json.JsonAsyncTokenizer;
import com.ok2c.hc5.json.JsonMessageException;
import com.ok2c.hc5.json.JsonTokenConsumer;

abstract class AbstractJsonEntityConsumer<T> implements AsyncEntityConsumer<T> {

    private final JsonAsyncTokenizer jsonTokenizer;
    private final AtomicReference<FutureCallback<T>> resultCallbackRef;
    private final AtomicReference<T> resultRef;

    AbstractJsonEntityConsumer(JsonFactory jsonFactory) {
        this.jsonTokenizer = new JsonAsyncTokenizer(jsonFactory);
        this.resultCallbackRef = new AtomicReference<>(null);
        this.resultRef = new AtomicReference<>(null);
    }

    abstract JsonTokenConsumer createJsonTokenConsumer(Consumer<T> resultConsumer);

    @Override
    public final void streamStart(EntityDetails entityDetails, FutureCallback<T> resultCallback) throws HttpException, IOException {
        ContentType contentType = ContentType.parseLenient(entityDetails.getContentType());
        if (contentType != null && !ContentType.APPLICATION_JSON.isSameMimeType(contentType)) {
            throw new JsonMessageException("Unexpected content type: " + contentType);
        }
        resultCallbackRef.set(resultCallback);
        jsonTokenizer.initialize(createJsonTokenConsumer(result -> {
            resultRef.set(result);
            FutureCallback<T> resultCallback1 = resultCallbackRef.getAndSet(null);
            if (resultCallback1 != null) {
                resultCallback1.completed(result);
            }
        }));
    }

    @Override
    public final void updateCapacity(CapacityChannel capacityChannel) throws IOException {
        capacityChannel.update(Integer.MAX_VALUE);
    }

    @Override
    public final void consume(ByteBuffer data) throws IOException {
        jsonTokenizer.consume(data);
    }

    @Override
    public final void streamEnd(List<? extends Header> trailers) throws HttpException, IOException {
        jsonTokenizer.streamEnd();
    }

    @Override
    public final void failed(Exception cause) {
        FutureCallback<T> resultCallback = resultCallbackRef.getAndSet(null);
        if (resultCallback != null) {
            resultCallback.failed(cause);
        }
    }

    @Override
    public final T getContent() {
        return resultRef.get();
    }

    @Override
    public void releaseResources() {
    }

}
