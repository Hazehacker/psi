package com.zeroone.star.sale.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class ImportReturnThreadPoolExecutor {

    @Bean("importReturnThreadPool")
    public ThreadPoolExecutor labelThreadPool(){
        return new ThreadPoolExecutor(10,
                20,
                5,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(5000),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
