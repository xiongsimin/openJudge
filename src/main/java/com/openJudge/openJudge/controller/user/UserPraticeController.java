package com.openJudge.openJudge.controller.user;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.openJudge.openJudge.entity.Competition;
import com.openJudge.openJudge.entity.Topic;
import com.openJudge.openJudge.entity.User;
import com.openJudge.openJudge.repository.CompetitionRepository;

@Controller
public class UserPraticeController {
	@Autowired
	CompetitionRepository practiceRepository;
	@GetMapping("/practice.html")
	public String goPractice(HttpServletRequest request,ModelMap map){
		HttpSession session=request.getSession();
		User user=(User)session.getAttribute("user");
		List<Competition> practices=practiceRepository.findPublishedPractice();
		for(Competition p:practices){
			List<Topic> ts=p.getTopics();
			for (int i = 0; i < ts.size(); i++) {
				String tryPeople = ts.get(i).getTry_people();
				String passPeople = ts.get(i).getPass_people();
				int tryPeopleNumber = 0;// 用于记录try_people字段中“,”的个数,即尝试人数个数
				int passPeopleNumber = 0;// 用于记录pass_people字段中“,”的个数,即尝试人数个数
				if (tryPeople != null && tryPeople.trim() != "") {
					for (int j = 0; j < tryPeople.length(); j++) {
						if (tryPeople.charAt(j) == ',') {
							tryPeopleNumber++;
						}
					}
				}
				if (passPeople != null && passPeople.trim() != "") {
					for (int j = 0; j < passPeople.length(); j++) {
						if (passPeople.charAt(j) == ',') {
							passPeopleNumber++;
						}
					}
				}
				ts.get(i).setTry_people_number(tryPeopleNumber);
				ts.get(i).setPass_people_number(passPeopleNumber);
				DecimalFormat df=new DecimalFormat("#.00");
				if (passPeopleNumber == 0) {
					ts.get(i).setRate("0%");
				} else {
					ts.get(i).setRate(df.format(((float)passPeopleNumber / (float)tryPeopleNumber) * 100) + "%");
				}
			}
			p.setTopics(ts);
		}
		/*List jsons=new ArrayList();
		for(Competition p:practices){
			Map m=new HashMap();
			m.put("title","text:"+p.getTitle());
			m.put("xAxis","categories:"+p.getTitle());
			List xAxis=new ArrayList();
			for()
		}*/
		map.addAttribute("practices", practices);
		map.addAttribute("practiceSize", practices.size());
		map.addAttribute("user", user);
		return "user/practice";
	}
}
