package com.kanga.kickrushapi;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestClockConfig {

    @Bean
    @Primary
    public Clock testClock() {
        return Clock.fixed(Instant.parse("2026-02-08T01:15:00Z"), ZoneId.of("Asia/Seoul"));
    }
}
