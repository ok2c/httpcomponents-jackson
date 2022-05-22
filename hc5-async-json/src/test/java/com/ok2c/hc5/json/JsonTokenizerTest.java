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

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonTokenId;

public class JsonTokenizerTest {

    @Test
    public void testTokenizer() throws Exception {
        JsonFactory factory = new JsonFactory();
        JsonTokenizer jsonTokenizer = new JsonTokenizer(factory);

        URL resource1 = getClass().getResource("/sample1.json");
        Assertions.assertThat(resource1).isNotNull();

        List<Integer> tokens1 = new ArrayList<>();
        try (InputStream inputStream = resource1.openStream()) {
            jsonTokenizer.tokenize(inputStream, (tokenId, jsonParser) -> tokens1.add(tokenId));
        }

        Assertions.assertThat(tokens1).containsExactly(
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
        );

        URL resource2 = getClass().getResource("/sample2.json");
        Assertions.assertThat(resource2).isNotNull();

        List<Integer> tokens2 = new ArrayList<>();
        try (InputStream inputStream = resource2.openStream()) {
            jsonTokenizer.tokenize(inputStream, (tokenId, jsonParser) -> tokens2.add(tokenId));
        }

        Assertions.assertThat(tokens2).containsExactly(
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
        );

        URL resource3 = getClass().getResource("/sample3.json");
        Assertions.assertThat(resource3).isNotNull();

        List<Integer> tokens3 = new ArrayList<>();
        try (InputStream inputStream = resource3.openStream()) {
            jsonTokenizer.tokenize(inputStream, (tokenId, jsonParser) -> tokens3.add(tokenId));
        }

        Assertions.assertThat(tokens3).containsExactly(
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
        );

        URL resource6 = getClass().getResource("/sample6.json");
        Assertions.assertThat(resource6).isNotNull();

        List<Integer> tokens6 = new ArrayList<>();
        try (InputStream inputStream = resource6.openStream()) {
            jsonTokenizer.tokenize(inputStream, (tokenId, jsonParser) -> tokens6.add(tokenId));
        }

        Assertions.assertThat(tokens6).containsExactly(
                JsonTokenId.ID_START_ARRAY,
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
                JsonTokenId.ID_END_ARRAY,
                JsonTokenId.ID_NO_TOKEN
        );
    }

    @Test
    public void testAsyncTokenizer() throws Exception {
        JsonFactory factory = new JsonFactory();
        JsonAsyncTokenizer jsonTokenizer = new JsonAsyncTokenizer(factory);

        URL resource1 = getClass().getResource("/sample1.json");
        Assertions.assertThat(resource1).isNotNull();

        URL resource2 = getClass().getResource("/sample2.json");
        Assertions.assertThat(resource2).isNotNull();

        URL resource3 = getClass().getResource("/sample3.json");
        Assertions.assertThat(resource2).isNotNull();

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

            Assertions.assertThat(tokens1).containsExactly(
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
            );

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

            Assertions.assertThat(tokens2).containsExactly(
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
            );

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

            Assertions.assertThat(tokens3).containsExactly(
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
            );
        }
    }

    @Test
    public void testConsumePartial() throws Exception {
        JsonFactory factory = new JsonFactory();
        JsonAsyncTokenizer jsonTokenizer = new JsonAsyncTokenizer(factory);

        ByteBuffer b1 = ByteBuffer.wrap(new byte[]{0, 0, 0, '{', '"', 'n', 'a', 'm', 0, 0, 0}, 3, 5);
        ByteBuffer b2 = ByteBuffer.wrap(new byte[]{0, 0, 'e', '"', ' ', ' ', ':', ' ', 0}, 2, 6);
        ByteBuffer b3 = ByteBuffer.wrap(new byte[]{0, 0, 0, 0, '"', 'v', 'a', 'l', 'u', 'e', '"', ' ', ' ', '}'}, 4, 10);

        List<Integer> tokens1 = new ArrayList<>();
        jsonTokenizer.initialize((tokenId, jsonParser) -> tokens1.add(tokenId));

        jsonTokenizer.consume(b1);
        jsonTokenizer.consume(b2);
        jsonTokenizer.consume(b3);
        jsonTokenizer.streamEnd();

        Assertions.assertThat(tokens1).containsExactly(
                JsonTokenId.ID_START_OBJECT,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_END_OBJECT,
                JsonTokenId.ID_NO_TOKEN
        );

        List<Integer> tokens2 = new ArrayList<>();
        jsonTokenizer.initialize((tokenId, jsonParser) -> tokens2.add(tokenId));

        ByteBuffer b4 = ByteBuffer.wrap(new byte[]{0, 0, 0, '{', '"', 'n', 'a', 'm', 0, 0, 0}, 3, 5).slice();
        ByteBuffer b5 = ByteBuffer.wrap(new byte[]{0, 0, 'e', '"', ' ', ' ', ':', ' ', 0}, 2, 6).slice();
        ByteBuffer b6 = ByteBuffer.wrap(new byte[]{0, 0, 0, 0, '"', 'v', 'a', 'l', 'u', 'e', '"', ' ', ' ', '}'}, 4, 10).slice();
        jsonTokenizer.consume(b4);
        jsonTokenizer.consume(b5);
        jsonTokenizer.consume(b6);
        jsonTokenizer.streamEnd();

        Assertions.assertThat(tokens2).containsExactly(
                JsonTokenId.ID_START_OBJECT,
                JsonTokenId.ID_FIELD_NAME,
                JsonTokenId.ID_STRING,
                JsonTokenId.ID_END_OBJECT,
                JsonTokenId.ID_NO_TOKEN
        );
    }

}
