package com.openJudge.openJudge.controller.supAdmin.compitition;

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

import com.openJudge.openJudge.domain.entity.Competition;
import com.openJudge.openJudge.domain.entity.SupAdmin;
import com.openJudge.openJudge.domain.repository.CompetitionRepository;
import com.openJudge.openJudge.domain.repository.SupAdminRepository;
import com.openJudge.openJudge.pageData.PageData;
@Controller
public class SupCompetitionController {
	
	@Autowired
	CompetitionRepository competitionRepository;
	@Autowired
	SupAdminRepository supAdminRepository;
	/*
	 * 	比赛列表页
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/sup_competition.html",method=RequestMethod.GET)
	public String goSupCompetition(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		String state=request.getParameter("state");
		List<Competition> competitions=new ArrayList<Competition>();//用来存储比赛信息
//		List<String> handler_names=new ArrayList<String>();//用来存储比赛对应的处理人昵称
		Date date=new Date();//获取系统当前时间
		Timestamp time=new Timestamp(date.getTime());//转化成Timestamp格式
		if(state==null||state.equals("0")){//查询未发布比赛
			state="0";
			competitions=competitionRepository.findDisPublishCompetition();
		}else if(state.equals("1")){//查询未开始比赛
			competitions=competitionRepository.findDisStartCompetition(time);
		}else if(state.equals("2")){//查询正在进行的比赛
			competitions=competitionRepository.findBeginningCompetition(time);
		}else if(state.equals("3")){//查询已结束比赛
			competitions=competitionRepository.findFinishCompetition(time);
		}
		List competitionList=new ArrayList();
		for(Competition c:competitions){
			Map m=new HashMap();
			try {
				m.put("id", c.getId());
				m.put("title", c.getTitle());
				m.put("time", c.getTime());
				m.put("start_time", c.getStart_time());
				m.put("end_time", c.getEnd_time());
				m.put("state", c.getState());//该state是competition.state，意义是比赛的发布与否
				int handler_character=c.getHandler_character();
				if(handler_character==0){//如果是超级管理员
					Long id=c.getHandler_id();
					SupAdmin supAId=supAdminRepository.findSupAdminById(id);
					String name=supAId.getName();
					m.put("handler_name", name);
				}else if(handler_character==1){//如果是管理员
					/*
					 * 
					 * 此处添加管理员情况的处理过程
					 * 
					 */
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			competitionList.add(m);
		}
		map.addAttribute("competitions", competitionList);
//		map.addAttribute("handler_names", handler_names);
		map.addAttribute("state", state);//该state是标记提交请求前所在的页面显示的是哪种状态的比赛 0未发布，1未开始，2进行中，3已结束
		return "supAdmin/sup_competition";
	}
	/*
	 * 新增比赛页
	 */
	@RequestMapping(value="/sup_new_competition.html",method=RequestMethod.GET)
	public String goSupNewCompetition(){
		return "supAdmin/sup_new_competition";
	}
	/*
	 * 保存新增的比赛信息
	 */
	@RequestMapping(value="/sup_add_competition",method=RequestMethod.POST)
	public String addCompetition(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		PageData pd=new PageData(request);
		map=pd.getModelMap();
		Competition competition=new Competition();
		Timestamp start_time=Timestamp.valueOf((String)map.get("start_time"));
		Timestamp end_time=Timestamp.valueOf((String)map.get("end_time"));
		Date date=new Date();
		Timestamp time=new Timestamp(date.getTime());
		HttpSession session=request.getSession();
		String character=(String)session.getAttribute("character");//从session中获取登陆者的身份
		String id=(String)session.getAttribute("id");//从session中获取登陆者的id
		if(character.equals("0")){
			competition.setHandler_character(0);
			competition.setHandler_id(Long.parseLong(id));
		}else{
			competition.setHandler_character(1);
			competition.setHandler_id(Long.parseLong(id));
		}
		competition.setTitle((String)map.get("title"));
		competition.setStart_time(start_time);
		competition.setEnd_time(end_time);
		competition.setTime(time);
		competition.setState(0);
		competition.setType(0);
		competitionRepository.save(competition);
		return "success";
	}
	/*
	 * 编辑比赛页
	 */
	@RequestMapping(value="/sup_edit_competition.html",method=RequestMethod.GET)
	public String goSupEditCompetition(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		String id=request.getParameter("id");
		Competition competition=competitionRepository.findCompetitionById(Long.parseLong(id));
		map.addAttribute("competition", competition);
		return "supAdmin/sup_edit_competition";
	}
	/*
	 * 保存编辑后的比赛内容
	 */
	@RequestMapping(value="/sup_edit_competition",method=RequestMethod.POST)
	public String editCompetition (HttpServletRequest request,HttpServletResponse response,ModelMap map){
		PageData pd=new PageData(request);
		Competition competition=new Competition();;
		map=pd.getModelMap();
//		HttpSession session=request.getSession();
//		int character=(int)session.getAttribute("character");//从session中获取登陆者的身份
//		Long id=(Long)session.getAttribute("id");//从session中获取登陆者的id
		competition=competitionRepository.findCompetitionById(Long.parseLong((String)map.get("id")));
		competition.setTitle((String)map.get("title"));
		competition.setStart_time(Timestamp.valueOf((String)map.get("start_time")));
		competition.setEnd_time(Timestamp.valueOf((String)map.get("end_time")));
		competitionRepository.save(competition);
		return "success";
		
	}
	/*
	 * 删除比赛
	 */
	@RequestMapping(value="/sup_delete_competition",method=RequestMethod.GET)
	public void deleteCompetition(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		PageData pd=new PageData(request);
		map=pd.getModelMap();
		Long id=Long.parseLong((String)map.get("id"));
		competitionRepository.deleteById(id);
		//回到列表页
		try {
			response.sendRedirect("/sup_competition.html?state="+(String)map.get("state"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * 发布比赛
	 */
	@RequestMapping(value="/sup_publish_competition",method=RequestMethod.GET)
	public void publishCompetition(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		PageData pd=new PageData(request);
		map=pd.getModelMap();
		Long id=Long.parseLong((String)map.get("id"));
		Competition competition=new Competition();
		competition=competitionRepository.findCompetitionById(id);
		competition.setState(1);
		competitionRepository.save(competition);
		try{
			response.sendRedirect("/sup_competition.html?state="+(String)map.get("state"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	 * 下线比赛
	 */
	@RequestMapping(value="/sup_undercarriage_competition",method=RequestMethod.GET)
	public void undercarriageCompetition(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		PageData pd=new PageData(request);
		map=pd.getModelMap();
		Long id=Long.parseLong((String)map.get("id"));
		Competition competition=new Competition();
		competition=competitionRepository.findCompetitionById(id);
		competition.setState(0);
		competitionRepository.save(competition);
		try{
			response.sendRedirect("/sup_competition.html?state="+(String)map.get("state"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	 * ***********************************************************
	 * ************************分隔线*****************************
	 * ***********************************************************
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	/*
	 * 练习列表页
	 */
	@RequestMapping(value="/sup_practice.html",method=RequestMethod.GET)
	public String goSupPractice(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		String state=request.getParameter("state");
		List<Competition> practices=new ArrayList<Competition>();//用来存储比赛信息
		if(state==null||state.equals("0")){//查询未发布练习
			state="0";
			practices=competitionRepository.findDisPublishPractice();
		}else if(state.equals("1")){//查询已发布练习
			practices=competitionRepository.findPublishedPractice();
		}
		List practiceList=new ArrayList();
		for(Competition c:practices){
			Map m=new HashMap();
			try {
				m.put("id", c.getId());
				m.put("title", c.getTitle());
				m.put("time", c.getTime());
				m.put("state", c.getState());//该state是practice.state，意义是练习的发布与否
				int handler_character=c.getHandler_character();
				if(handler_character==0){//如果是超级管理员
					Long id=c.getHandler_id();
					SupAdmin supAId=supAdminRepository.findSupAdminById(id);
					String name=supAId.getName();
					m.put("handler_name", name);
				}else if(handler_character==1){//如果是管理员
					/*
					 * 
					 * 此处添加管理员情况的处理过程
					 * 
					 */
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			practiceList.add(m);
		}
		map.addAttribute("practices", practiceList);
//		map.addAttribute("handler_names", handler_names);
		map.addAttribute("state", state);//该state是标记提交请求前所在的页面显示的是哪种状态的练习 0未发布，1已发布
		return "supAdmin/sup_practice";
	}
	/*
	 * 新增练习页
	 */
	@RequestMapping(value="/sup_new_practice.html",method=RequestMethod.GET)
	public String goSupNewPractice(){
		return "supAdmin/sup_new_practice";
	}
	/*
	 * 保存新增的练习信息
	 */
	@RequestMapping(value="/sup_add_practice",method=RequestMethod.POST)
	public String addPractice(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		PageData pd=new PageData(request);
		map=pd.getModelMap();
		Competition practice=new Competition();
		Date date=new Date();
		Timestamp time=new Timestamp(date.getTime());
		HttpSession session=request.getSession();
		String character=(String)session.getAttribute("character");//从session中获取登陆者的身份
		String id=(String)session.getAttribute("id");//从session中获取登陆者的id
		if(character.equals("0")){
			practice.setHandler_character(0);
			practice.setHandler_id(Long.parseLong(id));
		}else{
			practice.setHandler_character(1);
			practice.setHandler_id(Long.parseLong(id));
		}
		practice.setTitle((String)map.get("title"));
//		practice.setStart_time(start_time);
//		practice.setEnd_time(end_time);
		practice.setTime(time);
		practice.setState(0);
		practice.setType(1);
		competitionRepository.save(practice);
		return "success";
	}
	/*
	 * 编辑练习页
	 */
	@RequestMapping(value="/sup_edit_practice.html",method=RequestMethod.GET)
	public String goSupEditPractice(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		String id=request.getParameter("id");
		Competition practice=competitionRepository.findCompetitionById(Long.parseLong(id));
		map.addAttribute("practice", practice);
		return "supAdmin/sup_edit_practice";
	}
	/*
	 * 保存编辑后的练习内容
	 */
	@RequestMapping(value="/sup_edit_practice",method=RequestMethod.POST)
	public String editPractice (HttpServletRequest request,HttpServletResponse response,ModelMap map){
		PageData pd=new PageData(request);
		Competition practice=new Competition();;
		map=pd.getModelMap();
//		HttpSession session=request.getSession();
//		int character=(int)session.getAttribute("character");//从session中获取登陆者的身份
//		Long id=(Long)session.getAttribute("id");//从session中获取登陆者的id
		practice=competitionRepository.findCompetitionById(Long.parseLong((String)map.get("id")));
		practice.setTitle((String)map.get("title"));
//		practice.setStart_time(Timestamp.valueOf((String)map.get("start_time")));
//		practice.setEnd_time(Timestamp.valueOf((String)map.get("end_time")));
		competitionRepository.save(practice);
		return "success";
		
	}
	/*
	 * 删除练习
	 */
	@RequestMapping(value="/sup_delete_practice",method=RequestMethod.GET)
	public void deletePractice(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		PageData pd=new PageData(request);
		map=pd.getModelMap();
		Long id=Long.parseLong((String)map.get("id"));
		competitionRepository.deleteById(id);
		//回到列表页
		try {
			response.sendRedirect("/sup_practice.html?state="+(String)map.get("state"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * 发布练习
	 */
	@RequestMapping(value="/sup_publish_practice",method=RequestMethod.GET)
	public void publishPractice(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		PageData pd=new PageData(request);
		map=pd.getModelMap();
		Long id=Long.parseLong((String)map.get("id"));
		Competition practice=new Competition();
		practice=competitionRepository.findCompetitionById(id);
		practice.setState(1);
		competitionRepository.save(practice);
		try{
			response.sendRedirect("/sup_practice.html?state="+(String)map.get("state"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	 * 下线练习
	 */
	@RequestMapping(value="/sup_undercarriage_practice",method=RequestMethod.GET)
	public void undercarriagePractice(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		PageData pd=new PageData(request);
		map=pd.getModelMap();
		Long id=Long.parseLong((String)map.get("id"));
		Competition practice=new Competition();
		practice=competitionRepository.findCompetitionById(id);
		practice.setState(0);
		competitionRepository.save(practice);
		try{
			response.sendRedirect("/sup_practice.html?state="+(String)map.get("state"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
