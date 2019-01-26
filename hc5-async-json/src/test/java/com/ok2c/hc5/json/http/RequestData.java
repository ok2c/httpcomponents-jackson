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

import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.NameValuePair;

import com.fasterxml.jackson.databind.JsonNode;

public class RequestData {

    private int id;
    private URI url;
    private String origin;
    private Map<String, String> headers;
    private Map<String, String> args;
    private String data;
    private JsonNode json;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void generateHeaders(Header... headers) {
        this.headers = Arrays.stream(headers)
                .collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));
    }

    public Map<String, String> getArgs() {
        return args;
    }

    public void setArgs(Map<String, String> args) {
        this.args = args;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public JsonNode getJson() {
        return json;
    }

    public void setJson(JsonNode json) {
        this.json = json;
    }

    @Override
    public String toString() {
        return "RequestData{" +
                "id=" + id +
                ", url=" + url +
                ", origin='" + origin + '\'' +
                ", headers=" + headers +
                ", args=" + args +
                ", data='" + data + '\'' +
                ", json=" + json +
                '}';
    }

}
