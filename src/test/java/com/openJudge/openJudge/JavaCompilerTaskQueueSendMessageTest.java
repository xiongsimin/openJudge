package com.openJudge.openJudge;

import com.openJudge.openJudge.consist.BaseConstant;
import com.openJudge.openJudge.entity.JudgeRequest;
import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author aries
 * @Data 2021-02-15
 */
public class JavaCompilerTaskQueueSendMessageTest {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void sendMsg() {
        rabbitTemplate.convertAndSend("JavaCompilerTaskExchange", "JavaCompilerRouting", new JudgeRequest("java", BaseConstant.WHICH_STAGE_COMPILE, "test1234567890"));
        System.out.println("123");
    }
}
