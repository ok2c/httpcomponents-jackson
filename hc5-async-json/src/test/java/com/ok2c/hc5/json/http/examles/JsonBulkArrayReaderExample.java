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

import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ok2c.hc5.json.bulk.JsonBulkArrayReader;
import com.ok2c.hc5.json.http.RequestData;

public class JsonBulkArrayReaderExample {

    public static void main(String... args) throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(factory);

        URL resource = JsonBulkArrayReaderExample.class.getResource("/sample6.json");

        JsonBulkArrayReader bulkArrayReader = new JsonBulkArrayReader(objectMapper);
        bulkArrayReader.initialize(new TypeReference<RequestData>() {}, entry -> System.out.println(entry));
        try (InputStream inputStream = resource.openStream()) {
            int l;
            byte[] tmp = new byte[4096];
            while ((l = inputStream.read(tmp)) != -1) {
                bulkArrayReader.consume(ByteBuffer.wrap(tmp, 0, l));
            }
            bulkArrayReader.streamEnd();
        }
    }

}
