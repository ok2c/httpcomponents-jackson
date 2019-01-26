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

import org.apache.hc.client5.http.async.methods.HttpRequests;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.Message;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.io.CloseMode;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ok2c.hc5.json.http.JsonRequestProducers;
import com.ok2c.hc5.json.http.JsonResponseConsumers;
import com.ok2c.hc5.json.http.RequestData;

public class JsonSequenceRequestExample {

    public static void main(String... args) throws Exception {
        CloseableHttpAsyncClient client = HttpAsyncClients.createSystem();

        client.start();

        URI uri = URI.create("http://httpbin.org/post");

        JsonFactory factory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(factory);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        System.out.println("Executing POST " + uri);
        Future<?> future = client.execute(
                JsonRequestProducers.create(HttpRequests.POST.create(uri),
                        objectMapper,
                        channel -> {
                            channel.write(new BasicNameValuePair("name1", "value1"));
                            channel.write(new BasicNameValuePair("name2", "value2"));
                            channel.write(new BasicNameValuePair("name3", "value3"));
                            channel.endStream();
                        }),
                JsonResponseConsumers.create(objectMapper, RequestData.class),
                new FutureCallback<Message<HttpResponse, RequestData>>() {

                    @Override
                    public void completed(Message<HttpResponse, RequestData> message) {
                        System.out.println("Response status: " + message.getHead().getCode());
                        System.out.println(message.getBody());
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
