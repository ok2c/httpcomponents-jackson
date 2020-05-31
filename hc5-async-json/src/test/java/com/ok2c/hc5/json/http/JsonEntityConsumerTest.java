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

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.impl.BasicEntityDetails;
import org.apache.hc.core5.http.message.BasicHeader;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ok2c.hc5.json.JsonResultSink;

public class JsonEntityConsumerTest {

    @Test
    public void testJsonNodeEntityConsumer() throws Exception {
        JsonFactory factory = new JsonFactory();

        URL resource = getClass().getResource("/sample1.json");
        Assertions.assertThat(resource).isNotNull();

        AtomicReference<JsonNode> resultRef = new AtomicReference<>();
        JsonNodeEntityConsumer entityConsumer = new JsonNodeEntityConsumer(factory);
        try (InputStream inputStream = resource.openStream()) {
            entityConsumer.streamStart(
                    new BasicEntityDetails(-1, ContentType.APPLICATION_JSON),
                    new FutureCallback<JsonNode>() {

                        @Override
                        public void completed(JsonNode result) {
                            resultRef.set(result);
                        }

                        @Override
                        public void failed(Exception ex) {
                        }

                        @Override
                        public void cancelled() {
                        }

                    });
            byte[] bytebuf = new byte[1024];
            int len;
            while ((len = inputStream.read(bytebuf)) != -1) {
                entityConsumer.consume(ByteBuffer.wrap(bytebuf, 0, len));
            }
            entityConsumer.streamEnd(null);
        }

        ObjectNode expectedObject = JsonNodeFactory.instance.objectNode();
        expectedObject.putObject("args");
        expectedObject.putObject("headers")
                .put("Accept", "application/json")
                .put("Accept-Encoding", "gzip, deflate")
                .put("Accept-Language", "en-US,en;q=0.9")
                .put("Connection", "close")
                .put("Cookie", "_gauges_unique_year=1; _gauges_unique=1; _gauges_unique_hour=1; " +
                        "_gauges_unique_day=1; _gauges_unique_month=1")
                .put("Host", "httpbin.org")
                .put("Referer", "http://httpbin.org/")
                .put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "snap Chromium/71.0.3578.98 Chrome/71.0.3578.98 Safari/537.36");
        expectedObject.put("origin", "xxx.xxx.xxx.xxx");
        expectedObject.put("url", "http://httpbin.org/get");

        Assertions.assertThat(resultRef.get()).isEqualTo((expectedObject));
    }

    @Test
    public void testJsonObjectEntityConsumer() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(factory);

        URL resource = getClass().getResource("/sample1.json");
        Assertions.assertThat(resource).isNotNull();

        AtomicReference<RequestData> resultRef = new AtomicReference<>();
        JsonObjectEntityConsumer<RequestData> entityConsumer = new JsonObjectEntityConsumer<>(objectMapper, RequestData.class);
        try (InputStream inputStream = resource.openStream()) {
            entityConsumer.streamStart(
                    new BasicEntityDetails(-1, ContentType.APPLICATION_JSON),
                    new FutureCallback<RequestData>() {

                        @Override
                        public void completed(RequestData result) {
                            resultRef.set(result);
                        }

                        @Override
                        public void failed(Exception ex) {
                        }

                        @Override
                        public void cancelled() {
                        }

                    });
            byte[] bytebuf = new byte[1024];
            int len;
            while ((len = inputStream.read(bytebuf)) != -1) {
                entityConsumer.consume(ByteBuffer.wrap(bytebuf, 0, len));
            }
            entityConsumer.streamEnd(null);
        }

        RequestData expectedObject = new RequestData();
        expectedObject.setArgs(new HashMap<>());
        expectedObject.generateHeaders(
                new BasicHeader("Accept", "application/json"),
                new BasicHeader("Accept-Encoding", "gzip, deflate"),
                new BasicHeader("Accept-Language", "en-US,en;q=0.9"),
                new BasicHeader("Connection", "close"),
                new BasicHeader("Cookie", "_gauges_unique_year=1; _gauges_unique=1; _gauges_unique_hour=1; " +
                        "_gauges_unique_day=1; _gauges_unique_month=1"),
                new BasicHeader("Host", "httpbin.org"),
                new BasicHeader("Referer", "http://httpbin.org/"),
                new BasicHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "snap Chromium/71.0.3578.98 Chrome/71.0.3578.98 Safari/537.36"));
        expectedObject.setOrigin("xxx.xxx.xxx.xxx");
        expectedObject.setUrl(URI.create("http://httpbin.org/get"));

        Assertions.assertThat(resultRef.get()).isEqualToComparingFieldByField(expectedObject);
    }

    @Test
    public void testJsonTypeReferenceEntityConsumer() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(factory);

        URL resource = getClass().getResource("/sample4.json");
        Assertions.assertThat(resource).isNotNull();

        AtomicReference<List<String>> resultRef = new AtomicReference<>();
        JsonObjectEntityConsumer<List<String>> entityConsumer = new JsonObjectEntityConsumer<>(objectMapper, new TypeReference<List<String>>() { });
        try (InputStream inputStream = resource.openStream()) {
            entityConsumer.streamStart(
                    new BasicEntityDetails(-1, ContentType.APPLICATION_JSON),
                    new FutureCallback<List<String>>() {

                        @Override
                        public void completed(List<String> result) {
                            resultRef.set(result);
                        }

                        @Override
                        public void failed(Exception ex) {
                        }

                        @Override
                        public void cancelled() {
                        }

                    });
            byte[] bytebuf = new byte[1024];
            int len;
            while ((len = inputStream.read(bytebuf)) != -1) {
                entityConsumer.consume(ByteBuffer.wrap(bytebuf, 0, len));
            }
            entityConsumer.streamEnd(null);
        }

        Assertions.assertThat(resultRef.get()).containsExactly("1", "2", "3", "4");
    }

    @Test
    public void testJsonSequenceEntityConsumer() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(factory);

        URL resource = getClass().getResource("/sample3.json");
        Assertions.assertThat(resource).isNotNull();

        AtomicReference<Long> resultRef = new AtomicReference<>();
        AtomicInteger started = new AtomicInteger(0);
        List<RequestData> jsonDataList = new ArrayList<>();
        AtomicInteger ended = new AtomicInteger(0);
        JsonSequenceEntityConsumer<RequestData> entityConsumer = new JsonSequenceEntityConsumer<>(
                objectMapper,
                RequestData.class,
                new JsonResultSink<RequestData>() {

                    @Override
                    public void begin(int sizeHint) {
                        started.incrementAndGet();
                    }

                    @Override
                    public void accept(RequestData data) {
                        jsonDataList.add(data);
                    }

                    @Override
                    public void end() {
                        ended.incrementAndGet();
                    }

                });
        try (InputStream inputStream = resource.openStream()) {
            entityConsumer.streamStart(
                    new BasicEntityDetails(-1, ContentType.APPLICATION_JSON),
                    new FutureCallback<Long>() {

                        @Override
                        public void completed(Long result) {
                            resultRef.set(result);
                        }

                        @Override
                        public void failed(Exception ex) {
                        }

                        @Override
                        public void cancelled() {
                        }

                    });
            byte[] bytebuf = new byte[1024];
            int len;
            while ((len = inputStream.read(bytebuf)) != -1) {
                entityConsumer.consume(ByteBuffer.wrap(bytebuf, 0, len));
            }
            entityConsumer.streamEnd(null);
        }

        Assertions.assertThat(resultRef.get()).isEqualTo((3L));

        Assertions.assertThat(jsonDataList).hasSize(3);
        Assertions.assertThat(started.get()).isEqualTo((1));
        Assertions.assertThat(ended.get()).isEqualTo((1));


        RequestData expectedObject1 = new RequestData();
        expectedObject1.setId(0);
        expectedObject1.setUrl(URI.create("http://httpbin.org/stream/3"));
        expectedObject1.setArgs(new HashMap<>());
        expectedObject1.generateHeaders(
                new BasicHeader("Host", "httpbin.org"),
                new BasicHeader("Connection", "close"),
                new BasicHeader("Referer", "http://httpbin.org/"),
                new BasicHeader("Accept", "application/json"),
                new BasicHeader("Accept-Encoding", "gzip, deflate"),
                new BasicHeader("Accept-Language", "en-US,en;q=0.9"),
                new BasicHeader("Cookie", "_gauges_unique_year=1; _gauges_unique=1; _gauges_unique_month=1; " +
                        "_gauges_unique_day=1; _gauges_unique_hour=1"),
                new BasicHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "snap Chromium/71.0.3578.98 Chrome/71.0.3578.98 Safari/537.36"));
        expectedObject1.setOrigin("xxx.xxx.xxx.xxx");

        Assertions.assertThat(jsonDataList.get(0)).isEqualToComparingFieldByField(expectedObject1);

        RequestData expectedObject2 = new RequestData();
        expectedObject2.setId(1);
        expectedObject2.setUrl(URI.create("http://httpbin.org/stream/3"));
        expectedObject2.setArgs(new HashMap<>());
        expectedObject2.generateHeaders(
                new BasicHeader("Host", "httpbin.org"),
                new BasicHeader("Connection", "close"),
                new BasicHeader("Referer", "http://httpbin.org/"),
                new BasicHeader("Accept", "application/json"),
                new BasicHeader("Accept-Encoding", "gzip, deflate"),
                new BasicHeader("Accept-Language", "en-US,en;q=0.9"),
                new BasicHeader("Cookie", "_gauges_unique_year=1; _gauges_unique=1; _gauges_unique_month=1; " +
                        "_gauges_unique_day=1; _gauges_unique_hour=1"),
                new BasicHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "snap Chromium/71.0.3578.98 Chrome/71.0.3578.98 Safari/537.36"));
        expectedObject2.setOrigin("xxx.xxx.xxx.xxx");

        Assertions.assertThat(jsonDataList.get(1)).isEqualToComparingFieldByField(expectedObject2);

        RequestData expectedObject3 = new RequestData();
        expectedObject3.setId(2);
        expectedObject3.setUrl(URI.create("http://httpbin.org/stream/3"));
        expectedObject3.setArgs(new HashMap<>());
        expectedObject3.generateHeaders(
                new BasicHeader("Host", "httpbin.org"),
                new BasicHeader("Connection", "close"),
                new BasicHeader("Referer", "http://httpbin.org/"),
                new BasicHeader("Accept", "application/json"),
                new BasicHeader("Accept-Encoding", "gzip, deflate"),
                new BasicHeader("Accept-Language", "en-US,en;q=0.9"),
                new BasicHeader("Cookie", "_gauges_unique_year=1; _gauges_unique=1; _gauges_unique_month=1; " +
                        "_gauges_unique_day=1; _gauges_unique_hour=1"),
                new BasicHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "snap Chromium/71.0.3578.98 Chrome/71.0.3578.98 Safari/537.36"));
        expectedObject3.setOrigin("xxx.xxx.xxx.xxx");

        Assertions.assertThat(jsonDataList.get(2)).isEqualToComparingFieldByField((expectedObject3));
    }

    @Test
    public void testJsonTypeReferenceSequenceEntityConsumer() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(factory);

        URL resource = getClass().getResource("/sample5.json");
        Assertions.assertThat(resource).isNotNull();

        AtomicReference<Long> resultRef = new AtomicReference<>();
        AtomicInteger started = new AtomicInteger(0);
        List<List<String>> jsonDataList = new ArrayList<>();
        AtomicInteger ended = new AtomicInteger(0);
        JsonSequenceEntityConsumer<List<String>> entityConsumer = new JsonSequenceEntityConsumer<>(
                objectMapper,
                new TypeReference<List<String>>() { },
                new JsonResultSink<List<String>>() {

                    @Override
                    public void begin(int sizeHint) {
                        started.incrementAndGet();
                    }

                    @Override
                    public void accept(List<String> data) {
                        jsonDataList.add(data);
                    }

                    @Override
                    public void end() {
                        ended.incrementAndGet();
                    }

                });
        try (InputStream inputStream = resource.openStream()) {
            entityConsumer.streamStart(
                    new BasicEntityDetails(-1, ContentType.APPLICATION_JSON),
                    new FutureCallback<Long>() {

                        @Override
                        public void completed(Long result) {
                            resultRef.set(result);
                        }

                        @Override
                        public void failed(Exception ex) {
                        }

                        @Override
                        public void cancelled() {
                        }

                    });
            byte[] bytebuf = new byte[1024];
            int len;
            while ((len = inputStream.read(bytebuf)) != -1) {
                entityConsumer.consume(ByteBuffer.wrap(bytebuf, 0, len));
            }
            entityConsumer.streamEnd(null);
        }

        Assertions.assertThat(resultRef.get()).isEqualTo((3L));

        Assertions.assertThat(jsonDataList).hasSize(3);
        Assertions.assertThat(started.get()).isEqualTo((1));
        Assertions.assertThat(ended.get()).isEqualTo((1));

        Assertions.assertThat(jsonDataList.get(0)).containsExactly("1", "2", "3", "4");
        Assertions.assertThat(jsonDataList.get(1)).containsExactly("5", "6", "7", "8");
        Assertions.assertThat(jsonDataList.get(2)).containsExactly("9", "10", "11", "12");
    }

}
