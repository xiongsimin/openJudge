package com.openJudge.openJudge.myTools;

import com.openJudge.openJudge.consist.BaseConstant;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Judge {
	/**
	 * openJudge系统核心函数：源代码判定函数
	 * @param code	源代码
	 * @param type	源代码类型（c、c++、java）
	 * @param timeLimit		时间限制（单位为s）
	 * @param memoryLimit	内存限制（单位为MB）
	 * @Param testDataPath	测试文件所在目录
	 * @param userId	````用户id
	 * @return	返回是包括4个键值对的map，分别是state-评测结果（Accept、Compile Error、Runtime Error、Wrong Answer、System Error）、errorMsg-错误信息、time-运行时间（暂未实现）、memory-占用内存（暂未实现）
	 * @throws IOException
	 */
	public static Map<String,String> doJudge(String code,String type,int timeLimit,int memoryLimit,String testDataPath,Long userId) throws IOException{
		Map<String,String> map=new HashMap<String,String>();
		Date date=new Date();
		Long time=date.getTime();//获取系统当前时间
		if(type.equals("java")){//对java进行处理
			//1.在指定目录下建一个Main.java文件，并将code写入（目录命名规则：F:\openJudge_judge\用户id\系统当前时间）
			String codePath="F:\\openJudge_judge"+File.separator+userId+File.separator+time;
			String sourceFileName="Main.java";
			FileUtil.writeToFile(code.getBytes(),codePath,sourceFileName);
			//2.使用javac Xxx.java进行编译
			Runtime rt=Runtime.getRuntime();
			Process p1=null;//
			Process p2=null;//
			String[] command1={BaseConstant.JDK_PATH+"\\javac.exe","-encoding","utf-8",codePath+File.separator+sourceFileName};
			try{
				p1=rt.exec(command1);
			}catch (Exception e) {
				// TODO: handle exception
				System.out.println("执行javac命令出错！");
			}
			map=getSystemOut(p1);
			if(map.get("errorMsg")!=""){//编译出错
				map.put("state", "Compile Error");
			}else{//编译通过
				//TODO 尝试从缓存中获取题目输入、输出用例，缓存中不存在再从文件中读取
				//3.获取编译后的class文件名
				String classFileName="";//用于存储class文件名
				File[] sourceFiles=(new File(codePath)).listFiles();
				for(File f:sourceFiles){
					if(f.getName().substring(f.getName().lastIndexOf("."),f.getName().length()).equals(".class")){
						classFileName=f.getName().substring(0,f.getName().lastIndexOf("."));//截取class文件名（不包括后缀）
						break;
					}
				}
				//4.获取输入测试文件内容
				File inputFile1=new File(testDataPath+File.separator+"input1.txt");//用于判断输入数据是否存在
				if(inputFile1.exists()){//存在输入文件
					File[] files=(new File(testDataPath).listFiles());//获取测试文件目录下所有文件
					int count=files.length/2;//获取测试用例的组数
					for(int i=1;i<=count;i++){//循环判断是否满足所有测试用例
						String inputFile=FileUtil.readFromFile(testDataPath+File.separator+"input"+i+".txt");//从输入文件获取输入
						String rightAnswer=FileUtil.readFromFile(testDataPath+File.separator+"output"+i+".txt");//从输出文件获取正确结果
						String[] command2={BaseConstant.JDK_PATH+"\\java.exe","-cp",codePath,classFileName};
						System.out.println(codePath+File.separator+classFileName+".class");
						try{
							p1=rt.exec(command2);
							//rt.exec(inputFile);
							DataOutputStream dos=new DataOutputStream(p1.getOutputStream());
							dos.write(inputFile.getBytes());
							dos.flush();
							dos.close();
							//p1.wait(Long.parseLong("2000"));
							System.out.println("输入了"+inputFile);
						}catch (Exception e) {
							// TODO: handle exception
							System.out.println("执行java命令出错！");
						}
						map=getSystemOut(p1);
						if(map.get("errorMsg")!=""){//运行时错误
							map.put("state", "Runtime Error");
							System.out.println("运行时错误！");
							break;
						}else{
							System.out.println(map.get("result"));
							System.out.println(rightAnswer+222);
							if(rightAnswer.equals(map.get("result")+"\n")){//正确答案与输出相同
								map.put("state", "Accept");
								System.out.println("Accept");
							}else{
								map.put("state", "Wrong Answer");
								System.out.println("Wrong Answer");
								break;
							}
						}
					}


				}else{//不存在输入文件，只需要判断结果与输出文件是否相同（肯定只有一个输出测试文件）
					File outputFile1=new File(testDataPath+File.separator+"output1.txt");
					if(outputFile1.exists()){//输出测试文件存在
						String rightAnswer=FileUtil.readFromFile(outputFile1);//从输出文件获取正确结果
						//System.out.println(22+rightAnswer+11);
						//5.运行java Xxx ,并将输入内容以参数形式输入，并捕获返回的运行结果（只有一个输出结果的情况）
						String[] command2={"I:\\jdk1.8.0_171\\bin\\java.exe","-Djava.security.manager","-cp",codePath,classFileName};
						System.out.println(codePath+File.separator+classFileName+".class");
						try{
							p1=rt.exec(command2);
							//p1.destroy();
						}catch (Exception e) {
							// TODO: handle exception
							System.out.println("执行java命令出错！");
						}
						map=getSystemOut(p1);
						if(map.get("errorMsg")!=""){//运行时错误
							map.put("state", "Runtime Error");
							System.out.println("运行时错误！");
						}else{
							if(rightAnswer.equals(map.get("result")+"\n")){//正确答案与输出相同
								map.put("state", "Accept");
								System.out.println("Accept");
							}else{
								map.put("state", "Wrong Answer");
								System.out.println("Wrong Answer");
							}
						}
					}else{//不存在，为系统错误
						System.out.println("测试文件不存在！");
						map.put("errorMsg", "System Error");
					}
				}

				//6.判断运行结果，给4个键值对赋值
			}

			map.put("time", "2");
			map.put("memory", "1");
			return map;
			}
		return map;
	}
	public static void runCode(String code,String type,String testDataPath){

	}
	/**
	 * 打印进程输出结果
	 * @param process 进程
	 */
	public static void printProcessOutput(final Process process){
		read(process.getInputStream(),System.out);
		read(process.getErrorStream(),System.err);
	}
	/**
	 * 读取输入流并打印
	 * @param inputStream    process.getInputStream()
	 * @param out    System.out
	 * @return map 返回一个键值对（"msg"-msg），存储运行结果，如果没有结果，msg为""
	 */
	public static Map<String,String> read(InputStream inputStream,PrintStream out){
		Map<String,String> map=new HashMap<String,String>();
		String msg="";
		try{
			BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream,Charset.forName("GBK")));
			String line;
			while((line=reader.readLine())!=null&&msg.length()<200){
				out.println(line);
				msg+=line;
			}
			inputStream.close();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		map.put("msg", msg);
		return map;
	}
	/**
	 * 获取程序运行结果或错误信息
	 * @param process
	 * @return  map("result"-result,"errorMsg"-errorMsg)
	 */
	public static Map<String,String> getSystemOut(final Process process){
		Map<String,String> map=new HashMap<String,String>();
		String result=read(process.getInputStream(),System.out).get("msg");
		String errorMsg=read(process.getErrorStream(),System.err).get("msg");
		map.put("result", result);
		map.put("errorMsg", errorMsg);
		return map;
	}
}
