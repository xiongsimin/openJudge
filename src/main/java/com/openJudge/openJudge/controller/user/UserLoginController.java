package com.openJudge.openJudge.controller.user;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.openJudge.openJudge.entity.User;
import com.openJudge.openJudge.repository.UserRepository;

@Controller
public class UserLoginController {
	@Autowired
	UserRepository userRepository;
	@GetMapping("/user")
	public String goLogin(){
		return "user/login";
	}
	@PostMapping("/user/index")
	public String goIndex(HttpServletRequest request,HttpServletResponse response,ModelMap map) throws IOException{
		String account=request.getParameter("account");
		String password=request.getParameter("password");
		User user=new User();
		user=userRepository.findUserByAccount(account);
		if(user.getPassword().equals(password)){
			HttpSession session=request.getSession();
			session.setAttribute("user", user);
			response.sendRedirect("/topic.html");
		}
		map.put("msg", "账号与密码不匹配！");
		return "user/login";
	}
}
