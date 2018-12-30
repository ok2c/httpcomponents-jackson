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
import org.apache.hc.core5.http.Message;
import org.apache.hc.core5.http.nio.AsyncDataConsumer;
import org.apache.hc.core5.http.nio.AsyncEntityConsumer;
import org.apache.hc.core5.http.nio.CapacityChannel;
import org.apache.hc.core5.http.protocol.HttpContext;

class JsonMessageConsumer<H extends HttpMessage, T> implements AsyncDataConsumer {

    private final Supplier<AsyncEntityConsumer<T>> consumerSupplier;
    private final AtomicReference<AsyncEntityConsumer<T>> entityConsumerRef;
    private final AtomicReference<Message<H, T>> resultRef;

    public JsonMessageConsumer(Supplier<AsyncEntityConsumer<T>> consumerSupplier) {
        this.consumerSupplier = consumerSupplier;
        this.entityConsumerRef = new AtomicReference<>();
        this.resultRef = new AtomicReference<>();
    }

    final void consumeMessage(H messageHead, EntityDetails entityDetails, HttpContext context,
                              FutureCallback<Message<H, T>> resultCallback) throws HttpException, IOException {
        if (entityDetails == null) {
            Message<H, T> message = new Message<>(messageHead, null);
            resultRef.set(message);
            if (resultCallback != null) {
                resultCallback.completed(message);
            }
            return;
        }
        ContentType contentType = ContentType.parseLenient(entityDetails.getContentType());
        if (contentType != null &&
                ContentType.APPLICATION_JSON.getMimeType().equalsIgnoreCase(contentType.getMimeType())) {
            AsyncEntityConsumer<T> entityConsumer = consumerSupplier.get();
            entityConsumerRef.set(entityConsumer);
        } else {
            entityConsumerRef.set(new NoopJsonEntityConsumer<>());
        }
        entityConsumerRef.get().streamStart(entityDetails, new FutureCallback<T>() {

            @Override
            public void completed(T result) {
                Message<H, T> message = new Message<>(messageHead, result);
                resultRef.set(message);
                if (resultCallback != null) {
                    resultCallback.completed(message);
                }
            }

            @Override
            public void failed(Exception ex) {
                if (resultCallback != null) {
                    resultCallback.failed(ex);
                }
            }

            @Override
            public void cancelled() {
                if (resultCallback != null) {
                    resultCallback.cancelled();
                }
            }

        });
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

    Message<H, T> getResult() {
        return resultRef.get();
    }

    @Override
    public final void releaseResources() {
        final AsyncEntityConsumer<T> entityConsumer = entityConsumerRef.getAndSet(null);
        if (entityConsumer != null) {
            entityConsumer.releaseResources();
        }
    }

}
