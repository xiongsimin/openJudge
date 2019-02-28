package com.openJudge.openJudge.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Record {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Long user_id;//提交者的id
	private String state;
	private String errorMsg;
	private Timestamp submit_time;//提交时间
	private int time;//运行时间（s）
	private int memory;//占用内存（MB）
	@ManyToOne(optional=false)
	private Topic topic;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Timestamp getSubmit_time() {
		return submit_time;
	}
	public void setSubmit_time(Timestamp submit_time) {
		this.submit_time = submit_time;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getMemory() {
		return memory;
	}
	public void setMemory(int memory) {
		this.memory = memory;
	}
	public Topic getTopic() {
		return topic;
	}
	public void setTopic(Topic topic) {
		this.topic = topic;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	@Override
	public String toString() {
		return "Record [id=" + id + ", user_id=" + user_id + ", state=" + state + ", errorMsg=" + errorMsg
				+ ", submit_time=" + submit_time + ", time=" + time + ", memory=" + memory +  "]";
	}
	
}
