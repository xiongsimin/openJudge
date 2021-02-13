package com.openJudge.openJudge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.context.annotation.Lazy;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//编号（从1001开始自增）
    //	private Long competition_id;//所属比赛（练习的id）//外键
    private int number;//题目在比赛（练习）中的序号
    private Timestamp time;//创建时间
    private int handler_character;//创建人身份
    private Long handler_id;//创建人id
    @Transient
    private String handler_name;//创建人名字
    private String type;//题目类型
    private int time_limit;//时间限制(秒)
    private int memory_limit;//内存限制(MB)
    private String title;//标题
    private String content;//题干
    private String input_intro;//输入说明
    private String output_intro;//输出说明
    private String input_sample;//样例（输入）
    private String output_sample;//样例（输出）
    private String info;//说明（数据范围、提示等）
    @Transient   //该注解含义为不在数据库表中生成该字段
    private int try_people_number;//尝试人数
    @Transient
    private int pass_people_number;//通过人数
    private String try_people;//记录尝试人的id
    private String pass_people;//记录通过的id
    @Transient
    private String rate;//通过率
    private String test_data_path;//测试数据文件路径
    @ManyToOne(optional = false)//一对多与多对一双向关系中，一是关系维护端，此处optional设为false，意为当关系维护段不存在时，被维护端返回的值也是null
    //不加注解JsonIgnore时会因为集成Redis时配置了jackson2JsonRedisSerializer导致报错（从缓存查询时报错），暂未找到其他解决方法
    @JsonIgnoreProperties(value = {"topics"})
    @JsonIgnore
    private Competition competition;
    @OneToMany(mappedBy = "topic")
    @JsonIgnoreProperties(value = {"topic"})
    @JsonIgnore
    private List<Record> records;

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public Long getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTime_limit() {
        return time_limit;
    }

    public void setTime_limit(int time_limit) {
        this.time_limit = time_limit;
    }

    public int getMemory_limit() {
        return memory_limit;
    }

    public void setMemory_limit(int memory_limit) {
        this.memory_limit = memory_limit;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getInput_intro() {
        return input_intro;
    }

    public void setInput_intro(String input_intro) {
        this.input_intro = input_intro;
    }

    public String getOutput_intro() {
        return output_intro;
    }

    public void setOutput_intro(String output_intro) {
        this.output_intro = output_intro;
    }

    public String getInput_sample() {
        return input_sample;
    }

    public void setInput_sample(String input_sample) {
        this.input_sample = input_sample;
    }

    public String getOutput_sample() {
        return output_sample;
    }

    public void setOutput_sample(String output_sample) {
        this.output_sample = output_sample;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTest_data_path() {
        return test_data_path;
    }

    public void setTest_data_path(String test_data_path) {
        this.test_data_path = test_data_path;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getHandler_character() {
        return handler_character;
    }

    public void setHandler_character(int handler_character) {
        this.handler_character = handler_character;
    }

    public Long getHandler_id() {
        return handler_id;
    }

    public void setHandler_id(Long handler_id) {
        this.handler_id = handler_id;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTry_people_number() {
        return try_people_number;
    }

    public void setTry_people_number(int try_people_number) {
        this.try_people_number = try_people_number;
    }

    public int getPass_people_number() {
        return pass_people_number;
    }

    public void setPass_people_number(int pass_people_number) {
        this.pass_people_number = pass_people_number;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public void setTry_people(String try_people) {
        this.try_people = try_people;
    }

    public void setPass_people(String pass_people) {
        this.pass_people = pass_people;
    }

    public String getTry_people() {
        return try_people;
    }

    public String getPass_people() {
        return pass_people;
    }

    public String getHandler_name() {
        return handler_name;
    }

    public void setHandler_name(String handler_name) {
        this.handler_name = handler_name;
    }

    @Override
    public String toString() {
        return "Topic [id=" + id + ", number=" + number + ", time=" + time + ", handler_character=" + handler_character
                + ", handler_id=" + handler_id + ", handler_name=" + handler_name + ", type=" + type + ", time_limit="
                + time_limit + ", memory_limit=" + memory_limit + ", title=" + title + ", content=" + content
                + ", input_intro=" + input_intro + ", output_intro=" + output_intro + ", input_sample=" + input_sample
                + ", output_sample=" + output_sample + ", info=" + info + ", try_people_number=" + try_people_number
                + ", pass_people_number=" + pass_people_number + ", try_people=" + try_people + ", pass_people="
                + pass_people + ", rate=" + rate + ", test_data_path=" + test_data_path + ", competition=" + competition
                + ", records=" + records + "]";
    }
}
