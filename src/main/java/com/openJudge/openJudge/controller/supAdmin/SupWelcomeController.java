package com.openJudge.openJudge.controller.supAdmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SupWelcomeController {
	@RequestMapping(value="/sup_welcome.html",method=RequestMethod.GET)
	public String welcome(){
		return "supAdmin/sup_welcome";
	}
}
