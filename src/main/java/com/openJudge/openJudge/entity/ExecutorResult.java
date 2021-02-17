package com.openJudge.openJudge.entity;

/**
 * @Author aries
 * @Data 2021-02-15
 * @Description 执行器执行结果封装
 */
public class ExecutorResult {
    private String result;
    private String errorMsg;

    public ExecutorResult() {
    }

    public ExecutorResult(String result, String errorMsg) {
        this.result = result;
        this.errorMsg = errorMsg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "ExecutorResult{" +
                "result='" + result + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
