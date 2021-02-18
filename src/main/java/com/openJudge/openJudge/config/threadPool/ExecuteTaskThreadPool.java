package com.openJudge.openJudge.config.threadPool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @Author aries
 * @Data 2021-02-18
 */
@Configuration
public class ExecuteTaskThreadPool {
    @Bean
    ThreadPoolExecutor myExecuteTaskThreadPool() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 8, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(100), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        return executor;
    }
}
