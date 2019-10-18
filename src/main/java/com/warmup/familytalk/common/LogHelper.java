package com.warmup.familytalk.common;

import java.util.Optional;
import java.util.function.Consumer;

import org.slf4j.MDC;
import reactor.core.publisher.Signal;
import reactor.util.context.Context;

public class LogHelper {
    public static final String CONTEXT_KEY = "LOGGABLE_TIME_WATCH";

    public static Context createLogContext(TimeWatch timeWatch) {
        return Context.of(CONTEXT_KEY, timeWatch);
    }

    public static <T> Consumer<Signal<T>> logOnNext(Consumer<T> logStatement) {
        return signal -> {
            if (!signal.isOnNext()) return;

            final Optional<TimeWatch> runningTimeWatch = signal.getContext().getOrEmpty(CONTEXT_KEY);
            final TimeWatch timeWatch = runningTimeWatch.orElseGet(TimeWatch::createWhenHasNoRequestID);
            try (MDC.MDCCloseable closeable = MDC.putCloseable(CONTEXT_KEY, timeWatch.toString())) {
                logStatement.accept(signal.get());
            }
        };
    }

    public static <T> Consumer<Signal<T>> logOnError(Consumer<Throwable> logStatement) {
        return signal -> {
            if (!signal.isOnError()) return;

            final Optional<TimeWatch> runningTimeWatch = signal.getContext().getOrEmpty(CONTEXT_KEY);
            final TimeWatch timeWatch = runningTimeWatch.orElseGet(TimeWatch::createWhenHasNoRequestID);
            try (MDC.MDCCloseable closeable = MDC.putCloseable(CONTEXT_KEY, timeWatch.toStringWhenError())) {
                logStatement.accept(signal.getThrowable());
            }
        };
    }
}
