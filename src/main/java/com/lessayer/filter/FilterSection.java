package com.lessayer.filter;

import java.util.ArrayList;

public class FilterSection extends FilterOption {

	public FilterSection(FilterOption parent, String fieldName) {
		super(parent, fieldName);
		this.childrenOptions = new ArrayList<>();
		this.hasChildren = true;
		if (parent != null) {
			this.level = parent.getLevel() + 1;
		}
	}

	@Override
	public void updateParentSelected() {
		if (parentOption != null) {
			parentOption.selected = this.selected;
		}
	}

	@Override
	public void addChildOption(FilterOption child) {
		childrenOptions.add(child);
	}

}
