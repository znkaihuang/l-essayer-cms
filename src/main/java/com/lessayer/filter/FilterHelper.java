package com.lessayer.filter;

import java.util.ArrayDeque;

import lombok.Getter;
import lombok.Setter;

public class FilterHelper {
	
	@Getter
	public FilterOption filter;
	
	@Getter
	@Setter
	private String prevFilterSelect;
	
	public FilterHelper(FilterOption filter) {
		this.filter = filter;
	}
	
	public FilterHelper(String[] sections, String[] options, Integer[] optionNumPerSection) {
		this.filter = FilterHelper.constructFilter(sections, options, optionNumPerSection);
	}
	
	public static FilterOption constructFilter(String[] sections, String[] options, Integer[] optionNumPerSection) {
		FilterOption filter = new FilterSection(null, "filter", 0);
		
		for (int index = 0; index < sections.length; index++) {
			FilterOption filterSection = new FilterSection(filter, sections[index], index);
			filter.addChildOption(filterSection);
		}
		
		Integer[] sum = new Integer[optionNumPerSection.length];
		sum[0] = 0;
		for (int index = 1; index < optionNumPerSection.length; index++) {
			sum[index] = sum[index - 1] + optionNumPerSection[index - 1];
		}
		
		for (int index = 0; index < optionNumPerSection.length; index++) {
			FilterOption filterSection = filter.getChildrenOptions().get(index);
			for (int count = 0; count < optionNumPerSection[index]; count++) {
				FilterOption option = new FilterChildOption(filterSection, options[sum[index] + count], count);
				filterSection.addChildOption(option);
			}
		}
		
		return filter;
	}
	
	public void updateFilter(String filterOptionSelectString) {
		String[] prevSelectResult = prevFilterSelect.split(",");
		String[] selectResult = filterOptionSelectString.split(",");
		
		for (String orderString : prevSelectResult) {
			String[] orders = orderString.split("-");
			filter.getChildrenOptions().get(Integer.valueOf(orders[0])).getChildrenOptions().get(Integer.valueOf(orders[1])).setSelected(false);
		}
		
		for (String orderString : selectResult) {
			String[] orders = orderString.split("-");
			filter.getChildrenOptions().get(Integer.valueOf(orders[0])).getChildrenOptions().get(Integer.valueOf(orders[1])).setSelected(true);
		}
	}
	
	public void clearFilter() {
		ArrayDeque<FilterOption> dequeue = new ArrayDeque<>();
		dequeue.add(filter);
		
		while (!dequeue.isEmpty()) {
			FilterOption filterOption = dequeue.poll();			
			if (filterOption.isHasChildren()) {
				for (FilterOption child : filterOption.getChildrenOptions()) {
						dequeue.addFirst(child);
				}
			}
			else {
				filterOption.setSelected(false);
			}
		}
	}
	
}
