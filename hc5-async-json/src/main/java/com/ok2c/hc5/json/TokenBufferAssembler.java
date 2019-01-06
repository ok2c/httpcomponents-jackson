package com.ok2c.hc5.json;

import java.io.IOException;

import org.apache.hc.core5.util.Args;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.util.TokenBuffer;

/**
 * {@link JsonTokenConsumer} implementation that copies Json token events
 * into {@link TokenBuffer} and passes those buffers to a {@link JsonResultSink}.
 */
public final class TokenBufferAssembler implements JsonTokenConsumer {

    private final JsonResultSink<TokenBuffer> sink;

    private boolean started;
    private int depth;
    private TokenBuffer buffer;

    public TokenBufferAssembler(JsonResultSink<TokenBuffer> sink) {
        Args.notNull(sink, "Result sink");
        this.sink = sink;
        this.buffer = new TokenBuffer(null, false);
    }

    public void accept(int tokenId, JsonParser jsonParser) throws IOException {
        if (!started) {
            started = true;
            sink.begin(-1);
        }
        if (tokenId != JsonTokenId.ID_NO_TOKEN) {
            buffer.copyCurrentEvent(jsonParser);
        }
        switch (tokenId) {
            case JsonTokenId.ID_START_OBJECT:
            case JsonTokenId.ID_START_ARRAY:
                depth++;
                break;
            case JsonTokenId.ID_END_OBJECT:
            case JsonTokenId.ID_END_ARRAY:
                depth--;
                if (depth == 0) {
                    buffer.close();
                    sink.accept(buffer);
                    buffer = new TokenBuffer(null, false);
                }
                break;
            case JsonTokenId.ID_NO_TOKEN:
                if (!buffer.isClosed()) {
                    buffer.close();
                }
                sink.end();
                break;
        }
    }

}
