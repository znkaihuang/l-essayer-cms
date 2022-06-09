package com.lessayer.filter;

import java.util.ArrayDeque;

import org.junit.jupiter.api.Test;

public class FilterOptionTests {
	
	@Test
	public void printFilterOptionsTest() {
		FilterOption filter = createFilterOptions();
		printFilterOptions(filter);
	}
	
	@Test
	public void updateSelectedTest() {
		FilterOption filter = createFilterOptions();
		filter.getChildrenOptions().get(0).getChildrenOptions().get(1).setSelected(true);
		filter.getChildrenOptions().get(0).getChildrenOptions().get(2).setSelected(true);
		filter.getChildrenOptions().get(2).getChildrenOptions().get(0).setSelected(true);
		printFilterOptions(filter);
	}
	
	private FilterOption createFilterOptions() {
		FilterOption filter = new FilterSection(null, "Top");
		
		FilterOption section1 = new FilterSection(filter, "Section 1");
		FilterOption section2 = new FilterSection(filter, "Section 2");
		FilterOption section3 = new FilterSection(filter, "Section 3");
		
		FilterOption filterOPtion1_1 = new FilterChildOption(section1, "Option 1-1");
		FilterOption filterOPtion1_2 = new FilterChildOption(section1, "Option 1-2");
		FilterOption filterOPtion1_3 = new FilterChildOption(section1, "Option 1-3");

		FilterOption filterOPtion2_1 = new FilterChildOption(section2, "Option 2-1");
		FilterOption filterOPtion2_2 = new FilterChildOption(section2, "Option 2-2");
		FilterOption filterOPtion2_3 = new FilterChildOption(section2, "Option 2-3");
		FilterOption filterOPtion2_4 = new FilterChildOption(section2, "Option 2-4");
		

		FilterOption filterOPtion3_1 = new FilterChildOption(section3, "Option 3-1");
		FilterOption filterOPtion3_2 = new FilterChildOption(section3, "Option 3-2");
		
		filter.addChildOption(section1);
		filter.addChildOption(section2);
		filter.addChildOption(section3);
		
		section1.addChildOption(filterOPtion1_3);
		section1.addChildOption(filterOPtion1_2);
		section1.addChildOption(filterOPtion1_1);
		
		section2.addChildOption(filterOPtion2_4);
		section2.addChildOption(filterOPtion2_3);
		section2.addChildOption(filterOPtion2_2);
		section2.addChildOption(filterOPtion2_1);
		
		section3.addChildOption(filterOPtion3_2);
		section3.addChildOption(filterOPtion3_1);
		
		return filter;
	}

	private void printFilterOptions(FilterOption filter) {
		ArrayDeque<FilterOption> dequeue = new ArrayDeque<>();
		dequeue.add(filter);
		
		while (!dequeue.isEmpty()) {
			FilterOption filterOption = dequeue.pollLast();
			for (int i = 0; i < filterOption.getLevel(); i++) {
				System.out.print("  ");
			}
			System.out.println(filterOption.getFieldName() + " [" + filterOption.isSelected() + "]");
			
			if (filterOption.hasChildren) {
				for (FilterOption child : filterOption.getChildrenOptions()) {
					if (child.hasChildren) {
						dequeue.addFirst(child);
					}
					else {
						dequeue.addLast(child);
					}
				}
			}
		}
		
	}
}
