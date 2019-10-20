package com.warmup.familytalk.web;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;
import org.springframework.web.server.ServerWebExchange;
import com.warmup.familytalk.common.LoggingUtils;
import com.warmup.familytalk.common.Trace;
import reactor.core.publisher.Mono;

@Component
public class WebFluxConfiguration implements WebFluxConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*");
    }

    @Override
    public void configureArgumentResolvers(final ArgumentResolverConfigurer configurer) {
        configurer.addCustomResolver(new LoggableArgumentResolver());
    }

    private static final class LoggableArgumentResolver implements HandlerMethodArgumentResolver {
        @Override
        public boolean supportsParameter(final MethodParameter parameter) {
            return Trace.class.isAssignableFrom(parameter.getParameterType());
        }

        @Override
        public Mono<Object> resolveArgument(final MethodParameter parameter,
                                            final BindingContext bindingContext,
                                            final ServerWebExchange exchange) {
            return Mono.just(exchange.getAttributes().get(LoggingUtils.CONTEXT_KEY));
        }
    }
}