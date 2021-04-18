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
import java.util.function.Supplier;

import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.Message;
import org.apache.hc.core5.http.nio.AsyncEntityConsumer;
import org.apache.hc.core5.http.nio.AsyncResponseConsumer;
import org.apache.hc.core5.http.protocol.HttpContext;

final class JsonResponseObjectConsumer<T> extends AbstractJsonMessageConsumer<HttpResponse, T>
        implements AsyncResponseConsumer<Message<HttpResponse, T>> {

    private final Supplier<AsyncEntityConsumer<T>> consumerSupplier;

    public JsonResponseObjectConsumer(Supplier<AsyncEntityConsumer<T>> consumerSupplier) {
        this.consumerSupplier = consumerSupplier;
    }

    @Override
    public void consumeResponse(HttpResponse response,
                                EntityDetails entityDetails,
                                HttpContext context,
                                FutureCallback<Message<HttpResponse, T>> resultCallback) throws HttpException, IOException {
        consumeMessage(response, entityDetails, context, resultCallback);
    }

    @Override
    public void informationResponse(HttpResponse response, HttpContext context) throws HttpException, IOException {
    }

    @Override
    protected AsyncEntityConsumer<T> createEntityConsumer(HttpResponse response,
                                                          EntityDetails entityDetails) {
        ContentType contentType = ContentType.parseLenient(entityDetails.getContentType());
        AsyncEntityConsumer<T> entityConsumer;
        if (ContentType.APPLICATION_JSON.isSameMimeType(contentType)
                && response.getCode() >= HttpStatus.SC_SUCCESS
                && response.getCode() < HttpStatus.SC_REDIRECTION) {
            entityConsumer = consumerSupplier.get();
        }
        else {
            entityConsumer = new NoopJsonEntityConsumer<>();
        }
        return entityConsumer;
    }

}
