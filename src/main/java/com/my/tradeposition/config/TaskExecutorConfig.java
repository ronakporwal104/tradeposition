package com.my.tradeposition.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class TaskExecutorConfig {

    @Bean
    public ExecutorService executorService() {
        return Executors.newSingleThreadExecutor();
    }
}
