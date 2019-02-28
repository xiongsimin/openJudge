package com.openJudge.openJudge.controller.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.openJudge.openJudge.entity.Record;
import com.openJudge.openJudge.entity.User;
import com.openJudge.openJudge.repository.RecordRepository;

@Controller
public class UserRecordController {
	@Autowired
	RecordRepository recordRepository;
	@GetMapping("/record.html")
	public String goRecord(HttpServletRequest request,ModelMap map){
		HttpSession session=request.getSession();
		User user=(User)session.getAttribute("user");
		List<Record> records=recordRepository.findRecordByUserId(user.getId());
		map.addAttribute("records", records);
		map.addAttribute("user", user);
		return "user/record";
	}
}
