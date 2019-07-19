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
package com.ok2c.hc5.json.http.examles;

import java.net.URI;
import java.util.concurrent.Future;

import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.nio.support.AsyncRequestBuilder;
import org.apache.hc.core5.io.CloseMode;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ok2c.hc5.json.http.JsonResponseConsumers;
import com.ok2c.hc5.json.http.RequestData;

public class JsonSequenceResponseExample {

    public static void main(String... args) throws Exception {
        CloseableHttpAsyncClient client = HttpAsyncClients.createSystem();

        client.start();

        URI uri = URI.create("http://httpbin.org/stream/5");

        JsonFactory factory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(factory);
        System.out.println("Executing GET " + uri);
        Future<?> future = client.execute(
                AsyncRequestBuilder.get(uri).build(),
                JsonResponseConsumers.create(
                        objectMapper,
                        RequestData.class,
                        messageHead -> System.out.println("Response status: " + messageHead.getCode()),
                        requestData -> System.out.println(requestData)),
                new FutureCallback<Long>() {

                    @Override
                    public void completed(Long count) {
                        System.out.println("Objects received: " + count);
                    }

                    @Override
                    public void failed(Exception ex) {
                        ex.printStackTrace(System.out);
                    }

                    @Override
                    public void cancelled() {
                    }

                });
        future.get();

        System.out.println("Shutting down");
        client.shutdown(CloseMode.GRACEFUL);
    }

}
