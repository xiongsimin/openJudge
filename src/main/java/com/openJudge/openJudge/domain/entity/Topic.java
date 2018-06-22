package com.openJudge.openJudge.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Topic {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="topic_gen")
	@SequenceGenerator(initialValue=1001,allocationSize=1, name = "topic_gen")
	private Long id;//编号（从1001开始自增）
	private Long competition_id;//所属比赛（练习的id）
	private int number;//题目在比赛（练习）中的序号
	private Timestamp time;//创建时间
	private int handler_character;//创建人身份
	private Long handler_id;//创建人id
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
	private int try_people;//尝试人数
	private int pass_people;//通过人数
	private String test_data_path;//测试数据文件路径
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCompetition_id() {
		return competition_id;
	}
	public void setCompetition_id(Long competition_id) {
		this.competition_id = competition_id;
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
	public int getTry_people() {
		return try_people;
	}
	public void setTry_people(int try_people) {
		this.try_people = try_people;
	}
	public int getPass_people() {
		return pass_people;
	}
	public void setPass_people(int pass_people) {
		this.pass_people = pass_people;
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
	@Override
	public String toString() {
		return "Topic [id=" + id + ", competition_id=" + competition_id + ", number=" + number + ", time=" + time
				+ ", handler_character=" + handler_character + ", handler_id=" + handler_id + ", type=" + type
				+ ", time_limit=" + time_limit + ", memory_limit=" + memory_limit + ", title=" + title + ", content="
				+ content + ", input_intro=" + input_intro + ", output_intro=" + output_intro + ", input_sample="
				+ input_sample + ", output_sample=" + output_sample + ", info=" + info + ", try_people=" + try_people
				+ ", pass_people=" + pass_people + ", test_data_path=" + test_data_path + "]";
	}
}
