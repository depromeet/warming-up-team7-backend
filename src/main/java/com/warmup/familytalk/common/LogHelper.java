package com.warmup.familytalk.common;

import java.util.function.Consumer;

import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import reactor.core.publisher.Signal;
import reactor.util.context.Context;

@Slf4j
public final class LogHelper {
    public static final String CONTEXT_KEY = "LOGGABLE_TIME_WATCH";

    public static Context createLogContext(final TimeWatch timeWatch) {
        return Context.of(CONTEXT_KEY, timeWatch);
    }

    public static <T> Consumer<Signal<T>> logOnNext(final Consumer<T> logStatement) {
        return signal -> {
            if (!(signal.isOnNext() || signal.isOnComplete())) return;

            final TimeWatch runningTimeWatch = signal.getContext()
                                                     .get(ServerWebExchange.class)
                                                     .getRequiredAttribute(CONTEXT_KEY);
            try (MDC.MDCCloseable closeable = MDC.putCloseable(CONTEXT_KEY, runningTimeWatch.toString())) {
                logStatement.accept(signal.get());
            }
        };
    }

    public static <T> Consumer<Signal<T>> logOnError(final Consumer<Throwable> logStatement) {
        return signal -> {
            if (!signal.isOnError()) return;

            final TimeWatch runningTimeWatch = signal.getContext()
                                                     .get(ServerWebExchange.class)
                                                     .getRequiredAttribute(CONTEXT_KEY);
            try (MDC.MDCCloseable closeable = MDC.putCloseable(CONTEXT_KEY, runningTimeWatch.toString())) {
                logStatement.accept(signal.getThrowable());
            }
        };
    }

    public static String dumpRequest(final ServerHttpRequest request) {
        return request.toString();
    }

    public static String dumpResponse(final ServerHttpResponse response) {
        return response.toString();
    }
}
