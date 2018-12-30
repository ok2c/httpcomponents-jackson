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
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.Message;
import org.apache.hc.core5.http.nio.AsyncEntityConsumer;
import org.apache.hc.core5.http.nio.AsyncResponseConsumer;
import org.apache.hc.core5.http.protocol.HttpContext;

final class JsonResponseObjectConsumer<T> extends JsonMessageConsumer<HttpResponse, T>
        implements AsyncResponseConsumer<Message<HttpResponse, T>> {

    public JsonResponseObjectConsumer(Supplier<AsyncEntityConsumer<T>> consumerSupplier) {
        super(consumerSupplier);
    }

    @Override
    public void consumeResponse(HttpResponse request,
                                EntityDetails entityDetails,
                                HttpContext context,
                                FutureCallback<Message<HttpResponse, T>> resultCallback) throws HttpException, IOException {
        consumeMessage(request, entityDetails, context, resultCallback);
    }

    @Override
    public void informationResponse(HttpResponse response, HttpContext context) throws HttpException, IOException {
    }

    @Override
    public void failed(Exception cause) {
        super.failed(cause);
    }

    @Override
    public Message<HttpResponse, T> getResult() {
        return super.getResult();
    }
}
