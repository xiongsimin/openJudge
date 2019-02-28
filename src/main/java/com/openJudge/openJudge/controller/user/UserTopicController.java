package com.openJudge.openJudge.controller.user;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.openJudge.openJudge.entity.Record;
import com.openJudge.openJudge.entity.Topic;
import com.openJudge.openJudge.entity.User;
import com.openJudge.openJudge.myTools.Judge;
import com.openJudge.openJudge.repository.RecordRepository;
import com.openJudge.openJudge.repository.TopicRepository;

@Controller
public class UserTopicController {
	@Autowired
	TopicRepository topicRepository;
	@Autowired
	RecordRepository recordRepository;
	
	@GetMapping("/topic.html")
	public String goUserTopic(HttpServletRequest request,ModelMap map) {
		List<Topic> topics = topicRepository.findTopicsOfPublishedPractice();
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
		HttpSession session=request.getSession();
		User user=(User)session.getAttribute("user");
		map.addAttribute("topics", topics);
		map.addAttribute("user", user);
		return "user/topic";
	}
	@GetMapping("/detail_topic.html")
	public String goTopicDetail(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		Topic topic=topicRepository.findTopicById(Long.parseLong(request.getParameter("id")));
		HttpSession session=request.getSession();
		User user=(User)session.getAttribute("user");
		map.addAttribute("user", user);
		map.addAttribute("topic", topic);
		return "user/detail_topic";
	}
	@GetMapping("/submit_code")
	@ResponseBody
	public Map<String,String> judgeCode(HttpServletRequest request) throws IOException{
		HttpSession session=request.getSession();
		Long userId=((User)session.getAttribute("user")).getId();
		Long topicId=Long.parseLong(request.getParameter("topic_id"));
		String code=request.getParameter("code");
		String type=request.getParameter("type");//
		Date date=new Date();
		Timestamp submitTime=new Timestamp(date.getTime());
		Map<String,String> map=new HashMap<String,String>();
		Topic topic=new Topic();
		topic=topicRepository.findTopicById(topicId);
		System.out.println(topic.getTest_data_path());
		map=Judge.doJudge(code,type,topic.getTime_limit(),topic.getMemory_limit(),topic.getTest_data_path(),userId);
		//保存提交记录
		String tryPeople=topic.getTry_people();
		if(tryPeople==null||tryPeople.equals("")){
			topic.setTry_people(userId+",");
		}else{
			int i=0;
			while(tryPeople.length()>0){
				String str=tryPeople.substring(0,tryPeople.indexOf(","));
				if(userId==Long.parseLong(str)){
					i=1;
					break;
				}else{
					tryPeople=tryPeople.substring(tryPeople.indexOf(",")+1, tryPeople.length());
				}
			}
			if(i==0){//tryPeople字段里没有该提交人的id
				topic.setTry_people(topic.getTry_people()+userId+",");
			}
		}
		if(map.get("state")!=null&&map.get("state").equals("Accept")){
			String passPeople=topic.getPass_people();
			if(passPeople==null||passPeople.equals("")){
				topic.setPass_people(userId+",");
			}else{
				int i=0;
				while(passPeople.length()>0){
					String str=passPeople.substring(0,passPeople.indexOf(","));
					if(userId==Long.parseLong(str)){
						i=1;
						break;
					}else{
						passPeople=passPeople.substring(passPeople.indexOf(",")+1, passPeople.length());
					}
				}
				if(i==0){//passPeople字段里没有该提交人的id
					topic.setPass_people(topic.getPass_people()+userId+",");
				}
			}
		}
		topicRepository.save(topic);
		Record record=new Record();
		record.setUser_id(userId);
		record.setState(map.get("state"));
		record.setTime(Integer.parseInt(map.get("time")));
		record.setErrorMsg(map.get("errorMsg"));
		record.setMemory(Integer.parseInt(map.get("memory")));
		record.setSubmit_time(submitTime);
		record.setTopic(topic);
		recordRepository.save(record);
		return map;
	}
}
