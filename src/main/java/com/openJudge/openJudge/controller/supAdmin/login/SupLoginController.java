package com.openJudge.openJudge.controller.supAdmin.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.openJudge.openJudge.domain.entity.SupAdmin;
import com.openJudge.openJudge.domain.repository.SupAdminRepository;

@Controller
public class SupLoginController {
	
	@Autowired
	SupAdminRepository supAdminRepository;
	
	@RequestMapping("/sup")
	public String goLogin(){
		return "supAdmin/login";
	}
	@RequestMapping(value="/sup/index",method=RequestMethod.POST)
	public String goIndex(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		String id=request.getParameter("id");
		String psw=request.getParameter("psw");
		SupAdmin supAdmin=supAdminRepository.findSupAdminById(Long.parseLong(id));
		if(supAdmin.getPsw().equals(psw)){
			String last_login=supAdmin.getLast_login().toString();
			map.addAttribute(supAdmin);
			map.addAttribute("last_login", last_login);
			return "supAdmin/index";
		}
		else{
			return "supAdmin/login";
		}
	}
}
