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
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.nio.AsyncEntityConsumer;
import org.apache.hc.core5.http.nio.AsyncRequestConsumer;
import org.apache.hc.core5.http.protocol.HttpContext;

import com.ok2c.hc5.json.JsonConsumer;

final class JsonRequestStreamConsumer<T> extends JsonStreamConsumer<HttpRequest, T> implements AsyncRequestConsumer<T> {

    public JsonRequestStreamConsumer(Supplier<AsyncEntityConsumer<T>> consumerSupplier,
                                     JsonConsumer<HttpRequest> messageConsumer) {
        super(consumerSupplier, messageConsumer);
    }


    @Override
    public void consumeRequest(HttpRequest request,
                               EntityDetails entityDetails,
                               HttpContext context,
                               FutureCallback<T> resultCallback) throws HttpException, IOException {
        consumeMessage(request, entityDetails, resultCallback);
    }

    @Override
    public void failed(Exception cause) {
        super.failed(cause);
    }

    @Override
    public T getResult() {
        return super.getResult();
    }

}
