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
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.http.nio.DataStreamChannel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class JsonEntityProducerTest {

    static class MockDataStreamChannel implements DataStreamChannel {

        private final WritableByteChannel byteChannel;

        public MockDataStreamChannel(final WritableByteChannel byteChannel) {
            this.byteChannel = byteChannel;
        }

        @Override
        public void requestOutput() {
        }

        @Override
        public int write(final ByteBuffer src) throws IOException {
            return byteChannel.write(src);
        }

        @Override
        public void endStream() throws IOException {
            if (byteChannel.isOpen()) {
                byteChannel.close();
            }
        }

        @Override
        public void endStream(final List<? extends Header> trailers) throws IOException {
            endStream();
        }

        public boolean isOpen() {
            return byteChannel.isOpen();
        }

    }

    @Test
    public void testJsonObjectEntityProducer() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(factory);

        JsonObjectEntityProducer<List<NameValuePair>> producer = new JsonObjectEntityProducer<>(
                Arrays.asList(
                        new BasicNameValuePair("param1", "value"),
                        new BasicNameValuePair("param2", "blah-blah-blah-blah-blah-blah-blah-blah-blah-blah-blah"))
                , objectMapper);

        int[][] params = new int[][]{ {1024, -1}, {16, 16}, {32, 32} };

        for (int i = 0; i < params.length; i++) {
            WritableByteChannelMock byteChannel = new WritableByteChannelMock(params[i][0], params[i][1]);
            MockDataStreamChannel dataChannel = new MockDataStreamChannel(byteChannel);
            while (dataChannel.isOpen()) {
                producer.produce(dataChannel);
                byteChannel.flush();
            }
            Assertions.assertThat(byteChannel.dump(StandardCharsets.US_ASCII)).isEqualTo("[" +
                            "{\"name\":\"param1\",\"value\":\"value\"}," +
                            "{\"name\":\"param2\",\"value\":\"blah-blah-blah-blah-blah-blah-blah-blah-blah-blah-blah\"}" +
                            "]");
            producer.releaseResources();
        }
    }

    @Test
    public void testJsonNodeEntityProducer() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(factory);

        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
        arrayNode.add(JsonNodeFactory.instance.
                objectNode().put("name", "param1").put("value", "value"));
        arrayNode.add(JsonNodeFactory.instance.
                objectNode().put("name", "param2").put("value", "blah-blah-blah-blah-blah-blah-blah-blah-blah-blah-blah"));

        JsonNodeEntityProducer producer = new JsonNodeEntityProducer(arrayNode, objectMapper);

        int[][] params = new int[][]{ {1024, -1}, {16, 16}, {32, 32} };

        for (int i = 0; i < params.length; i++) {
            WritableByteChannelMock byteChannel = new WritableByteChannelMock(params[i][0], params[i][1]);
            MockDataStreamChannel dataChannel = new MockDataStreamChannel(byteChannel);
            while (dataChannel.isOpen()) {
                producer.produce(dataChannel);
                byteChannel.flush();
            }
            Assertions.assertThat(byteChannel.dump(StandardCharsets.US_ASCII)).isEqualTo("[" +
                            "{\"name\":\"param1\",\"value\":\"value\"}," +
                            "{\"name\":\"param2\",\"value\":\"blah-blah-blah-blah-blah-blah-blah-blah-blah-blah-blah\"}" +
                            "]");
            producer.releaseResources();
        }
    }

    @Test
    public void testJsonObjectSequenceEntityProducer() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(factory);

        AtomicInteger count = new AtomicInteger(0);

        JsonSequenceEntityProducer<NameValuePair> producer = new JsonSequenceEntityProducer<>(
                objectMapper,
                1024,
                channel -> {
                    switch (count.incrementAndGet()) {
                        case 1:
                            channel.write(
                                    new BasicNameValuePair("param1", "value"));
                            break;
                        case 2:
                            channel.write(
                                    new BasicNameValuePair("param2", "blah-blah-blah-blah-blah-blah-blah-blah-blah-blah-blah"));
                            break;
                        default:
                            channel.endStream();
                    }

                });

        int[][] params = new int[][]{ {1024, -1}, {16, 16}, {32, 32} };

        for (int i = 0; i < params.length; i++) {
            WritableByteChannelMock byteChannel = new WritableByteChannelMock(params[i][0], params[i][1]);
            MockDataStreamChannel dataChannel = new MockDataStreamChannel(byteChannel);
            while (dataChannel.isOpen()) {
                producer.produce(dataChannel);
                byteChannel.flush();
            }
            Assertions.assertThat(byteChannel.dump(StandardCharsets.US_ASCII)).isEqualTo(
                            "{\"name\":\"param1\",\"value\":\"value\"}" +
                            "{\"name\":\"param2\",\"value\":\"blah-blah-blah-blah-blah-blah-blah-blah-blah-blah-blah\"}"
                    );
            producer.releaseResources();
            count.set(0);
        }
    }

}
