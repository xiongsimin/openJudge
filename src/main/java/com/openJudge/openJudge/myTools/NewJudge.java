package com.openJudge.openJudge.myTools;

import com.openJudge.openJudge.consist.BaseConstant;
import com.openJudge.openJudge.entity.ExecutorResult;
import com.openJudge.openJudge.entity.JudgeResult;
import com.openJudge.openJudge.entity.Sample;
import com.openJudge.openJudge.exception.CustomException;
import com.openJudge.openJudge.executor.Executor;
import com.openJudge.openJudge.factory.executor.ExecutorFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.*;

/**
 * @Author aries
 * @Data 2021-02-08
 */
@Component
public class NewJudge {

    @Autowired
    ThreadPoolExecutor myExecuteTaskThreadPool;

    public static JudgeResult judge(String code, List<Sample> sampleList, String type, Long userId) throws IOException, InterruptedException {
        JudgeResult result = new JudgeResult();
        Date date = new Date();
        Long time = date.getTime();//获取系统当前时间
        String sourceFilePath = BaseConstant.SOURCE_FILE_ROOT_PATH + File.separator + userId + File.separator + time;
        if ("java".equals(type)) {
            //编译源文件
            Map map = execJavac(code, sourceFilePath);
            //编译出错
            if (map.get("errorMsg") != "") {
                map.put("state", "Compile Error");
            }
            //编译通过
            else {
                //3.获取编译后的class文件名
                String tempClassFileName = "";//用于存储class文件名

                File[] sourceFiles = (new File(sourceFilePath)).listFiles();
                for (File f : sourceFiles) {
                    if (f.getName().substring(f.getName().lastIndexOf("."), f.getName().length()).equals(".class")) {
                        tempClassFileName = f.getName().substring(0, f.getName().lastIndexOf("."));//截取class文件名（不包括后缀）
                        break;
                    }
                }
                final String classFileName = tempClassFileName;
                //失败用例
                List<Sample> failedSample = new ArrayList<>();
                //用例线程数
                int sampleThreadCount = sampleList.size();
                CountDownLatch cd = new CountDownLatch(sampleThreadCount);
                //循环遍历测试用例
                for (Sample sample : sampleList) {
                    new Thread(() -> {
                        System.out.println("线程“" + Thread.currentThread().getName() + "”开始执行");
                        //执行二进制字节码，得到结果
                        Map execResult = execJava(sample.getInput(), sourceFilePath, classFileName);
                        //结果与测试用例标准答案对比
                        //运行时错误
                        if (!"".equals(execResult.get("errorMsg"))) {
//                        execResult.put("state", "Runtime Error");
                            System.out.println("运行时错误！");
                            result.setState(BaseConstant.JUDGE_RESULT_STATE_RUNTIME_ERROR);
                        } else {
                            //用户程序输出
                            String userOutput = (String) execResult.get("result");
                            System.out.println(execResult.get("result"));
                            //正确答案与输出不相同
                            if (!sample.getOutput().equals(userOutput)) {
//                            execResult.put("state", "Wrong Answer");
                                System.out.println("Wrong Answer");
                                result.setState(BaseConstant.JUDGE_RESULT_STATE_WRONG_ANSWER);
                                sample.setUserOutput(userOutput);
                                failedSample.add(sample);
                            }
                        }
                        cd.countDown();
                        System.out.println("线程“" + Thread.currentThread().getName() + "”执行完毕");
                    }, "用例[" + sample.getId() + "]的线程").start();

                }
                //等待所有线程用例全部执行完毕
                cd.await();
                System.out.println("用例线程全部执行完毕");
                result.setFailedSamples(failedSample);
                result.setAcceptRate((float) (sampleList.size() - failedSample.size()) / sampleList.size());
                if (failedSample.size() == 0) {
                    result.setState(BaseConstant.JUDGE_RESULT_STATE_ACCEPT);
                }
            }
        }
        return result;
    }

    /**
     * 编译和运行都封装为特定类后重构
     *
     * @param code
     * @param sampleList
     * @param type
     * @param userId
     * @return
     * @throws InterruptedException
     */
    //TODO 此处可拆分成微服务，或消息队列或使用****线程池****，以解决高并发时可能导致的服务宕掉的问题
    public JudgeResult newJudge(String code, List<Sample> sampleList, String type, Long userId) throws InterruptedException, IOException {
        JudgeResult result = new JudgeResult();
        List<Future<ExecutorResult>> futureList = new ArrayList<>();
        Date date = new Date();
        Long time = date.getTime();//获取系统当前时间
        String sourceFilePath = BaseConstant.SOURCE_FILE_ROOT_PATH + File.separator + userId + File.separator + time;
        //将代码写入源文件
        FileUtil.writeToFile(code.getBytes(), sourceFilePath, BaseConstant.JAVA_SOURCE_FILE_NAME);
        Executor compileExecutor = ExecutorFactory.getInstance(type, BaseConstant.WHICH_STAGE_COMPILE, sourceFilePath, null, null);
        Future<ExecutorResult> compileFuture = null;
        try {
            compileFuture = myExecuteTaskThreadPool.submit((Callable<ExecutorResult>) compileExecutor);
        } catch (RejectedExecutionException e) {
            result.setState(BaseConstant.JUDGE_RESULT_STATE_SYSTEM_BUSY);
            e.printStackTrace();
            return result;
        }

//        Future<ExecutorResult> compileFuture = myExecuteTaskThreadPool.submit((Callable<ExecutorResult>) compileExecutor);
        ExecutorResult compilerResult = null;
        try {
            compilerResult = compileFuture.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //编译期错误
        if (StringUtils.isNotBlank(compilerResult.getErrorMsg())) {
            result.setState(BaseConstant.JUDGE_RESULT_STATE_COMPILE_ERROR);
            result.setCompileErrorMsg(compilerResult.getErrorMsg());
        }
        //编译通过
        else {
            //失败用例
            List<Sample> failedSample = new ArrayList<>();
            //用例线程数
            int sampleThreadCount = sampleList.size();
//            CountDownLatch cd = new CountDownLatch(sampleThreadCount);
            //循环遍历测试用例
            for (Sample sample : sampleList) {
                System.out.println("线程“" + Thread.currentThread().getName() + "”开始执行");
                //执行二进制字节码，得到结果
                Executor executeExecutor = ExecutorFactory.getInstance(type, BaseConstant.WHICH_STAGE_EXECUTE, sourceFilePath, sourceFilePath, sample);
//                myExecuteTaskThreadPool.submit((Runnable) executeExecutor);
                ExecutorResult executeResult = null;
                try {
                    Future<ExecutorResult> executeFuture = myExecuteTaskThreadPool.submit((Callable<ExecutorResult>) executeExecutor);
                    futureList.add(executeFuture);
                } catch (RejectedExecutionException e) {
                    result.setState(BaseConstant.JUDGE_RESULT_STATE_SYSTEM_BUSY);
                    e.printStackTrace();
                    return result;
                }
            }
            for (Future<ExecutorResult> future : futureList) {
                ExecutorResult executorResult = null;
                try {
                    executorResult = future.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                Sample sample = executorResult.getSample();
                //结果与测试用例标准答案对比
                //运行时错误
                if (StringUtils.isNotBlank(executorResult.getErrorMsg())) {
                    result.setState(BaseConstant.JUDGE_RESULT_STATE_RUNTIME_ERROR);
                } else {
                    //用户程序输出
                    String userOutput = executorResult.getResult();
                    //正确答案与输出不相同
                    if (!sample.getOutput().equals(userOutput)) {
                        result.setState(BaseConstant.JUDGE_RESULT_STATE_WRONG_ANSWER);
                        sample.setUserOutput(userOutput);
                        failedSample.add(sample);
                    }
                }
            }
            //等待所有线程用例全部执行完毕
//            cd.await();
            System.out.println("用例线程全部执行完毕");
            result.setFailedSamples(failedSample);
            result.setAcceptRate((float) (sampleList.size() - failedSample.size()) / sampleList.size());
            if (failedSample.size() == 0) {
                result.setState(BaseConstant.JUDGE_RESULT_STATE_ACCEPT);
            }
        }
        return result;
    }

    private static Map execJava(String input, String sourceFilePath, String classFileName) {
        Map result = new HashMap();
        Runtime rt = Runtime.getRuntime();
        Process p1 = null;//
        Process p2 = null;//
        String[] command2 = {BaseConstant.JDK_PATH + "\\java.exe", "-cp", sourceFilePath, classFileName};
        System.out.println(sourceFilePath + File.separator + classFileName + ".class");
        try {
            p1 = rt.exec(command2);
            //rt.exec(inputFile);
            DataOutputStream dos = new DataOutputStream(p1.getOutputStream());
            dos.write(input.getBytes());
            dos.flush();
            dos.close();
            //p1.wait(Long.parseLong("2000"));
            System.out.println("输入了" + input);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("执行java命令出错！");
        }
        return getSystemOut(p1);
    }


    /**
     * 获取程序运行结果或错误信息
     *
     * @param process
     * @return map(" result " - result, " errorMsg " - errorMsg)
     */
    public static Map<String, String> getSystemOut(final Process process) {
        Map<String, String> map = new HashMap<String, String>();
        String result = read(process.getInputStream(), System.out).get("msg");
        String errorMsg = read(process.getErrorStream(), System.err).get("msg");
        map.put("result", result);
        map.put("errorMsg", errorMsg);
        return map;
    }

    /**
     * 读取输入流并打印
     *
     * @param inputStream process.getInputStream()
     * @param out         System.out
     * @return map 返回一个键值对（"msg"-msg），存储运行结果，如果没有结果，msg为""
     */
    public static Map<String, String> read(InputStream inputStream, PrintStream out) {
        Map<String, String> map = new HashMap<String, String>();
        String msg = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("GBK")));
            String line;
            while ((line = reader.readLine()) != null && msg.length() < 200) {
                out.println(line);
                msg += line;
            }
            inputStream.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        map.put("msg", msg);
        return map;
    }

    /**
     * 执行javac编译java源文件
     *
     * @param code
     * @param codePath
     * @return
     * @throws IOException
     */
    private static Map execJavac(String code, String codePath) throws IOException {
//        String codePath = "F:\\openJudge_judge" + File.separator + userId + File.separator + time;
        String sourceFileName = "Main.java";
        FileUtil.writeToFile(code.getBytes(), codePath, sourceFileName);
        //2.使用javac Xxx.java进行编译
        Runtime rt = Runtime.getRuntime();
        Process p1 = null;//
        Process p2 = null;//
        String[] command1 = {BaseConstant.JDK_PATH + "\\javac.exe", "-encoding", "utf-8", codePath + File.separator + sourceFileName};
        try {
            p1 = rt.exec(command1);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("执行javac命令出错！");
        }
        return getSystemOut(p1);
    }

    public static void main(String[] args) {
        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}
