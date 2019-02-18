package com.example.demo.model;

import java.util.Date;

public class TaskAnalysis {

	private Integer passTodayCount;
	private Integer todayUnDealCount;
	private Integer todayDealCount;
	private Integer passRemoveCount;
	
	private Integer total;
	private Double percent;
	
	private Date date;
	public Integer getPassTodayCount() {
		return passTodayCount;
	}
	public void setPassTodayCount(Integer passTodayCount) {
		this.passTodayCount = passTodayCount;
	}
	public Integer getTodayUnDealCount() {
		return todayUnDealCount;
	}
	public void setTodayUnDealCount(Integer todayUnDealCount) {
		this.todayUnDealCount = todayUnDealCount;
	}
	public Integer getTodayDealCount() {
		return todayDealCount;
	}
	public void setTodayDealCount(Integer todayDealCount) {
		this.todayDealCount = todayDealCount;
	}
	public Integer getPassRemoveCount() {
		return passRemoveCount;
	}
	public void setPassRemoveCount(Integer passRemoveCount) {
		this.passRemoveCount = passRemoveCount;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Double getPercent() {
		return percent;
	}
	public void setPercent(Double percent) {
		this.percent = percent;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
