package com.openJudge.openJudge.entity;

import javax.persistence.*;

/**
 * @Author aries
 * @Data 2021-02-08
 */
@Entity
public class Sample {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String input;
    private String output;
    //用例是否存在输入
    private boolean haveInput;
    @Transient
    private String userOutput;
    @OneToOne
    private Topic topic;

    public Sample() {
    }

    public Sample(Long id, String input, String output, boolean haveInput, String userOutput, Topic topic) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.haveInput = haveInput;
        this.userOutput = userOutput;
        this.topic = topic;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public void setUserOutput(String userOutput) {
        this.userOutput = userOutput;
    }

    public String getUserOutput() {
        return userOutput;
    }

    public boolean isHaveInput() {
        return haveInput;
    }

    public void setHaveInput(boolean haveInput) {
        this.haveInput = haveInput;
    }

    @Override
    public String toString() {
        return "Sample{" +
                "id=" + id +
                ", input='" + input + '\'' +
                ", output='" + output + '\'' +
                ", haveInput=" + haveInput +
                ", userOutput='" + userOutput + '\'' +
                ", topic=" + topic +
                '}';
    }
}
