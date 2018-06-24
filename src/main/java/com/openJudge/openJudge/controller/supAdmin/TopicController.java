package com.openJudge.openJudge.controller.supAdmin;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.openJudge.openJudge.entity.Competition;
import com.openJudge.openJudge.entity.SupAdmin;
import com.openJudge.openJudge.entity.Topic;
import com.openJudge.openJudge.myPlugins.PageData;
import com.openJudge.openJudge.repository.CompetitionRepository;
import com.openJudge.openJudge.repository.SupAdminRepository;
import com.openJudge.openJudge.repository.TopicRepository;

@Controller
public class TopicController {

	@Autowired
	TopicRepository topicRepository;
	@Autowired
	SupAdminRepository supAdminrepository;
	@Autowired
	CompetitionRepository competitionRepository;

	/*
	 * 题目列表页
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/sup_topic.html", method = RequestMethod.GET)
	public String goSupTopic(ModelMap map) {
		List<Topic> topics = (List<Topic>) topicRepository.findAll();
		List listTopics=new ArrayList();
		for (Topic t : topics) {
			Map m=new HashMap();
			SupAdmin supAdmin=new SupAdmin();
			Competition competition=new Competition();
			m.put("id", t.getId());
			m.put("title", t.getTitle());
			m.put("type", t.getType());
			m.put("handler_id", t.getHandler_id());
			m.put("handler_character", t.getHandler_character());
			m.put("time", t.getTime());
			m.put("competition_id", t.getCompetition_id());
			m.put("try_people", t.getTry_people());
			m.put("pass_people", t.getPass_people());
			if(t.getTry_people()==0){
				m.put("throughput_rate", "0%");
			}else{
				m.put("throughput_rate", (t.getPass_people()/t.getTry_people())/100+"%");
			}
			if(t.getHandler_character()==0){//超级管理员
				supAdmin=supAdminrepository.findSupAdminById(t.getHandler_id());
			}else{
				/*
				 * 添加管理员情况下的代码
				 * adminrepository.findAdminById(t.getHandler_id());
				 */
			}
			m.put("handler_name", supAdmin.getName());//处理者名字
			competition=competitionRepository.findCompetitionById(t.getCompetition_id());
			m.put("competition_title", competition.getTitle());//所属比赛或练习的标题
			m.put("number", t.getNumber());//题目在比赛中的题号
			listTopics.add(m);
		}
		map.addAttribute("listTopics", listTopics);
		return "supAdmin/sup_topic";
	}

	/*
	 * 新增题目页
	 */
	@RequestMapping(value = "/sup_new_topic.html", method = RequestMethod.GET)
	public String goSupNewTopic(HttpServletRequest request,ModelMap map) {
		List<Competition> competitionsOrPractices=new ArrayList<Competition>();
		HttpSession session=request.getSession();
		int handler_character=Integer.parseInt((String)session.getAttribute("character"));
//		Long handler_id=Long.parseLong((String)session.getAttribute("id"));
		if(handler_character==0){
			//超级管理员可以新建所有类型题
			competitionsOrPractices=(List<Competition>)competitionRepository.findAll();
		}else{
			//普通管理员只能新建练习题 
			competitionsOrPractices=(List<Competition>)competitionRepository.findAllPractice();
		}
		map.addAttribute("competitionsOrPractices", competitionsOrPractices);
		return "supAdmin/sup_new_topic";
	}

	/*
	 * 保存新增题目信息
	 */
	@RequestMapping(value = "/sup_add_topic", method = RequestMethod.POST)
	public String addTopic(HttpServletRequest request, HttpServletResponse response, ModelMap map,@RequestParam("test_data") MultipartFile test_data_file) {
		PageData pd = new PageData(request);
		map = pd.getModelMap();
		Topic topic = new Topic();
		Date date = new Date();
		Timestamp time = new Timestamp(date.getTime());
		/*上传压缩文件并解压到指定目录*/
		
		/*上传压缩文件并解压到指定目录*/
		String test_data_path="";//待修改
		topic.setCompetition_id(Long.parseLong((String) map.get("competition_id")));
		topic.setContent((String) map.get("content"));
		topic.setInfo((String) map.get("info"));
		topic.setInput_intro((String) map.get("input_intro"));
		topic.setInput_sample((String) map.get("input_sample"));
		topic.setMemory_limit(Integer.parseInt((String) map.get("memory_limit")));
		topic.setNumber(Integer.parseInt((String) map.get("number")));
		topic.setOutput_intro((String) map.get("output_intro"));
		topic.setOutput_sample((String) map.get("output_sample"));
		topic.setTest_data_path(test_data_path);
//		topic.setTest_data_path((String) map.get("test_data_path"));
		topic.setTime(time);
		topic.setTime_limit(Integer.parseInt((String) map.get("time_limit")));
		topic.setTitle((String) map.get("title"));
		topic.setType((String) map.get("type"));
		HttpSession session = request.getSession();
		String character = (String) session.getAttribute("character");// 从session中获取登陆者的身份
		String id = (String) session.getAttribute("id");// 从session中获取登陆者的id
		if (character.equals("0")) {
			topic.setHandler_character(0);
			topic.setHandler_id(Long.parseLong(id));
		} else {
			topic.setHandler_character(1);
			topic.setHandler_id(Long.parseLong(id));
		}
		topicRepository.save(topic);
		return "success";
	}
	/*
	 * 查看题目详情页
	 */
	@RequestMapping(value="/sup_detail_topic.html",method=RequestMethod.GET)
	public String goSupTopicDetail(HttpServletRequest request,ModelMap map){
		Long topic_id=Long.parseLong(request.getParameter("id"));
		Topic topic=topicRepository.findTopicById(topic_id);
		map.addAttribute("topic", topic);
		return "supAdmin/sup_detail_topic";
	}
	/*
	 * 编辑题目页
	 */
	@RequestMapping(value="/sup_edit_topic.html",method=RequestMethod.GET)
	public String goSupEditTopic(HttpServletRequest request,ModelMap map){
		Long id=Long.parseLong((String)request.getParameter("id"));
		List<Competition> competitionsOrPractices=new ArrayList<Competition>();
		Topic topic=topicRepository.findTopicById(id);
		HttpSession session=request.getSession();
		int handler_character=Integer.parseInt((String)session.getAttribute("character"));
		if(handler_character==0){
			//超级管理员可以新建所有类型题
			competitionsOrPractices=(List<Competition>)competitionRepository.findAll();
		}else{
			//普通管理员只能新建练习题 
			competitionsOrPractices=(List<Competition>)competitionRepository.findAllPractice();
		}
		map.addAttribute("topic", topic);
		map.addAttribute("competitionsOrPractices", competitionsOrPractices);
		return "supAdmin/sup_edit_topic";
	}
	/*
	 * 保存编辑内容
	 */
	@RequestMapping(value="/sup_edit_topic",method=RequestMethod.POST)
	public String editTopic(HttpServletRequest request,HttpServletResponse respone,ModelMap map){
		Long id=Long.parseLong((String)request.getParameter("id"));
		Topic topic=topicRepository.findTopicById(id);
		topic.setCompetition_id(Long.parseLong(request.getParameter("competition_id")));
		topic.setContent(request.getParameter("content"));
		topic.setInfo(request.getParameter("info"));
		topic.setInput_intro(request.getParameter("input_intro"));
		topic.setInput_sample(request.getParameter("input_sample"));
		topic.setMemory_limit(Integer.parseInt(request.getParameter("memory_limit")));
		topic.setNumber(Integer.parseInt(request.getParameter("number")));
		topic.setOutput_intro(request.getParameter("output_intro"));
		topic.setOutput_sample(request.getParameter("output_sample"));
		topic.setTime_limit(Integer.parseInt(request.getParameter("time_limit")));
		topic.setTitle(request.getParameter("title"));
		topic.setType(request.getParameter("type"));
		String change_test_data=request.getParameter("chang_test_data");//从获取测试数据是否更改的信息
		if(change_test_data!=null&&change_test_data.equals("1")){
			/*
			 * 添加上传新测试数据的代码  
			 */
		}
		topicRepository.save(topic);
		return "success";
	}
	/*
	 * 删除
	 */
	@RequestMapping(value="/sup_delete_topic",method=RequestMethod.GET)
	public void delectTopic(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		Long id=Long.parseLong((String)request.getParameter("id"));
		topicRepository.deleteById(id);
		try {
			response.sendRedirect("/sup_topic.html");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
