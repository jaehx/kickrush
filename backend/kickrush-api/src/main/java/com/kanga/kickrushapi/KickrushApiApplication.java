package com.kanga.kickrushapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.Bean;

import java.time.Clock;

@SpringBootApplication(scanBasePackages = {"com.kanga.kickrushapi", "com.kanga.kickrush"})
@EntityScan(basePackages = "com.kanga.kickrush.domain")
@EnableJpaRepositories(basePackages = "com.kanga.kickrush.domain")
public class KickrushApiApplication {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    public static void main(String[] args) {
        SpringApplication.run(KickrushApiApplication.class, args);
    }

}
