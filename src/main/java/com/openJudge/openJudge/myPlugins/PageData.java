package com.openJudge.openJudge.myPlugins;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;

public class PageData {
	private HttpServletRequest request;
	// private HttpServletResponse response;
	private ModelMap map = new ModelMap();

	/*
	 * 构造函数// 将页面参数存放在ModelMap中
	 */
	public PageData(HttpServletRequest req) {
		request = req;
		try {
			request.setCharacterEncoding("utf-8");
		} catch (Exception ex) {
			System.out.println("设置字符集失败！");
		}
		Enumeration paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String pn = (String) paramNames.nextElement();
			if (request.getParameterValues(pn).length > 1)// 复选框
			{
				String[] params = request.getParameterValues(pn);
				map.addAttribute(pn, params);
			} else {
				String value = request.getParameter(pn);
				map.addAttribute(pn, value);
			}
		}
	}
	/*
	 * 返回参数集
	 */
	public ModelMap getModelMap() {
		return map;
	}
}
