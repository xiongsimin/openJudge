package com.openJudge.openJudge.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class userTopicController {
	@GetMapping("/user")
	public String goUserTopic(){
		
		return "user/topic";
	}
}
