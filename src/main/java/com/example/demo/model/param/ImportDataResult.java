package com.example.demo.model.param;

import java.util.List;

public class ImportDataResult {
	
	/**
	 * 总数
	 */
	private int total;

	private List<List<String>> result;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<List<String>> getResult() {
		return result;
	}

	public void setResult(List<List<String>> result) {
		this.result = result;
	}
	
}
