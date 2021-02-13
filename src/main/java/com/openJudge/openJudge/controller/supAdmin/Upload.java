package com.openJudge.openJudge.controller.supAdmin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.openJudge.openJudge.myTools.FileUtil;

@Controller
public class Upload {
	@GetMapping("/file")
	public String goUploadPage(){
		return "upload";
	}
	@PostMapping("/upload")
	@ResponseBody
	public String upload(HttpServletRequest request,@RequestParam("file") MultipartFile file){
		String info=request.getParameter("info");
		System.out.println(info);
		String contentType=file.getContentType();
		
		String fileName=file.getOriginalFilename();
		System.out.println(contentType+"  "+fileName);
		String filePath="F:/upload";
		try{
			FileUtil.writeToFile(file.getBytes(), filePath, fileName);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("文件上传失败！");
		}
		//FileUtil.uploadFile()
		return "文件上传成功！";
	}
}
