package com.openJudge.openJudge.myTools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/*
 * 文件处理工具类
 */
public class FileUtil {
	/*
	 * 写入文件
	 * @param filePath 文件目录
	 * @param fileName 文件名
	 */
	public static void uploadFile(byte[] file,String filePath,String fileName) throws IOException{
		File targetFile=new File(filePath);
		if(!targetFile.exists()){
			targetFile.mkdirs();
		}
			FileOutputStream out=new FileOutputStream(filePath+"/"+fileName);
			out.write(file);
			out.flush();
			out.close();
	}
}
