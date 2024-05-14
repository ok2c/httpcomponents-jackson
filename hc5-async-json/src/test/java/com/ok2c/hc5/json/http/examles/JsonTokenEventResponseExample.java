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
import com.ok2c.hc5.json.JsonTokenEventHandler;
import com.ok2c.hc5.json.http.JsonResponseConsumers;

public class JsonTokenEventResponseExample {

    public static void main(String... args) throws Exception {
        CloseableHttpAsyncClient client = HttpAsyncClients.createSystem();

        client.start();

        URI uri = URI.create("http://httpbin.org/get");

        JsonFactory factory = new JsonFactory();
        System.out.println("Executing GET " + uri);
        Future<?> future = client.execute(
                AsyncRequestBuilder.get(uri).build(),
                JsonResponseConsumers.create(
                        factory,
                        messageHead -> System.out.println("Response status: " + messageHead.getCode()),
                        new JsonTokenEventHandler() {

                            @Override
                            public void objectStart() {
                                System.out.print("object start/");
                            }

                            @Override
                            public void objectEnd() {
                                System.out.print("object end/");
                            }

                            @Override
                            public void arrayStart() {
                                System.out.print("array start/");
                            }

                            @Override
                            public void arrayEnd() {
                                System.out.print("array end/");
                            }

                            @Override
                            public void field(String name) {
                                System.out.print(name + "=");
                            }

                            @Override
                            public void embeddedObject(Object object) {
                                System.out.print(object + "/");
                            }

                            @Override
                            public void value(String value) {
                                System.out.print("\"" + value + "\"/");
                            }

                            @Override
                            public void value(int value) {
                                System.out.print(value + "/");
                            }

                            @Override
                            public void value(long value) {
                                System.out.print(value + "/");
                            }

                            @Override
                            public void value(double value) {
                                System.out.print(value + "/");
                            }

                            @Override
                            public void value(boolean value) {
                                System.out.print(value + "/");
                            }

                            @Override
                            public void valueNull() {
                                System.out.print("null/");
                            }

                            @Override
                            public void endOfStream() {
                                System.out.println("stream end/");
                            }

                        }),
                new FutureCallback<Void>() {

                    @Override
                    public void completed(Void input) {
                        System.out.println("Object received");
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
        client.close(CloseMode.GRACEFUL);
    }

}
