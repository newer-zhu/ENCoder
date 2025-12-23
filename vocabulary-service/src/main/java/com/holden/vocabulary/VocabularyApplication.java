package com.holden.vocabulary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableConfigurationProperties
@SpringBootApplication
@EnableScheduling
@EnableFeignClients
@EntityScan(basePackages = "com.holden.common.entity")
public class VocabularyApplication {

    public static void main(String[] args) {
        SpringApplication.run(VocabularyApplication.class, args);
    }

}
