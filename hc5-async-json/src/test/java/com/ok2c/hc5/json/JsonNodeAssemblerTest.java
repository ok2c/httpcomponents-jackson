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

import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;

import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonNodeAssemblerTest {

    @Test
    public void testJsonNodeAssembly() throws Exception {
        JsonFactory factory = new JsonFactory();
        JsonAsyncTokenizer jsonTokenizer = new JsonAsyncTokenizer(factory);

        URL resource1 = getClass().getResource("/sample1.json");
        Assertions.assertThat(resource1).isNotNull();

        URL resource2 = getClass().getResource("/sample2.json");
        Assertions.assertThat(resource2).isNotNull();

        JsonNodeAssembler jsonNodeAssembler1 = JsonNodeAssembler.create();

        try (InputStream inputStream = resource1.openStream()) {
            jsonTokenizer.initialize(new JsonTokenEventHandlerAdaptor(jsonNodeAssembler1));
            byte[] bytebuf = new byte[1024];
            int len;
            while ((len = inputStream.read(bytebuf)) != -1) {
                jsonTokenizer.consume(ByteBuffer.wrap(bytebuf, 0, len));
            }
            jsonTokenizer.streamEnd();
        }

        JsonNode jsonNode1 = jsonNodeAssembler1.getResult();
        ObjectNode expectedObject1 = JsonNodeFactory.instance.objectNode();
        expectedObject1.putObject("args");
        expectedObject1.putObject("headers")
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
        expectedObject1.put("origin", "xxx.xxx.xxx.xxx");
        expectedObject1.put("url", "http://httpbin.org/get");

        Assertions.assertThat(jsonNode1).isEqualTo((expectedObject1));

        JsonNodeAssembler jsonNodeAssembler2 = JsonNodeAssembler.create();

        try (InputStream inputStream = resource2.openStream()) {
            jsonTokenizer.initialize(new JsonTokenEventHandlerAdaptor(jsonNodeAssembler2));
            byte[] bytebuf = new byte[1024];
            int len;
            while ((len = inputStream.read(bytebuf)) != -1) {
                jsonTokenizer.consume(ByteBuffer.wrap(bytebuf, 0, len));
            }
            jsonTokenizer.streamEnd();
        }

        JsonNode jsonNode2 = jsonNodeAssembler2.getResult();
        ArrayNode expectedObject2 = JsonNodeFactory.instance.arrayNode();
        expectedObject2.addArray().add(1).add(2).add(3);
        expectedObject2.addArray().add(1.1).add(2.2).add(3.3);
        expectedObject2.addArray()
                .add(JsonNodeFactory.instance.objectNode().put("name1", "value1"))
                .add(JsonNodeFactory.instance.objectNode().put("name2", "value2"))
                .add(JsonNodeFactory.instance.objectNode().put("name3", "value3"));
        expectedObject2.addArray()
                .add(2)
                .add(2.2)
                .add(JsonNodeFactory.instance.objectNode().put("name2", "value2"));

        Assertions.assertThat(jsonNode2).isEqualTo((expectedObject2));
    }

}
