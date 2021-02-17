package com.openJudge.openJudge.controller;

import com.openJudge.openJudge.consist.BaseConstant;
import com.openJudge.openJudge.entity.JudgeRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author aries
 * @Data 2021-02-15
 */
@RestController
@RequestMapping(value = "/test")
public class TestController {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("sendMsg")
    String sendMsg() {
        rabbitTemplate.convertAndSend("JavaCompilerTaskExchange", "JavaCompilerRouting", new JudgeRequest("java", BaseConstant.WHICH_STAGE_COMPILE, "test1234567890"));
        System.out.println("123");
        return "ok";
    }
}
