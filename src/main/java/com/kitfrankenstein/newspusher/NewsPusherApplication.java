package com.kitfrankenstein.newspusher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NewsPusherApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsPusherApplication.class, args);
    }

}
