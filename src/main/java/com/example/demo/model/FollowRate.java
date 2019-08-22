package com.example.demo.model;

import java.util.Date;

public class FollowRate {

	private Integer customerSumCount;
	private Integer customerFollowCount;
	private Double followRate;
	private Date date;

	public Integer getCustomerSumCount() {
		return customerSumCount;
	}

	public void setCustomerSumCount(Integer customerSumCount) {
		this.customerSumCount = customerSumCount;
	}

	public Integer getCustomerFollowCount() {
		return customerFollowCount;
	}

	public void setCustomerFollowCount(Integer customerFollowCount) {
		this.customerFollowCount = customerFollowCount;
	}

	public Double getFollowRate() {
		return followRate;
	}

	public void setFollowRate(Double followRate) {
		this.followRate = followRate;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
