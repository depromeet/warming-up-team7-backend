package com.warmup.familytalk.web;

import java.util.Collections;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import com.warmup.familytalk.common.Trace;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import static com.warmup.familytalk.common.LoggingUtils.CONTEXT_KEY;
import static com.warmup.familytalk.common.LoggingUtils.logOnError;
import static com.warmup.familytalk.common.LoggingUtils.logOnNext;
import static com.warmup.familytalk.common.LoggingUtils.logging;

@Slf4j
@Component
public class LogFilter implements WebFilter {
    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final WebFilterChain chain) {
        final String requestId = exchange.getRequest().getId();
        final Trace trace = Trace.createStarted(requestId);

        exchange.getResponse()
                .getHeaders()
                .put("x-debug-id", Collections.singletonList(trace.getDebugId()));
        exchange.getAttributes()
                .put(CONTEXT_KEY, trace);

        return Mono.empty()
//                   .doOnEach(logOnNext(o -> log.info("request header:[{}]", exchange.getRequest().getHeaders())))
                   .then(chain.filter(exchange)
                              .doOnEach(logOnNext(o -> log.info("done")))
                              .doOnEach(logOnError(o -> log.error("error done")))
                              .doOnError(e -> logging(trace, () -> log.error("hihihi123123123"))));
    }
}
