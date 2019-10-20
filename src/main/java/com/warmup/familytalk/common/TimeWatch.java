package com.warmup.familytalk.common;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

import org.springframework.util.StopWatch;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeWatch implements Loggable {

    private final String requestId;
    private final StopWatch stopWatch = new StopWatch();
    private final Properties properties = new Properties();

    private final LocalDateTime startedAt = LocalDateTime.now(ZoneOffset.UTC);
    private final String randomKey = RandomStringUtils.randomAlphanumeric(10);

    public static TimeWatch createStarted(String requestId) {
        final TimeWatch timeWatch = new TimeWatch(requestId);
        timeWatch.stopWatch.start();

        return timeWatch;
    }

    public static TimeWatch createWhenHasNoRequestID() {
        return new TimeWatch("NO-REQUEST-ID");
    }

    public String getDebugId() {
        return String.format("%s%s", randomKey, requestId);
    }

    public LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }

    public Properties getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return String.format("[%s|%ss]%s%s",
                             getCurrentLocalDateTime().format(DateTimeFormatter.ofPattern("yyMMdd'T'HH:mm:ss:SSS")),
                             (double) Duration.between(startedAt, getCurrentLocalDateTime())
                                              .get(ChronoUnit.NANOS) / 1_000_000_000,
                             randomKey, requestId);
    }
}
