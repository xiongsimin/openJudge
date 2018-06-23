package com.openJudge.openJudge.controller.supAdmin;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.openJudge.openJudge.entity.Topic;
import com.openJudge.openJudge.myPlugins.PageData;
import com.openJudge.openJudge.repository.TopicRepository;

@Controller
public class TopicController {
	
	@Autowired
	TopicRepository topicRepository;
	
	/*
	 * 题目列表页
	 */
	@RequestMapping(value="/sup_topic.html",method=RequestMethod.GET)
	public String goSupTopic(ModelMap map){
		List<Topic> topics=(List<Topic>)topicRepository.findAll();
		map.addAttribute("topics", topics);
		return "supAdmin/sup_topic";
	}
	/*
	 * 新增题目页
	 */
	@RequestMapping(value="/sup_new_topic.html",method=RequestMethod.GET)
	public String goSupNewTopic(ModelMap map){
		return "supAdmin/sup_new_topic";
	}
	/*
	 * 保存新增题目信息
	 */
	@RequestMapping(value="/sup_add_topic",method=RequestMethod.POST)
	public String addTopic(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		PageData pd=new PageData(request);
		map=pd.getModelMap();
		Topic topic=new Topic();
		Date date=new Date();
		Timestamp time=new Timestamp(date.getTime());
		topic.setCompetition_id(Long.parseLong((String)map.get("competition_id")));
		topic.setContent((String)map.get("content"));
		topic.setInfo((String)map.get("info"));
		topic.setInput_intro((String)map.get("input_intro"));
		topic.setInput_sample((String)map.get("input_sample"));
		topic.setMemory_limit(Integer.parseInt((String)map.get("memory_limit")));
		topic.setNumber(Integer.parseInt((String)map.get("number")));
		topic.setOutput_intro((String)map.get("output_intro"));
		topic.setOutput_sample((String)map.get("output_sample"));
		topic.setTest_data_path((String)map.get("test_data_path"));
		topic.setTime(time);
		topic.setTime_limit(Integer.parseInt((String)map.get("time_limit")));
		topic.setTitle((String)map.get("title"));
		topic.setType((String)map.get("type"));
		HttpSession session=request.getSession();
		String character=(String)session.getAttribute("character");//从session中获取登陆者的身份
		String id=(String)session.getAttribute("id");//从session中获取登陆者的id
		if(character.equals("0")){
			topic.setHandler_character(0);
			topic.setHandler_id(Long.parseLong(id));
		}else{
			topic.setHandler_character(1);
			topic.setHandler_id(Long.parseLong(id));
		}
		topicRepository.save(topic);
		return "success";
	}
}
