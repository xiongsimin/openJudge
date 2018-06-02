package com.openJudge.openJudge.controller.supAdmin.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class Login {
	@RequestMapping("/sup")
	public String goLogin(){
		return "supAdmin/login";
	}
	@RequestMapping(value="/sup/index",method=RequestMethod.POST)
	public String goIndex(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		String id=request.getParameter("id");
		String name=request.getParameter("name");
		map.addAttribute("id", id);
		map.addAttribute("name", name);
		if(id.equals("1")&&name.equals("xiongsimin")){
			return "supAdmin/index";
		}
		else{
			return "supAdmin/login";
		}
	}
}
