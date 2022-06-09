package com.lessayer.filter;

public class FilterChildOption extends FilterOption {

	public FilterChildOption(FilterOption parent, String fieldName) {
		super(parent, fieldName);
		this.level = parent.level + 1;
		this.hasChildren = false;
	}

	@Override
	public void addChildOption(FilterOption child) {
		
	}

	@Override
	public void updateParentSelected() {
		parentOption.setSelected(this.selected);
	}
	
}
