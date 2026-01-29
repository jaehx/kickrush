package com.kanga.kickrushapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.kanga")
public class KickrushApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(KickrushApiApplication.class, args);
    }

}
