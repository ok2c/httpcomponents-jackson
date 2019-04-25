# Event-driven, asynchronous JSON message processors for Apache HttpComponents 5.0

## Project scope

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

## Documentation

Detailed [project documentation](https://ok2c.github.io/httpcomponents-jackson) can be found at
https://ok2c.github.io/httpcomponents-jackson
