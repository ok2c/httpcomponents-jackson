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
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonTokenId;

public class JsonTokenizerTest {

    @Test
    public void testTokenizer() throws Exception {
        JsonFactory factory = new JsonFactory();
        JsonTokenizer jsonTokenizer = new JsonTokenizer(factory);

        URL resource1 = getClass().getResource("/sample1.json");
        Assert.assertThat(resource1, CoreMatchers.notNullValue());

        List<Integer> tokens1 = new ArrayList<>();
        try (InputStream inputStream = resource1.openStream()) {
            jsonTokenizer.tokenize(inputStream, (tokenId, jsonParser) -> tokens1.add(tokenId));
        }

        Assert.assertThat(tokens1, Matchers.contains(
                JsonTokenId.ID_START_OBJECT,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_START_OBJECT,
                JsonTokenId.ID_END_OBJECT,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_START_OBJECT,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_END_OBJECT,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_END_OBJECT,
                JsonTokenId.ID_NO_TOKEN
        ));

        URL resource2 = getClass().getResource("/sample2.json");
        Assert.assertThat(resource2, CoreMatchers.notNullValue());

        List<Integer> tokens2 = new ArrayList<>();
        try (InputStream inputStream = resource2.openStream()) {
            jsonTokenizer.tokenize(inputStream, (tokenId, jsonParser) -> tokens2.add(tokenId));
        }

        Assert.assertThat(tokens2, Matchers.contains(
                JsonTokenId.ID_START_ARRAY,
                JsonTokenId.ID_START_ARRAY,
                JsonTokenId.ID_NUMBER_INT,
                JsonTokenId.ID_NUMBER_INT,
                JsonTokenId.ID_NUMBER_INT,
                JsonTokenId.ID_END_ARRAY,
                JsonTokenId.ID_START_ARRAY,
                JsonTokenId.ID_NUMBER_FLOAT,
                JsonTokenId.ID_NUMBER_FLOAT,
                JsonTokenId.ID_NUMBER_FLOAT,
                JsonTokenId.ID_END_ARRAY,
                JsonTokenId.ID_START_ARRAY,
                JsonTokenId.ID_START_OBJECT,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_END_OBJECT,
                JsonTokenId.ID_START_OBJECT,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_END_OBJECT,
                JsonTokenId.ID_START_OBJECT,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_END_OBJECT,
                JsonTokenId.ID_END_ARRAY,
                JsonTokenId.ID_START_ARRAY,
                JsonTokenId.ID_NUMBER_INT,
                JsonTokenId.ID_NUMBER_FLOAT,
                JsonTokenId.ID_START_OBJECT,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_END_OBJECT,
                JsonTokenId.ID_END_ARRAY,
                JsonTokenId.ID_END_ARRAY,
                JsonTokenId.ID_NO_TOKEN
        ));

        URL resource3 = getClass().getResource("/sample3.json");
        Assert.assertThat(resource3, CoreMatchers.notNullValue());

        List<Integer> tokens3 = new ArrayList<>();
        try (InputStream inputStream = resource3.openStream()) {
            jsonTokenizer.tokenize(inputStream, (tokenId, jsonParser) -> tokens3.add(tokenId));
        }

        Assert.assertThat(tokens3, Matchers.contains(
                JsonTokenId.ID_START_OBJECT,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_START_OBJECT,
                JsonTokenId.ID_END_OBJECT,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_START_OBJECT,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_END_OBJECT,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_NUMBER_INT,
                JsonTokenId.ID_END_OBJECT,
                JsonTokenId.ID_START_OBJECT,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_START_OBJECT,
                JsonTokenId.ID_END_OBJECT,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_START_OBJECT,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_END_OBJECT,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_NUMBER_INT,
                JsonTokenId.ID_END_OBJECT,
                JsonTokenId.ID_START_OBJECT,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_START_OBJECT,
                JsonTokenId.ID_END_OBJECT,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_START_OBJECT,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_END_OBJECT,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_NUMBER_INT,
                JsonTokenId.ID_END_OBJECT,
                JsonTokenId.ID_NO_TOKEN
        ));
    }

    @Test
    public void testAsyncTokenizer() throws Exception {
        JsonFactory factory = new JsonFactory();
        JsonAsyncTokenizer jsonTokenizer = new JsonAsyncTokenizer(factory);

        URL resource1 = getClass().getResource("/sample1.json");
        Assert.assertThat(resource1, CoreMatchers.notNullValue());

        URL resource2 = getClass().getResource("/sample2.json");
        Assert.assertThat(resource2, CoreMatchers.notNullValue());

        URL resource3 = getClass().getResource("/sample3.json");
        Assert.assertThat(resource2, CoreMatchers.notNullValue());

        for (int bufSize : new int[]{2048, 1024, 256, 32, 16, 8}) {
            List<Integer> tokens1 = new ArrayList<>();
            try (InputStream inputStream = resource1.openStream()) {
                jsonTokenizer.initialize((tokenId, jsonParser) -> tokens1.add(tokenId));
                byte[] buf = new byte[bufSize];
                int len;
                while ((len = inputStream.read(buf)) != -1) {
                    jsonTokenizer.consume(ByteBuffer.wrap(buf, 0, len));
                }
                jsonTokenizer.streamEnd();
            }

            Assert.assertThat(tokens1, Matchers.contains(
                    JsonTokenId.ID_START_OBJECT,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_START_OBJECT,
                    JsonTokenId.ID_END_OBJECT,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_START_OBJECT,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_END_OBJECT,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_END_OBJECT,
                    JsonTokenId.ID_NO_TOKEN
            ));

            List<Integer> tokens2 = new ArrayList<>();
            try (InputStream inputStream = resource2.openStream()) {
                jsonTokenizer.initialize((tokenId, jsonParser) -> tokens2.add(tokenId));
                byte[] buf = new byte[bufSize];
                int len;
                while ((len = inputStream.read(buf)) != -1) {
                    jsonTokenizer.consume(ByteBuffer.wrap(buf, 0, len));
                }
                jsonTokenizer.streamEnd();
            }

            Assert.assertThat(tokens2, Matchers.contains(
                    JsonTokenId.ID_START_ARRAY,
                    JsonTokenId.ID_START_ARRAY,
                    JsonTokenId.ID_NUMBER_INT,
                    JsonTokenId.ID_NUMBER_INT,
                    JsonTokenId.ID_NUMBER_INT,
                    JsonTokenId.ID_END_ARRAY,
                    JsonTokenId.ID_START_ARRAY,
                    JsonTokenId.ID_NUMBER_FLOAT,
                    JsonTokenId.ID_NUMBER_FLOAT,
                    JsonTokenId.ID_NUMBER_FLOAT,
                    JsonTokenId.ID_END_ARRAY,
                    JsonTokenId.ID_START_ARRAY,
                    JsonTokenId.ID_START_OBJECT,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_END_OBJECT,
                    JsonTokenId.ID_START_OBJECT,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_END_OBJECT,
                    JsonTokenId.ID_START_OBJECT,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_END_OBJECT,
                    JsonTokenId.ID_END_ARRAY,
                    JsonTokenId.ID_START_ARRAY,
                    JsonTokenId.ID_NUMBER_INT,
                    JsonTokenId.ID_NUMBER_FLOAT,
                    JsonTokenId.ID_START_OBJECT,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_END_OBJECT,
                    JsonTokenId.ID_END_ARRAY,
                    JsonTokenId.ID_END_ARRAY,
                    JsonTokenId.ID_NO_TOKEN
            ));

            List<Integer> tokens3 = new ArrayList<>();
            try (InputStream inputStream = resource3.openStream()) {
                jsonTokenizer.initialize((tokenId, jsonParser) -> tokens3.add(tokenId));
                byte[] buf = new byte[bufSize];
                int len;
                while ((len = inputStream.read(buf)) != -1) {
                    jsonTokenizer.consume(ByteBuffer.wrap(buf, 0, len));
                }
                jsonTokenizer.streamEnd();
            }

            Assert.assertThat(tokens3, Matchers.contains(
                    JsonTokenId.ID_START_OBJECT,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_START_OBJECT,
                    JsonTokenId.ID_END_OBJECT,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_START_OBJECT,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_END_OBJECT,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_NUMBER_INT,
                    JsonTokenId.ID_END_OBJECT,
                    JsonTokenId.ID_START_OBJECT,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_START_OBJECT,
                    JsonTokenId.ID_END_OBJECT,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_START_OBJECT,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_END_OBJECT,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_NUMBER_INT,
                    JsonTokenId.ID_END_OBJECT,
                    JsonTokenId.ID_START_OBJECT,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_START_OBJECT,
                    JsonTokenId.ID_END_OBJECT,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_START_OBJECT,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_END_OBJECT,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_STRING,
                    JsonTokenId.ID_FIELD_NAME,
                    JsonTokenId.ID_NUMBER_INT,
                    JsonTokenId.ID_END_OBJECT,
                    JsonTokenId.ID_NO_TOKEN
            ));
        }
    }

}
