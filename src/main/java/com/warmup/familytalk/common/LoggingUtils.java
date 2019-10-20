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
public final class LoggingUtils {
    public static final String CONTEXT_KEY = "LOGGABLE_TIME_WATCH";

    @FunctionalInterface
    public interface Log {
        void write();
    }

    public static Context createLogContext(final Trace trace) {
        return Context.of(CONTEXT_KEY, trace);
    }

    public static void logging(final Trace trace, final Log logStatement) {
        try (MDC.MDCCloseable closeable = MDC.putCloseable(CONTEXT_KEY, trace.toString())) {
            logStatement.write();
        }
    }

    public static void dumpThrowable(final Trace trace, final Throwable e) {
        logging(trace, () -> log.error("{}", e.toString()));
        logging(trace, () -> log.error("{}", formatThrowable(trace, e)));
    }

    public static String formatThrowable(final Trace trace, final Throwable e) {
        final StackTraceElement[] stack = e.getStackTrace();
        final StringBuilder sb = new StringBuilder(256);
        for (StackTraceElement s : stack) {
            sb.append("@@@@@ ")
              .append(trace.getDebugId())
              .append("> ")
              .append(s.toString())
              .append("\n");
        }
        return sb.toString();
    }

    public static <T> Consumer<Signal<T>> logOnNext(final Consumer<T> logStatement) {
        return signal -> {
            if (!(signal.isOnNext() || signal.isOnComplete())) return;

            final Trace runningTrace = signal.getContext()
                                             .get(ServerWebExchange.class)
                                             .getRequiredAttribute(CONTEXT_KEY);
            try (MDC.MDCCloseable closeable = MDC.putCloseable(CONTEXT_KEY, runningTrace.toString())) {
                logStatement.accept(signal.get());
            }
        };
    }

    public static <T> Consumer<Signal<T>> logOnError(final Consumer<Throwable> logStatement) {
        return signal -> {
            if (!signal.isOnError()) return;

            final Trace runningTrace = signal.getContext()
                                             .get(ServerWebExchange.class)
                                             .getRequiredAttribute(CONTEXT_KEY);
            try (MDC.MDCCloseable closeable = MDC.putCloseable(CONTEXT_KEY, runningTrace.toString())) {
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
