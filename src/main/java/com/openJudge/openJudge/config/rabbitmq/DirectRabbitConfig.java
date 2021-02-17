package com.openJudge.openJudge.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

/**
 * @Author aries
 * @Data 2021-02-15
 */
@Configuration
public class DirectRabbitConfig {
    //队列
    @Bean
    public Queue JavaCompilerTaskQueue() {
        return new Queue("JavaCompilerTaskQueue");
    }

    //交换机
    @Bean
    DirectExchange JavaCompilerTaskExchange() {
        return new DirectExchange("JavaCompilerTaskExchange", false, false);
    }
    //绑定
    @Bean
    Binding JavaCompilerTaskBinding(){
        return BindingBuilder
                .bind(JavaCompilerTaskQueue())
                .to(JavaCompilerTaskExchange())
                //设置匹配键
                .with("JavaCompilerRouting");
    }
}
