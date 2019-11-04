package com.warmup.familytalk.web;

import org.springframework.core.MethodParameter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;
import org.springframework.web.server.ServerWebExchange;
import com.warmup.familytalk.auth.model.JwtToken;
import com.warmup.familytalk.auth.model.User;
import com.warmup.familytalk.auth.service.JwtService;
import com.warmup.familytalk.auth.service.UserService;
import com.warmup.familytalk.bot.service.NewsGeneratorService;
import com.warmup.familytalk.common.LoggingUtils;
import com.warmup.familytalk.common.Trace;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class WebFluxConfiguration implements WebFluxConfigurer {
    private final JwtService jwtService;
    private final UserService userService;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*");
    }

    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addConverter(new NewsCountryEnumConverter());
        registry.addConverter(new NewsCategoryEnumConverter());
    }

    @Override
    public void configureArgumentResolvers(final ArgumentResolverConfigurer configurer) {
        configurer.addCustomResolver(new LoggableArgumentResolver());
        configurer.addCustomResolver(new UserArgumentResolver(jwtService, userService));
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

    @RequiredArgsConstructor
    private static final class UserArgumentResolver implements HandlerMethodArgumentResolver {
        private final JwtService jwtService;
        private final UserService userService;

        @Override
        public boolean supportsParameter(final MethodParameter parameter) {
            return User.class.isAssignableFrom(parameter.getParameterType());
        }

        @Override
        public Mono<Object> resolveArgument(final MethodParameter parameter,
                                            final BindingContext bindingContext,
                                            final ServerWebExchange exchange) {
            final String authorization = exchange.getRequest().getHeaders().get("Authorization").get(0).substring(7);
            return jwtService.parse(authorization)
                             .map(JwtToken::getUserId)
                             .flatMap(userService::findByUserId);
        }
    }

    private static class NewsCategoryEnumConverter implements Converter<String, NewsGeneratorService.Category> {
        @Override
        public NewsGeneratorService.Category convert(String source) {
            return NewsGeneratorService.Category.valueOf(source.toUpperCase());
        }
    }

    private static class NewsCountryEnumConverter implements Converter<String, NewsGeneratorService.Country> {
        @Override
        public NewsGeneratorService.Country convert(String source) {
            return NewsGeneratorService.Country.valueOf(source.toUpperCase());
        }
    }
}