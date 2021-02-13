package com.openJudge.openJudge.controller.supAdmin;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.openJudge.openJudge.service.SampleService;
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
import com.openJudge.openJudge.myTools.FileUtil;
import com.openJudge.openJudge.myTools.PageData;
import com.openJudge.openJudge.myTools.ZipUtil;
import com.openJudge.openJudge.repository.CompetitionRepository;
import com.openJudge.openJudge.repository.SupAdminRepository;
import com.openJudge.openJudge.repository.TopicRepository;

@Controller
public class SupTopicController {
    @Autowired
    TopicRepository topicRepository;
    @Autowired
    SupAdminRepository supAdminrepository;
    @Autowired
    CompetitionRepository competitionRepository;

    /**
     * 题目列表页
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @RequestMapping(value = "/sup_topic.html", method = RequestMethod.GET)
    public String goSupTopic(ModelMap map, HttpServletRequest request) {
        List<Topic> topics = (List<Topic>) topicRepository.findAll();
        //数据库表topic中没有存储通过人数、尝试人数、通过率，而是将他们的id通过字符串的形式储存，因此，需要做一下处理，将他们赋给实体的对应成员变量
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
            SupAdmin supAdmin = new SupAdmin();
            if (topics.get(i).getHandler_character() == 0) {// 超级管理员
                supAdmin = supAdminrepository.findSupAdminById(topics.get(i).getHandler_id());
            } else {
                /*
                 * 添加管理员情况下的代码 adminrepository.findAdminById(t.getHandler_id());
                 */
            }
            topics.get(i).setHandler_name(supAdmin.getName());
        }
        map.addAttribute("topics", topics);
        return "supAdmin/sup_topic";
    }

    /**
     * 新增题目页（选择题目所属练习或比赛时，需先将比赛或练习下线）
     */
    @RequestMapping(value = "/sup_new_topic.html", method = RequestMethod.GET)
    public String goSupNewTopic(HttpServletRequest request, ModelMap map) {
        List<Competition> competitionsOrPractices = new ArrayList<Competition>();
        HttpSession session = request.getSession();
        int handler_character = Integer.parseInt((String) session.getAttribute("character"));
        // Long handler_id=Long.parseLong((String)session.getAttribute("id"));
        if (handler_character == 0) {
            // 超级管理员可以新建所有类型题
            competitionsOrPractices = (List<Competition>) competitionRepository.findDisPublishCompetitionOrPractice();
        } else {
            // 普通管理员只能新建练习题
            competitionsOrPractices = (List<Competition>) competitionRepository.findDisPublishPractice();
        }
        map.addAttribute("competitionsOrPractices", competitionsOrPractices);
        return "supAdmin/sup_new_topic";
    }

    /**
     * 保存新增题目信息
     *
     * @throws IOException
     */
    @RequestMapping(value = "/sup_add_topic", method = RequestMethod.POST)
    public String addTopic(HttpServletRequest request, HttpServletResponse response, ModelMap map, @RequestParam("test_data") MultipartFile testDataFile) throws IOException {
        PageData pd = new PageData(request);
        // MultipartHttpServletRequest
        // mRequest=(MultipartHttpServletRequest)request;//错误！！！
        // MultipartFile testDataFile=mRequest.getFile("test_data");
        map = pd.getModelMap();
        Topic topic = new Topic();
        Date date = new Date();
        Timestamp time = new Timestamp(date.getTime());
        /* 上传压缩文件并解压到指定目录 */
        String rootPath = "F:" + File.separator + "openJudge_upload";//文件存储根目录（所有题目的测试数据都在这个文件夹下，包括临时压缩包所在的文件夹temp）
        //String temp=File.separator+"temp";//临时路径（用于临时存放上传过来的压缩包）
        String midPath = File.separator + (String) map.get("competition_id");//中间目录（是题目所属比赛、练习的id，所有该比赛或练习的题目的测试数据都存在该文件夹下）
        String subPath = File.separator + (String) map.get("number");//题目目录（是题号，存放该题目的所有测试数据）

        String contentType = testDataFile.getContentType();//获取文件类型
        if (contentType.equals("application/x-zip-compressed")) {//如果是zip格式
            String originFileName = testDataFile.getOriginalFilename();//获取上传文件的名字
            try {
                FileUtil.writeToFile(testDataFile.getBytes(), rootPath + midPath + subPath, originFileName);//将上传的文件放到临时文件夹的下的对应比赛下的对应的题号文件夹下
                System.out.println("上传文件成功!");
            } catch (Exception e) {
                // TODO: handle exception
                System.out.println("上传文件失败!");
            }
            ZipUtil.upzipFile(rootPath + midPath + subPath, rootPath + midPath + subPath + File.separator + originFileName);//将压缩包解压到当前文件夹
            File[] files = new File(rootPath + midPath + subPath).listFiles();
            for (File f : files) {//zip目录解压后可能会有一个多余的文件夹，此处循环将该文件夹下的文件移动到rootPath+midPath+subPath下
                if (f.isDirectory()) {
                    File[] fs = f.listFiles();
                    for (File _f : fs) {
                        FileUtil.moveFile(rootPath + midPath + subPath, _f);
                    }
                    f.delete();//删除移除文件后的空文件夹
                }
            }
            if (new File(rootPath + midPath + subPath + File.separator + originFileName).delete()) {
                System.out.println("删除压缩包成功!");
            } else {
                System.out.println("删除压缩包失败!");
            }
        } else {
            return "fail";
        }
        /* 上传压缩文件并解压到指定目录 */




        /* 上传压缩文件并解压到指定目录 */
        /*
         * if(testDataFile.isEmpty()){ map.addAttribute("errorMsg", "文件为空！");
         * return "fail"; } String
         * fileName=testDataFile.getOriginalFilename();//获取上传文件的名字
         * if(fileName.substring(fileName.lastIndexOf("."),
         * fileName.length())!="zip"&&fileName.substring(fileName.lastIndexOf(
         * "."), fileName.length())!="rar"){ map.addAttribute("errorMsg",
         * "请上传zip或rar格式的压缩文件！"); } int
         * size=(int)testDataFile.getSize();//获取文件大小 if(size>1000000){
         * map.addAttribute("errorMsg", "文件大小不能超过10M！"); return "fail"; }
         * System.out.println(fileName+"-->"+size); String
         * path="F:/openJudgeTestData";//测试文件存放的根目录 String
         * midPath=(String)map.get("competition_id");//中间目录（练习、比赛号） String
         * subPath=(String)map.get("number");//子目录（题目题号） String
         * test_data_path=path+midPath+subPath+fileName; File f=new
         * File(test_data_path);
         * if(!f.getParentFile().getParentFile().exists()){//如果根目录不存在，新建
         * f.getParentFile().getParentFile().mkdirs(); }else{
         * if(!f.getParentFile().exists()){//如果中间目录不存在，新建
         * f.getParentFile().mkdirs(); } } try{
         * testDataFile.transferTo(f);//保存文件 }catch (Exception e) {
         * e.printStackTrace(); }
         */
        /* 上传压缩文件并解压到指定目录 */
        String testDataPath = rootPath + midPath + subPath;// 测试文件所在文件夹

        Competition competition = competitionRepository.findCompetitionById(Long.parseLong((String) map.get("competition_id")));

        topic.setCompetition(competition);
        topic.setContent((String) map.get("content"));
        topic.setInfo((String) map.get("info"));
        topic.setInput_intro((String) map.get("input_intro"));
        topic.setInput_sample((String) map.get("input_sample"));
        topic.setMemory_limit(Integer.parseInt((String) map.get("memory_limit")));
        topic.setNumber(Integer.parseInt((String) map.get("number")));
        topic.setOutput_intro((String) map.get("output_intro"));
        topic.setOutput_sample((String) map.get("output_sample"));
        topic.setTest_data_path(testDataPath);
        // topic.setTest_data_path((String) map.get("test_data_path"));
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

    /**
     * 查看题目详情页
     */
    @RequestMapping(value = "/sup_detail_topic.html", method = RequestMethod.GET)
    public String goSupTopicDetail(HttpServletRequest request, ModelMap map) {
        Long topic_id = Long.parseLong(request.getParameter("id"));
        Topic topic = topicRepository.findTopicById(topic_id);
        map.addAttribute("topic", topic);
        return "supAdmin/sup_detail_topic";
    }

    /**
     * 编辑题目页
     */
    @RequestMapping(value = "/sup_edit_topic.html", method = RequestMethod.GET)
    public String goSupEditTopic(HttpServletRequest request, ModelMap map) {
        Long id = Long.parseLong((String) request.getParameter("id"));
        Competition competitionOrPractice = new Competition();
        Topic topic = topicRepository.findTopicById(id);
        HttpSession session = request.getSession();
        int handler_character = Integer.parseInt((String) session.getAttribute("character"));
		/*if (handler_character == 0) {
			// 超级管理员可以新建所有类型题
			competitionsOrPractices = (List<Competition>) competitionRepository.findAll();
		} else {
			// 普通管理员只能新建练习题
			competitionsOrPractices = (List<Competition>) competitionRepository.findAllPractice();
		}*/
        competitionOrPractice = competitionRepository.findCompetitionById(topic.getCompetition().getId());
        map.addAttribute("topic", topic);
        map.addAttribute("competitionOrPractice", competitionOrPractice);
        return "supAdmin/sup_edit_topic";
    }

    /**
     * 保存编辑内容
     */
    @RequestMapping(value = "/sup_edit_topic", method = RequestMethod.POST)
    public String editTopic(HttpServletRequest request, HttpServletResponse respone, ModelMap map) {
        Long id = Long.parseLong((String) request.getParameter("id"));
        Competition competition = competitionRepository
                .findCompetitionById(Long.parseLong(request.getParameter("competition_id")));
        Topic topic = topicRepository.findTopicById(id);
        topic.setCompetition(competition);
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
        String change_test_data = request.getParameter("chang_test_data");// 从获取测试数据是否更改的信息
        if (change_test_data != null && change_test_data.equals("1")) {
            /*
             * 添加上传新测试数据的代码
             */
        }
        topicRepository.save(topic);
        return "success";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/sup_delete_topic", method = RequestMethod.GET)
    public void delectTopic(HttpServletRequest request, HttpServletResponse response, ModelMap map)
            throws UnsupportedEncodingException, IOException {
        Long id = Long.parseLong((String) request.getParameter("id"));
        Topic topic = new Topic();
        topic = topicRepository.findTopicById(id);
        if (topic == null) {// 题目不存在（已被删除）
            response.sendRedirect("/sup_topic.html");
        } else {//
            topicRepository.deleteById(id);
            response.sendRedirect("/sup_topic.html");
        }
    }
}
