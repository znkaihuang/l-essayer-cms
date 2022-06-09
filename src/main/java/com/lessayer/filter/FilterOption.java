package com.lessayer.filter;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class FilterOption {
	
	protected String fieldName;
	protected String field;
	protected boolean selected;
	protected boolean hasChildren;
	protected Integer level;
	protected Integer order;
	public FilterOption parentOption;
	public List<FilterOption> childrenOptions;
	
	public FilterOption(FilterOption parent, String fieldName, String field, Integer order) {
		this.parentOption = parent;
		this.fieldName = fieldName;
		this.field = field;
		this.selected = false;
		this.level = 0;
		this.order = order;
	}
	
	public abstract void addChildOption(FilterOption child);
	
	public abstract void updateParentSelected();
	
	public void setSelected(boolean selected) {
		this.selected = selected;
		updateParentSelected();
	}
}
