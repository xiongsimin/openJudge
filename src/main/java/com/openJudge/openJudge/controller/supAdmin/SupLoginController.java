package com.openJudge.openJudge.controller.supAdmin;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.openJudge.openJudge.config.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.openJudge.openJudge.entity.SupAdmin;
import com.openJudge.openJudge.repository.SupAdminRepository;

@Controller
public class SupLoginController {

    @Autowired
    SupAdminRepository supAdminRepository;

    @RequestMapping("/sup")
    public String goLogin() {
        System.out.println(ConfigProperties.getUploadPath());
        return "supAdmin/login";
    }

    @RequestMapping(value = "/sup/index", method = RequestMethod.POST)
    public String goIndex(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        // PageData pd=new PageData(request);
        // map=pd.getModelMap();
        // String id=(String)map.get("id");
        // String psw=(String)map.get("psw");
        String id = request.getParameter("id");
        String psw = request.getParameter("psw");
        String character = request.getParameter("character");// 标记登录时身份，请求中无此参数的规定为超级管理员
        if (character == null) {
            character = "0";
        }
        SupAdmin supAdmin = supAdminRepository.findSupAdminById(Long.parseLong(id));
        // map=new ModelMap();
        if (supAdmin != null) {
            if (supAdmin.getPsw().equals(psw)) {
                // String last_login=supAdmin.getLast_login().toString();
                map.addAttribute("supAdmin", supAdmin);
                Timestamp last_login = supAdmin.getLast_login();
                map.addAttribute("last_login", last_login);
                HttpSession session = request.getSession();
                session.setAttribute("id", id);
                session.setAttribute("character", character);
                map.addAttribute("character", character);
                // 更新登录时间
                Date date = new Date();
                supAdmin.setLast_login(new Timestamp(date.getTime()));// 此处修改supAdmin会导致前面{{map.addAttribute("supAdmin",supAdmin);}}中map中的last_login的值发生改变
                supAdminRepository.save(supAdmin);
                return "supAdmin/index";
            } else {
                map.addAttribute("msg", "账号密码不匹配！");
                return "supAdmin/login";
            }
        } else {
            map.addAttribute("msg", "账号不存在！");
            return "supAdmin/login";
        }

    }
}
