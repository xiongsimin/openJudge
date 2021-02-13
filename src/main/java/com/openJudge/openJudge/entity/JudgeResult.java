package com.openJudge.openJudge.entity;

import java.util.List;

/**
 * @Author aries
 * @Data 2021-02-08
 * @Description 封装执行代码测评后的结果
 */
public class JudgeResult {
    //评测状态  包括 ACCEPT | Runtime Error | Wrong Answer
    private String state;
    //失败用例
    private List<Sample> failedSamples;
    //编译错误信息
    private String compileErrorMsg;

    //测试用例通过率
    private float acceptRate;
    //

    public JudgeResult() {
    }

    public JudgeResult(String state, List<Sample> failedSamples, String compileErrorMsg) {
        this.state = state;
        this.failedSamples = failedSamples;
        this.compileErrorMsg = compileErrorMsg;
    }

    public JudgeResult(String state, List<Sample> failedSamples, String compileErrorMsg, float acceptRate) {
        this.state = state;
        this.failedSamples = failedSamples;
        this.compileErrorMsg = compileErrorMsg;
        this.acceptRate = acceptRate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Sample> getFailedSamples() {
        return failedSamples;
    }

    public void setFailedSamples(List<Sample> failedSamples) {
        this.failedSamples = failedSamples;
    }

    public String getCompileErrorMsg() {
        return compileErrorMsg;
    }

    public void setCompileErrorMsg(String compileErrorMsg) {
        this.compileErrorMsg = compileErrorMsg;
    }

    public float getAcceptRate() {
        return acceptRate;
    }

    public void setAcceptRate(float acceptRate) {
        this.acceptRate = acceptRate;
    }

    @Override
    public String toString() {
        return "JudgeResult{" +
                "state='" + state + '\'' +
                ", failedSamples=" + failedSamples +
                ", compileErrorMsg='" + compileErrorMsg + '\'' +
                ", acceptRate=" + acceptRate +
                '}';
    }
}
