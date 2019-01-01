# Event-driven, asynchronous JSON message processors for Apache HttpComponents 5.0

Apache HttpComponents do not directly provide any support for specific message formats such as XML or JSON.

While it is relatively easy to process HTTP message content with any data processing libraries 
that support the classic (blocking) I/O model based on `InputStream` and `OutputStream` APIs,
there is no such common API for asynchronous data processing in Java 8 or earlier. 

It is a very common anti-pattern with asynchronous HTTP content processing when message body content
gets buffered in memory and then processed using standard blocking `InputStream` and `OutputStream` 
APIs.

This library aims at making asynchronous processing of JSON messages with Apache HttpCore 5.0 and 
Apache HttpClient 5.0 simple and convenient while eliminating intermediate content buffering in memory.
The library uses the asynchronous JSON message parser of the fantastic 
[Jackson JSON processor](https://github.com/FasterXML/jackson) to tokenize JSON content and map onto
a higher level Java object model.

## HTTP response message consumers

There are several `AsyncResponseConsumer` implementations provided by the library that can be 
created using `JsonResponseConsumers` factory class.

1. Request data consumers that convert JSON messages into an object or multiple objects of the same 
class using Jackson's `ObjectMapper`

    * Single data object
     
    ```java
    CloseableHttpAsyncClient client = HttpAsyncClients.createSystem();
    
    client.start();
    
    URI uri = URI.create("http://httpbin.org/get");
    
    JsonFactory factory = new JsonFactory();
    ObjectMapper objectMapper = new ObjectMapper(factory);
    System.out.println("Executing GET " + uri);
    Future<?> future = client.execute(
            AsyncRequestBuilder.get(uri).build(),
            JsonResponseConsumers.create(objectMapper, RequestData.class),
            new FutureCallback<Message<HttpResponse, RequestData>>() {
    
                @Override
                public void completed(Message<HttpResponse, RequestData> message) {
                    System.out.println("Response status: " + message.getHead().getCode());
                    System.out.println(message.getBody());
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
    
    client.shutdown(CloseMode.GRACEFUL);
    ```
    
    Stdout>
    ```
    Executing GET http://httpbin.org/get
    Response status: 200
    RequestData{id=0, url=http://httpbin.org/get, origin='xxx.xxx.xxx.xxx', headers={Connection=close, Host=httpbin.org, User-Agent=Apache-HttpAsyncClient/5.0-beta3 (Java/1.8.0_181)}, args={}}
    ```
    
    * Sequence of data objects of the same class
    
    ```java
    CloseableHttpAsyncClient client = HttpAsyncClients.createSystem();
    
    client.start();
    
    URI uri = URI.create("http://httpbin.org/stream/5");
    
    JsonFactory factory = new JsonFactory();
    ObjectMapper objectMapper = new ObjectMapper(factory);
    System.out.println("Executing GET " + uri);
    Future<?> future = client.execute(
            AsyncRequestBuilder.get(uri).build(),
            JsonResponseConsumers.create(
                    objectMapper,
                    RequestData.class,
                    messageHead -> System.out.println("Response status: " + messageHead.getCode()),
                    requestData -> System.out.println(requestData)),
            new FutureCallback<Long>() {
    
                @Override
                public void completed(Long count) {
                    System.out.println("Objects received: " + count);
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
    
    client.shutdown(CloseMode.GRACEFUL);
    ```
    
    Stdout>
    ```
    Executing GET http://httpbin.org/stream/5
    Response status: 200
    RequestData{id=0, url=http://httpbin.org/stream/5, origin='xxx.xxx.xxx.xxx', headers={Host=httpbin.org, Connection=close, User-Agent=Apache-HttpAsyncClient/5.0-beta3 (Java/1.8.0_181)}, args={}}
    RequestData{id=1, url=http://httpbin.org/stream/5, origin='xxx.xxx.xxx.xxx', headers={Host=httpbin.org, Connection=close, User-Agent=Apache-HttpAsyncClient/5.0-beta3 (Java/1.8.0_181)}, args={}}
    RequestData{id=2, url=http://httpbin.org/stream/5, origin='xxx.xxx.xxx.xxx', headers={Host=httpbin.org, Connection=close, User-Agent=Apache-HttpAsyncClient/5.0-beta3 (Java/1.8.0_181)}, args={}}
    RequestData{id=3, url=http://httpbin.org/stream/5, origin='xxx.xxx.xxx.xxx', headers={Host=httpbin.org, Connection=close, User-Agent=Apache-HttpAsyncClient/5.0-beta3 (Java/1.8.0_181)}, args={}}
    RequestData{id=4, url=http://httpbin.org/stream/5, origin='xxx.xxx.xxx.xxx', headers={Host=httpbin.org, Connection=close, User-Agent=Apache-HttpAsyncClient/5.0-beta3 (Java/1.8.0_181)}, args={}}
    Objects received: 5
    ``` 

2. Request data consumer that converts JSON messages into a `JsonNode` object

    ```java
    CloseableHttpAsyncClient client = HttpAsyncClients.createSystem();
    
    client.start();
    
    URI uri = URI.create("http://httpbin.org/get");
    
    JsonFactory factory = new JsonFactory();
    System.out.println("Executing GET " + uri);
    Future<?> future = client.execute(
            AsyncRequestBuilder.get(uri).build(),
            JsonResponseConsumers.create(factory),
            new FutureCallback<Message<HttpResponse, JsonNode>>() {
    
                @Override
                public void completed(Message<HttpResponse, JsonNode> message) {
                    System.out.println("Response status: " + message.getHead().getCode());
                    System.out.println(message.getBody());
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
    
    client.shutdown(CloseMode.GRACEFUL);
    
    ```
     
    Stdout>
    ```
    Executing GET http://httpbin.org/get
    Shutting down
    Response status: 200
    {"args":{},"headers":{"Connection":"close","Host":"httpbin.org","User-Agent":"Apache-HttpAsyncClient/5.0-beta3 (Java/1.8.0_181)"},"origin":"xxx.xxx.xxx.xxx","url":"http://httpbin.org/get"}
    ``` 

3. Request data consumer that converts JSON messages a sequence of token evens. This is the most efficient way of 
processing JSON content supported by the library. It enables partial consumption of JSON structure without creating
a full representation of incoming messages.

    ```java
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
    
    client.shutdown(CloseMode.GRACEFUL);
    
    ```
    
    Stdout>
    ```
    Response status: 200
    object start/args=object start/object end/headers=object start/Connection="close"/Host="httpbin.org"/User-Agent="Apache-HttpAsyncClient/5.0-beta3 (Java/1.8.0_181)"/object end/origin="xxx.xxx.xxx.xxx"/url="http://httpbin.org/get"/object end/stream end/
    Object received
    ```
