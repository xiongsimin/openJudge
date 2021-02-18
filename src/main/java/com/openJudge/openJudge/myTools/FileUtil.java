package com.openJudge.openJudge.myTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * 文件处理工具类
 */
public class FileUtil {
	/**
	 * 写入文件
	 * @param file 二进制比特流
	 * @param filePath 文件目录
	 * @param fileName 文件名
	 */
	public static void writeToFile(byte[] file, String filePath, String fileName) throws IOException {
		File targetFile = new File(filePath);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
			System.out.println("新建了路径【" + filePath + "】");
		}
		FileOutputStream out = new FileOutputStream(filePath + File.separator + fileName);
		try {
			out.write(file);
			out.flush();
			System.out.println("成功写入文件！");
		}catch (IOException e){
			e.printStackTrace();
		}finally {
			out.close();
			System.out.println("成功关闭写入流！");
		}
	}
	/**
	 * 读取文件内容并放入String中
	 * @param file	要读取的文件
	 * @return 文件内容
	 * @throws IOException 
	 */
	public static String readFromFile(File file) throws IOException{
		StringBuilder sb=new StringBuilder();
		String s="";
		BufferedReader br=new BufferedReader(new FileReader(file));
		while((s=br.readLine())!=null){
			sb.append(s+"\n");//每读取一行添加一个换行符
		}
		br.close();
		String str=sb.toString();
		return str;
	}
	/**
	 * 重载public static String readFromFile(File file);
	 * @param file	要读取的文件路径
	 * @return 文件内容
	 * @throws IOException 
	 */
	public static String readFromFile(String file) throws IOException{
		File dir=new File(file);
		return readFromFile(dir);
	}
	/**
	 * 将源路径下的文件移动到目标路径（非递归）
	 * @param targetPath 目标路径
	 * @param srcPath 源路径
	 * @throws IOException 
	 */
	public static void moveFile(String targetPath,String srcPath) throws IOException{
		File srcDir=new File(srcPath);
		File[] files=srcDir.listFiles();
		for(File f:files){
			moveFile(targetPath+File.separator+f.getName(),f);
		}
	}
	/**
	 * 将源文件移动到目标文件
	 * @param targeFile 目标文件
	 * @param srcFile 源文件
	 * @throws IOException 
	 */
	public static void moveFile(String targeFile,File srcFile) throws IOException{
		File target=new File(targeFile+File.separator+srcFile.getName());
		if(target.exists()){//如果已存在，删除
			target.delete();
			System.out.println("删除了文件【"+target+"】");
		}
		if(srcFile.renameTo(target)){
			System.out.println("成功将文件【"+srcFile.getPath()+"】移动到【"+target+"】");
		}else{
			System.out.println("移动文件失败！");
		}
		
	}
	/**
	 * 删除单个文件或路径（空路径）
	 * @param FilePath
	 */
	public static void deleteFile(String filePath){
		File dir=new File(filePath);
		if(dir.exists()&&dir.isFile()){//如果是存在且是文件
			if(dir.delete()){
				System.out.println("删除文件【"+filePath+"】成功！");
			}else{
				System.out.println("删除文件【"+filePath+"】失败！");
			}
		}else{
			System.out.println("删除失败！文件【"+filePath+"】不存在！");
		}
	}
	/**
	 * 递归删除文件夹及其下面的所有文件
	 * @param path 文件夹路径
	 */
	public static void deleteDirectory(String path){
		File dir= new File(path);
		if(dir.exists()&&dir.isDirectory()){
			File[] files=dir.listFiles();
			for(File f:files){
				if(f.isFile()){
					new File(path+File.separator+f.getName()).delete();
				}else{
					deleteDirectory(path+File.separator+f.getName());//递归删除
				}
			}
			dir.delete();//删除空文件夹
		}else{
			System.out.println("删除失败！路径【"+path+"】不存在！");
		}
	}
/*	public static void main(String[] args) throws IOException {
		//moveFile("F:\\openJudge_upload\\69\\2", "F:\\openJudge_upload\\69\\2DataTables.zip");
		String s=readFromFile(new File("F:\\test\\b.java"));
		System.out.println(s);
	}*/
}
