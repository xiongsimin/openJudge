package com.openJudge.openJudge.entity;

import java.io.Serializable;

/**
 * @Author aries
 * @Data 2021-02-15
 * @Description
 */
public class JudgeRequest implements Serializable {
    //编程语言
    private String language;
    //阶段  (编译/运行)
    private String stage;
    //源代码内容
    private String code;

    public JudgeRequest(String language, String stage, String code) {
        this.language = language;
        this.stage = stage;
        this.code = code;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "JudgeRequest{" +
                "language='" + language + '\'' +
                ", stage='" + stage + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
