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

import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.nio.AsyncEntityProducer;
import org.apache.hc.core5.http.nio.AsyncResponseProducer;
import org.apache.hc.core5.http.nio.ResponseChannel;
import org.apache.hc.core5.http.protocol.HttpContext;

final class JsonResponseObjectProducer extends JsonMessageProducer implements AsyncResponseProducer {

    private final HttpResponse response;

    public JsonResponseObjectProducer(HttpResponse response, AsyncEntityProducer entityProducer) {
        super(entityProducer);
        this.response = response;
    }

    @Override
    public void sendResponse(ResponseChannel channel, HttpContext context) throws HttpException, IOException {
        channel.sendResponse(response, getEntityDetails(), context);
    }

    @Override
    public boolean isRepeatable() {
        return super.isRepeatable();
    }

    @Override
    public void failed(Exception cause) {
    }

}
