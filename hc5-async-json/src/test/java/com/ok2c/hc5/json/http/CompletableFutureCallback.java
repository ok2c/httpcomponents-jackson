package com.ok2c.hc5.json.http;

import java.util.concurrent.CompletableFuture;

import org.apache.hc.core5.concurrent.FutureCallback;

/**
 * Connects a {@link FutureCallback} to a {@link CompletableFuture}
 *
 * @param <T> The result type
 */
public class CompletableFutureCallback<T> implements FutureCallback<T> {

    private final CompletableFuture<T> future;

    public CompletableFutureCallback() {
        this(new CompletableFuture<>());
    }

    public CompletableFutureCallback(CompletableFuture<T> future) {
        this.future = future;
    }

    public CompletableFuture<T> getFuture() {
        return future;
    }

    @Override
    public void completed(T result) {
        future.complete(result);
    }

    @Override
    public void failed(Exception ex) {
        future.completeExceptionally(ex);
    }

    @Override
    public void cancelled() {
        future.cancel(false);
    }
}
