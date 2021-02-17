package com.openJudge.openJudge.executor.impl;

import com.openJudge.openJudge.consist.BaseConstant;
import com.openJudge.openJudge.entity.ExecutorResult;
import com.openJudge.openJudge.executor.Executor;
import com.openJudge.openJudge.myTools.ProcessUtil;

import java.io.DataOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author aries
 * @Data 2021-02-15
 */
public class JavaExecuteExecutor implements Executor {
    private String classFilePath;
    private String input;

    public JavaExecuteExecutor(String classFilePath, String input) {
        this.classFilePath = classFilePath;
        this.input = input;
    }


    @Override
    public ExecutorResult service() {
        Runtime rt = Runtime.getRuntime();
        Process p1 = null;
        String[] command2 = {BaseConstant.JDK_PATH + "\\java.exe", "-cp", classFilePath};
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
            e.printStackTrace();
            System.out.println("执行java命令出错！");
        }
        return ProcessUtil.getSystemOut(p1);
    }
}
