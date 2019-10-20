package com.warmup.familytalk.web;

import java.util.Collections;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import com.warmup.familytalk.common.TimeWatch;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import static com.warmup.familytalk.common.LogHelper.CONTEXT_KEY;
import static com.warmup.familytalk.common.LogHelper.logOnError;
import static com.warmup.familytalk.common.LogHelper.logOnNext;

@Slf4j
@Component
public class LogFilter implements WebFilter {
    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final WebFilterChain chain) {
        final String requestId = exchange.getRequest().getId();
        final TimeWatch timeWatch = TimeWatch.createStarted(requestId);

        exchange.getResponse()
                .getHeaders()
                .put("x-debug-id", Collections.singletonList(timeWatch.getDebugId()));
        exchange.getAttributes()
                .put(CONTEXT_KEY, timeWatch);

        return Mono.empty()
                   .doOnEach(logOnNext(o -> log.info("request header:[{}]", exchange.getRequest().getHeaders())))
                   .then(chain.filter(exchange)
                              .doOnEach(logOnNext(o -> log.info("done")))
                              .doOnEach(logOnError(o -> log.error("error done"))));
    }
}
