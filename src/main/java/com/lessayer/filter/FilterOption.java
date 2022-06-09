package com.lessayer.filter;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class FilterOption {
	
	protected String fieldName;
	protected boolean selected;
	protected boolean hasChildren;
	protected Integer level;
	protected FilterOption parentOption;
	protected List<FilterOption> childrenOptions;
	
	public FilterOption(FilterOption parent, String fieldName) {
		this.parentOption = parent;
		this.fieldName = fieldName;
		this.selected = false;
		this.level = 0;
	}
	
	public abstract void addChildOption(FilterOption child);
	
	public abstract void updateParentSelected();
	
	public void setSelected(boolean selected) {
		this.selected = selected;
		updateParentSelected();
	}
}
