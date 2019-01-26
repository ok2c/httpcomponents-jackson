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
import java.util.function.Supplier;

import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpMessage;
import org.apache.hc.core5.http.nio.AsyncDataConsumer;
import org.apache.hc.core5.http.nio.AsyncEntityConsumer;
import org.apache.hc.core5.http.nio.CapacityChannel;

import com.ok2c.hc5.json.JsonConsumer;

class JsonStreamConsumer<H extends HttpMessage, T> implements AsyncDataConsumer {

    private final Supplier<AsyncEntityConsumer<T>> entityConsumerSupplier;
    private final JsonConsumer<H> messageConsumer;
    private final AtomicReference<AsyncEntityConsumer<T>> entityConsumerRef;

    public JsonStreamConsumer(Supplier<AsyncEntityConsumer<T>> entityConsumerSupplier,
                              JsonConsumer<H> messageConsumer) {
        this.entityConsumerSupplier = entityConsumerSupplier;
        this.messageConsumer = messageConsumer;
        this.entityConsumerRef = new AtomicReference<>();
    }

    final void consumeMessage(H messageHead, EntityDetails entityDetails,
                              FutureCallback<T> resultCallback) throws HttpException, IOException {
        if (messageConsumer != null) {
            messageConsumer.accept(messageHead);
        }
        if (entityDetails == null) {
            if (resultCallback != null) {
                resultCallback.completed(null);
            }
            return;
        }
        ContentType contentType = ContentType.parseLenient(entityDetails.getContentType());
        if (contentType != null &&
                ContentType.APPLICATION_JSON.getMimeType().equalsIgnoreCase(contentType.getMimeType())) {
            AsyncEntityConsumer<T> entityConsumer = entityConsumerSupplier.get();
            entityConsumerRef.set(entityConsumer);
        } else {
            entityConsumerRef.set(new NoopJsonEntityConsumer<>());
        }
        entityConsumerRef.get().streamStart(entityDetails, resultCallback);
    }

    @Override
    public final void updateCapacity(CapacityChannel capacityChannel) throws IOException {
        entityConsumerRef.get().updateCapacity(capacityChannel);
    }

    @Override
    public final void consume(ByteBuffer data) throws IOException {
        entityConsumerRef.get().consume(data);
    }

    @Override
    public final void streamEnd(List<? extends Header> trailers) throws HttpException, IOException {
        entityConsumerRef.get().streamEnd(trailers);
    }

    void failed(Exception cause) {
        entityConsumerRef.get().failed(cause);
    }

    T getResult() {
        return entityConsumerRef.get().getContent();
    }

    @Override
    public final void releaseResources() {
        final AsyncEntityConsumer<?> entityConsumer = entityConsumerRef.getAndSet(null);
        if (entityConsumer != null) {
            entityConsumer.releaseResources();
        }
    }

}
