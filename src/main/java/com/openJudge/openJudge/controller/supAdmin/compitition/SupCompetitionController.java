package com.openJudge.openJudge.controller.supAdmin.compitition;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SupCompetitionController {
	@RequestMapping(value="/sup_competition.html",method=RequestMethod.GET)
	public String supCompetition(){
		return "supAdmin/sup_competition";
	}
}
