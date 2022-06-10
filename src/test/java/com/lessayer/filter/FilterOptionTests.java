package com.lessayer.filter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

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
	
	@Test
	public void createFilterQueryObjectTest() {
		FilterOption filter = createFilterOptions();
		filter.getChildrenOptions().get(0).getChildrenOptions().get(1).setSelected(true);
		filter.getChildrenOptions().get(0).getChildrenOptions().get(2).setSelected(true);
		filter.getChildrenOptions().get(2).getChildrenOptions().get(0).setSelected(true);
		
		List<FilterQueryObject> filterQueryObjects = new ArrayList<>();
		
		FilterQueryObject queryObject1 = new FilterQueryObject(filter.getChildrenOptions().get(0).getFieldName());
		queryObject1.addQueryOption(filter.getChildrenOptions().get(0).getChildrenOptions().get(1).getFieldName());
		queryObject1.addQueryOption(filter.getChildrenOptions().get(0).getChildrenOptions().get(2).getFieldName());
		
		FilterQueryObject queryObject2 = new FilterQueryObject(filter.getChildrenOptions().get(2).getFieldName());
		queryObject2.addQueryOption(filter.getChildrenOptions().get(2).getChildrenOptions().get(0).getFieldName());
		
		filterQueryObjects.add(queryObject1);
		filterQueryObjects.add(queryObject2);
		
		printFilterQueryList(filterQueryObjects);
	}
	
	@Test
	public void filterHelperReturnQueryObjectTest() {
		String[] sections = {"Language", "Video Length", "Subtitle"};
		String[] options = {
				"English", "French", "Chinese",
				"0-10 minutes", "11-30 minutes", "31-60 minutes", "More than 1 hour",
				"With subtitle", "Without subtitle"
		};
		Integer[] optionNumPerSection = {3, 4, 2};
		FilterHelper filterHelper = new FilterHelper(sections, options, optionNumPerSection);
		filterHelper.setPrevFilterSelect("0-0");
		filterHelper.updateFilter("0-2,0-1,2-0");
		
		printFilterQueryList(filterHelper.getFilterQueryList());
	}
	
	private FilterOption createFilterOptions() {
		FilterOption filter = new FilterSection(null, "Top", 0);
		
		FilterOption section1 = new FilterSection(filter, "Section 1", 0);
		FilterOption section2 = new FilterSection(filter, "Section 2", 1);
		FilterOption section3 = new FilterSection(filter, "Section 3", 2);
		
		FilterOption filterOPtion1_1 = new FilterChildOption(section1, "Option 1-1", 0);
		FilterOption filterOPtion1_2 = new FilterChildOption(section1, "Option 1-2", 1);
		FilterOption filterOPtion1_3 = new FilterChildOption(section1, "Option 1-3", 2);

		FilterOption filterOPtion2_1 = new FilterChildOption(section2, "Option 2-1", 0);
		FilterOption filterOPtion2_2 = new FilterChildOption(section2, "Option 2-2", 1);
		FilterOption filterOPtion2_3 = new FilterChildOption(section2, "Option 2-3", 2);
		FilterOption filterOPtion2_4 = new FilterChildOption(section2, "Option 2-4", 3);
		

		FilterOption filterOPtion3_1 = new FilterChildOption(section3, "Option 3-1", 0);
		FilterOption filterOPtion3_2 = new FilterChildOption(section3, "Option 3-2", 1);
		
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
	
	private void printFilterQueryList(List<FilterQueryObject> queryList) {
		for (FilterQueryObject queryObject : queryList) {
			System.out.println("[" + queryObject.getSectionName() + "]");
			for (String queryOption : queryObject.getQueryOptions()) {
				System.out.print(queryOption + " ");
			}
			System.out.println();
		}
	}
}
