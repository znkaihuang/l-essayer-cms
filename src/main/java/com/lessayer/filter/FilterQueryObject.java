package com.lessayer.filter;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class FilterQueryObject {
	
	private String sectionName;
	private List<String> queryOptions = new ArrayList<>();
	
	public FilterQueryObject(String sectionName) {
		this.sectionName = sectionName;
	}
	
	public void addQueryOption(String queryOption) {
		this.queryOptions.add(queryOption);
	}
}
