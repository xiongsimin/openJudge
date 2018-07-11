package com.openJudge.openJudge.controller.user;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.openJudge.openJudge.entity.Topic;
import com.openJudge.openJudge.myTools.Judge;
import com.openJudge.openJudge.repository.TopicRepository;

@Controller
public class UserTopicController {
	@Autowired
	TopicRepository topicRepository;

	@GetMapping("/topic.html")
	public String goUserTopic(ModelMap map) {
		List<Topic> topics = topicRepository.findTopicOfPractice();
		// 数据库表topic中没有存储通过人数、尝试人数、通过率，而是将他们的id通过字符串的形式储存，因此，需要做一下处理，将他们赋给实体的对应成员变量
		for (int i = 0; i < topics.size(); i++) {
			String tryPeople = topics.get(i).getTry_people();
			String passPeople = topics.get(i).getPass_people();
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
			topics.get(i).setTry_people_number(tryPeopleNumber);
			topics.get(i).setPass_people_number(passPeopleNumber);
			DecimalFormat df = new DecimalFormat("#.00");
			if (passPeopleNumber == 0) {
				topics.get(i).setRate("0%");
			} else {
				topics.get(i).setRate(df.format(((float) passPeopleNumber / (float) tryPeopleNumber) * 100) + "%");
			}
		}
		map.addAttribute("topics", topics);
		return "user/topic";
	}
	@GetMapping("/detail_topic.html")
	public String goTopicDetail(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		Topic topic=topicRepository.findTopicById(Long.parseLong(request.getParameter("id")));
		map.addAttribute("topic", topic);
		return "user/detail_topic";
	}
	@GetMapping("/submit_code")
	@ResponseBody
	public Map<String,String> judgeCode(HttpServletRequest request) throws IOException{
		Long topicId=Long.parseLong(request.getParameter("topic_id"));
		String code=request.getParameter("code");
		String type=request.getParameter("type");
		Map<String,String> map=new HashMap<String,String>();
		Topic topic=new Topic();
		topic=topicRepository.findTopicById(topicId);
		System.out.println(topic.getTest_data_path());
		map=Judge.doJudge(code,type,topic.getTime_limit(),topic.getMemory_limit(),topic.getTest_data_path(),1001);
		return map;
	}
}
