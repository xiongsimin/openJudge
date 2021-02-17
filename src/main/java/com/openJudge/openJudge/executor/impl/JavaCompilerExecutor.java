package com.openJudge.openJudge.executor.impl;

import com.openJudge.openJudge.consist.BaseConstant;
import com.openJudge.openJudge.entity.ExecutorResult;
import com.openJudge.openJudge.executor.Executor;
import com.openJudge.openJudge.myTools.ProcessUtil;


/**
 * @Author aries
 * @Data 2021-02-15
 */
public class JavaCompilerExecutor implements Executor {

    private String sourceFilePath;

    public JavaCompilerExecutor(String sourceFilePath) {
        this.sourceFilePath = sourceFilePath;
    }

    /**
     * 执行java编译程序
     *
     * @return
     */
    @Override
    public ExecutorResult service() {
        Runtime rt = Runtime.getRuntime();
        Process p1 = null;
        String[] command1 = {BaseConstant.JDK_PATH + "\\javac.exe", "-encoding", "utf-8", sourceFilePath};
        try {
            p1 = rt.exec(command1);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("执行javac命令出错！");
        }
        return ProcessUtil.getSystemOut(p1);
    }
}
