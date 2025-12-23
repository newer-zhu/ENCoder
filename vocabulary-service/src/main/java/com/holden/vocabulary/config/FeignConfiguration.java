package com.holden.vocabulary.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author: Hodor_Zhu
 * @description
 * @date: 2025/12/19 22:55
 */
@Configuration
public class FeignConfiguration {

    @Bean
    public OkHttpClient feignOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(
                        5,          // 最大连接数
                        5, TimeUnit.MINUTES
                ))
                .build();
    }

}
