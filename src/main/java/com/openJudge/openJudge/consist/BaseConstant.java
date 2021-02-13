package com.openJudge.openJudge.consist;

/**
 * @Author aries
 * @Data 2021-02-07
 */
public class BaseConstant {
    public static final String JDK_PATH = "I:\\jdk1.8.0_171\\bin";
    public static final String JAVAC_EXE_PATH = JDK_PATH + "\\javac.exe";
    public static final String SOURCE_FILE_ROOT_PATH="F:\\openJudge_judge";

    public static final String JUDGE_RESULT_STATE_ACCEPT="ACCEPT";
    public static final String JUDGE_RESULT_STATE_RUNTIME_ERROR="Runtime Error";
    public static final String JUDGE_RESULT_STATE_WRONG_ANSWER="Wrong Answer";
    public enum type {JAVA, C_PLUS_PLUS, PYTHON}

    ;
}
