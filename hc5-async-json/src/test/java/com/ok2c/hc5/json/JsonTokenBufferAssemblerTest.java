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
package com.ok2c.hc5.json;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class JsonTokenBufferAssemblerTest {

    @Test
    public void testTokenBufferAssembly() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(factory);
        JsonAsyncTokenizer jsonTokenizer = new JsonAsyncTokenizer(factory);

        URL resource = getClass().getResource("/sample1.json");
        Assertions.assertThat(resource).isNotNull();

        AtomicReference<TokenBuffer> tokenBufferRef = new AtomicReference<>(null);
        try (InputStream inputStream = resource.openStream()) {
            jsonTokenizer.initialize(new TokenBufferAssembler(tokenBufferRef::set));
            byte[] bytebuf = new byte[1024];
            int len;
            while ((len = inputStream.read(bytebuf)) != -1) {
                jsonTokenizer.consume(ByteBuffer.wrap(bytebuf, 0, len));
            }
            jsonTokenizer.streamEnd();
        }

        TokenBuffer tokenBuffer = tokenBufferRef.get();
        Assertions.assertThat(tokenBuffer).isNotNull();
        JsonNode jsonNode = objectMapper.readTree(tokenBuffer.asParserOnFirstToken());

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

        Assertions.assertThat(jsonNode).isEqualTo(expectedObject);
    }

    @Test
    public void testTokenBufferSequenceAssembly() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(factory);
        JsonAsyncTokenizer jsonTokenizer = new JsonAsyncTokenizer(factory);

        URL resource = getClass().getResource("/sample3.json");
        Assertions.assertThat(resource).isNotNull();

        AtomicInteger started = new AtomicInteger(0);
        List<TokenBuffer> tokenBufferList = new ArrayList<>();
        AtomicInteger ended = new AtomicInteger(0);
        try (InputStream inputStream = resource.openStream()) {
            jsonTokenizer.initialize(new TokenBufferAssembler(new JsonResultSink<TokenBuffer>() {

                @Override
                public void begin(int sizeHint) {
                    started.incrementAndGet();
                }

                @Override
                public void accept(TokenBuffer tokenBuffer) {
                    tokenBufferList.add(tokenBuffer);
                }

                @Override
                public void end() {
                    ended.incrementAndGet();
                }

            }));
            byte[] bytebuf = new byte[1024];
            int len;
            while ((len = inputStream.read(bytebuf)) != -1) {
                jsonTokenizer.consume(ByteBuffer.wrap(bytebuf, 0, len));
            }
            jsonTokenizer.streamEnd();
        }

        Assertions.assertThat(tokenBufferList).hasSize(3);
        Assertions.assertThat(started.get()).isEqualTo(1);
        Assertions.assertThat(ended.get()).isEqualTo((1));

        TokenBuffer tokenBuffer1 = tokenBufferList.get(0);
        Assertions.assertThat(tokenBuffer1).isNotNull();
        JsonNode jsonNode1 = objectMapper.readTree(tokenBuffer1.asParserOnFirstToken());

        ObjectNode expectedObject1 = JsonNodeFactory.instance.objectNode();
        expectedObject1.put("url", "http://httpbin.org/stream/3");
        expectedObject1.putObject("args");
        expectedObject1.putObject("headers")
                .put("Host", "httpbin.org")
                .put("Connection", "close")
                .put("Referer", "http://httpbin.org/")
                .put("Accept", "application/json")
                .put("Accept-Encoding", "gzip, deflate")
                .put("Accept-Language", "en-US,en;q=0.9")
                .put("Cookie", "_gauges_unique_year=1; _gauges_unique=1; _gauges_unique_month=1; _gauges_unique_day=1; " +
                        "_gauges_unique_hour=1")
                .put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "snap Chromium/71.0.3578.98 Chrome/71.0.3578.98 Safari/537.36");
        expectedObject1.put("origin", "xxx.xxx.xxx.xxx");
        expectedObject1.put("id", 0);

        Assertions.assertThat(jsonNode1).isEqualTo((expectedObject1));

        TokenBuffer tokenBuffer2 = tokenBufferList.get(1);
        Assertions.assertThat(tokenBuffer2).isNotNull();
        JsonNode jsonNode2 = objectMapper.readTree(tokenBuffer2.asParserOnFirstToken());

        ObjectNode expectedObject2 = JsonNodeFactory.instance.objectNode();
        expectedObject2.put("url", "http://httpbin.org/stream/3");
        expectedObject2.putObject("args");
        expectedObject2.putObject("headers")
                .put("Host", "httpbin.org")
                .put("Connection", "close")
                .put("Referer", "http://httpbin.org/")
                .put("Accept", "application/json")
                .put("Accept-Encoding", "gzip, deflate")
                .put("Accept-Language", "en-US,en;q=0.9")
                .put("Cookie", "_gauges_unique_year=1; _gauges_unique=1; _gauges_unique_month=1; _gauges_unique_day=1; " +
                        "_gauges_unique_hour=1")
                .put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "snap Chromium/71.0.3578.98 Chrome/71.0.3578.98 Safari/537.36");
        expectedObject2.put("origin", "xxx.xxx.xxx.xxx");
        expectedObject2.put("id", 1);

        Assertions.assertThat(jsonNode2).isEqualTo((expectedObject2));

        TokenBuffer tokenBuffer3 = tokenBufferList.get(2);
        Assertions.assertThat(tokenBuffer3).isNotNull();
        JsonNode jsonNode3 = objectMapper.readTree(tokenBuffer3.asParserOnFirstToken());

        ObjectNode expectedObject3 = JsonNodeFactory.instance.objectNode();
        expectedObject3.put("url", "http://httpbin.org/stream/3");
        expectedObject3.putObject("args");
        expectedObject3.putObject("headers")
                .put("Host", "httpbin.org")
                .put("Connection", "close")
                .put("Referer", "http://httpbin.org/")
                .put("Accept", "application/json")
                .put("Accept-Encoding", "gzip, deflate")
                .put("Accept-Language", "en-US,en;q=0.9")
                .put("Cookie", "_gauges_unique_year=1; _gauges_unique=1; _gauges_unique_month=1; _gauges_unique_day=1; " +
                        "_gauges_unique_hour=1")
                .put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "snap Chromium/71.0.3578.98 Chrome/71.0.3578.98 Safari/537.36");
        expectedObject3.put("origin", "xxx.xxx.xxx.xxx");
        expectedObject3.put("id", 2);

        Assertions.assertThat(jsonNode3).isEqualTo((expectedObject3));
    }

    @Test
    public void testTokenBufferAssemblyNoContent() throws Exception {
        JsonFactory factory = new JsonFactory();
        JsonAsyncTokenizer jsonTokenizer = new JsonAsyncTokenizer(factory);

        AtomicReference<TokenBuffer> tokenBufferRef = new AtomicReference<>(null);
        jsonTokenizer.initialize(new TokenBufferAssembler(tokenBufferRef::set));
        jsonTokenizer.streamEnd();

        Assertions.assertThat(tokenBufferRef.get()).isNull();
    }

    @Test
    public void testTokenBufferArrayAssembly() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(factory);
        JsonAsyncTokenizer jsonTokenizer = new JsonAsyncTokenizer(factory);

        URL resource = getClass().getResource("/sample6.json");
        Assertions.assertThat(resource).isNotNull();

        AtomicInteger started = new AtomicInteger(0);
        List<TokenBuffer> tokenBufferList = new ArrayList<>();
        AtomicInteger ended = new AtomicInteger(0);
        try (InputStream inputStream = resource.openStream()) {
            jsonTokenizer.initialize(new TopLevelArrayTokenFilter(new TokenBufferAssembler(new JsonResultSink<TokenBuffer>() {

                @Override
                public void begin(int sizeHint) {
                    started.incrementAndGet();
                }

                @Override
                public void accept(TokenBuffer tokenBuffer) {
                    tokenBufferList.add(tokenBuffer);
                }

                @Override
                public void end() {
                    ended.incrementAndGet();
                }

            })));
            byte[] bytebuf = new byte[1024];
            int len;
            while ((len = inputStream.read(bytebuf)) != -1) {
                jsonTokenizer.consume(ByteBuffer.wrap(bytebuf, 0, len));
            }
            jsonTokenizer.streamEnd();
        }

        Assertions.assertThat(tokenBufferList).hasSize(3);
        Assertions.assertThat(started.get()).isEqualTo(1);
        Assertions.assertThat(ended.get()).isEqualTo((1));

        TokenBuffer tokenBuffer1 = tokenBufferList.get(0);
        Assertions.assertThat(tokenBuffer1).isNotNull();
        JsonNode jsonNode1 = objectMapper.readTree(tokenBuffer1.asParserOnFirstToken());

        ObjectNode expectedObject1 = JsonNodeFactory.instance.objectNode();
        expectedObject1.put("url", "http://httpbin.org/stream/3");
        expectedObject1.putObject("args");
        expectedObject1.putObject("headers")
                .put("Host", "httpbin.org")
                .put("Connection", "close")
                .put("Referer", "http://httpbin.org/")
                .put("Accept", "application/json")
                .put("Accept-Encoding", "gzip, deflate")
                .put("Accept-Language", "en-US,en;q=0.9")
                .put("Cookie", "_gauges_unique_year=1; _gauges_unique=1; _gauges_unique_month=1; _gauges_unique_day=1; " +
                        "_gauges_unique_hour=1")
                .put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "snap Chromium/71.0.3578.98 Chrome/71.0.3578.98 Safari/537.36");
        expectedObject1.put("origin", "xxx.xxx.xxx.xxx");
        expectedObject1.put("id", 0);

        Assertions.assertThat(jsonNode1).isEqualTo((expectedObject1));

        TokenBuffer tokenBuffer2 = tokenBufferList.get(1);
        Assertions.assertThat(tokenBuffer2).isNotNull();
        JsonNode jsonNode2 = objectMapper.readTree(tokenBuffer2.asParserOnFirstToken());

        ObjectNode expectedObject2 = JsonNodeFactory.instance.objectNode();
        expectedObject2.put("url", "http://httpbin.org/stream/3");
        expectedObject2.putObject("args");
        expectedObject2.putObject("headers")
                .put("Host", "httpbin.org")
                .put("Connection", "close")
                .put("Referer", "http://httpbin.org/")
                .put("Accept", "application/json")
                .put("Accept-Encoding", "gzip, deflate")
                .put("Accept-Language", "en-US,en;q=0.9")
                .put("Cookie", "_gauges_unique_year=1; _gauges_unique=1; _gauges_unique_month=1; _gauges_unique_day=1; " +
                        "_gauges_unique_hour=1")
                .put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "snap Chromium/71.0.3578.98 Chrome/71.0.3578.98 Safari/537.36");
        expectedObject2.put("origin", "xxx.xxx.xxx.xxx");
        expectedObject2.put("id", 1);

        Assertions.assertThat(jsonNode2).isEqualTo((expectedObject2));

        TokenBuffer tokenBuffer3 = tokenBufferList.get(2);
        Assertions.assertThat(tokenBuffer3).isNotNull();
        JsonNode jsonNode3 = objectMapper.readTree(tokenBuffer3.asParserOnFirstToken());

        ObjectNode expectedObject3 = JsonNodeFactory.instance.objectNode();
        expectedObject3.put("url", "http://httpbin.org/stream/3");
        expectedObject3.putObject("args");
        expectedObject3.putObject("headers")
                .put("Host", "httpbin.org")
                .put("Connection", "close")
                .put("Referer", "http://httpbin.org/")
                .put("Accept", "application/json")
                .put("Accept-Encoding", "gzip, deflate")
                .put("Accept-Language", "en-US,en;q=0.9")
                .put("Cookie", "_gauges_unique_year=1; _gauges_unique=1; _gauges_unique_month=1; _gauges_unique_day=1; " +
                        "_gauges_unique_hour=1")
                .put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                        "snap Chromium/71.0.3578.98 Chrome/71.0.3578.98 Safari/537.36");
        expectedObject3.put("origin", "xxx.xxx.xxx.xxx");
        expectedObject3.put("id", 2);

        Assertions.assertThat(jsonNode3).isEqualTo((expectedObject3));
    }

    @ParameterizedTest
    @MethodSource
    public void testTokenBufferSimpleStringAssembly( final String exampleString, final JsonNode expectedJsonNode ) throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(factory);
        JsonAsyncTokenizer jsonTokenizer = new JsonAsyncTokenizer(factory);

        AtomicInteger started = new AtomicInteger(0);
        List<TokenBuffer> tokenBufferList = new ArrayList<>();
        AtomicInteger ended = new AtomicInteger(0);
        try (InputStream inputStream = new ByteArrayInputStream( exampleString.getBytes( StandardCharsets.UTF_8) ) ) {
            jsonTokenizer.initialize(new TopLevelArrayTokenFilter(new TokenBufferAssembler(new JsonResultSink<TokenBuffer>() {

                @Override
                public void begin(int sizeHint) {
                    started.incrementAndGet();
                }

                @Override
                public void accept(TokenBuffer tokenBuffer) {
                    tokenBufferList.add(tokenBuffer);
                }

                @Override
                public void end() {
                    ended.incrementAndGet();
                }

            })));
            byte[] bytebuf = new byte[1024];
            int len;
            while ((len = inputStream.read(bytebuf)) != -1) {
                jsonTokenizer.consume(ByteBuffer.wrap(bytebuf, 0, len));
            }
            jsonTokenizer.streamEnd();
        }

        Assertions.assertThat(tokenBufferList).hasSize(1);
        Assertions.assertThat(started.get()).isEqualTo(1);
        Assertions.assertThat(ended.get()).isEqualTo((1));

        TokenBuffer tokenBuffer1 = tokenBufferList.get(0);
        Assertions.assertThat(tokenBuffer1).isNotNull();
        JsonNode jsonNode1 = objectMapper.readTree(tokenBuffer1.asParserOnFirstToken());

        Assertions.assertThat(jsonNode1).isEqualTo( expectedJsonNode );
    }

    public static Stream<Arguments> testTokenBufferSimpleStringAssembly() {
        return Stream.of( Arguments.of( "7.7", JsonNodeFactory.instance.numberNode( BigDecimal.valueOf( 7.7 ) ) ),
                Arguments.of( "7", JsonNodeFactory.instance.numberNode(7 ) ),
                Arguments.of( "\"RAW_VALUE\"", JsonNodeFactory.instance.textNode( "RAW_VALUE" ) ),
                Arguments.of( "true", JsonNodeFactory.instance.booleanNode( true ) ),
                Arguments.of( "false", JsonNodeFactory.instance.booleanNode( false ) ),
                Arguments.of( "null", JsonNodeFactory.instance.nullNode() ) );
    }
}
