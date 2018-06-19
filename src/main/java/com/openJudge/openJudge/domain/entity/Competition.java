package com.openJudge.openJudge.domain.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Competition {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String title;
	private int handler_character;//发布者身份 0为超级管理员；1为管理员
	private Long handler_id;//发布者id
	private Timestamp time;//创建时间
	private Timestamp start_time;
	private Timestamp end_time;
	private int Type;//类型：0比赛 1练习（练习和比赛没有本质区别，但权限不同，超级管理员可以管理比赛，但管理员只能管理练习，并且练习没有开始时间和结束时间）
	private int state;//状态 0：未发布 1：已发布但未开始 2：已发布且正在进行 3：已结束
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getHandler_character() {
		return handler_character;
	}
	public void setHandler_character(int handler_character) {
		this.handler_character = handler_character;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public Timestamp getStart_time() {
		return start_time;
	}
	public void setStart_time(Timestamp start_time) {
		this.start_time = start_time;
	}
	public Timestamp getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Timestamp end_time) {
		this.end_time = end_time;
	}
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getType() {
		return Type;
	}
	public void setType(int type) {
		Type = type;
	}
	public Long getHandler_id() {
		return handler_id;
	}
	public void setHandler_id(Long handler_id) {
		this.handler_id = handler_id;
	}
	@Override
	public String toString() {
		return "Competition [id=" + id + ", title=" + title + ", handler_character=" + handler_character
				+ ", handler_id=" + handler_id + ", time=" + time + ", start_time=" + start_time + ", end_time="
				+ end_time + ", Type=" + Type + ", state=" + state + "]";
	}
	
}
