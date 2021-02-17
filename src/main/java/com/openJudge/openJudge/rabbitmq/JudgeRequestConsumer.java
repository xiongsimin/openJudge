package com.openJudge.openJudge.rabbitmq;

import com.openJudge.openJudge.entity.JudgeRequest;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author aries
 * @Data 2021-02-15
 * @Description 评测请求消费者
 */
@Component
@RabbitListener(queues = "JavaCompilerTaskQueue")
public class JudgeRequestConsumer {
    @RabbitHandler
    public void process(JudgeRequest judgeRequest) {
        System.out.println(judgeRequest.toString());
    }
}
