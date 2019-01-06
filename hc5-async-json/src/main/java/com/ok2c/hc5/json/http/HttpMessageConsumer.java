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

import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpMessage;

/**
 * Represents an operation that accepts a {@link HttpMessage} head as input.
 *
 * @param <H> type of HTTP message.
 */
@FunctionalInterface
public interface HttpMessageConsumer<H extends HttpMessage> {

    /**
     * Triggered to signal receipt of an HTTP message head.
     * @param messageHead the message head.
     */
    void accept(H messageHead) throws HttpException;

}
