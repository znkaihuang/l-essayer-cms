package com.lessayer.converter;

public abstract class ContentConverter {
	
	public static enum ConvertType {
		HTML, Markdown
	}
	
	public static ContentConverter getInstance(ConvertType convertFrom, ConvertType convertTo) {
		
		if (convertFrom.equals(ConvertType.Markdown) && convertTo.equals(ConvertType.HTML)) {
			return new MarkdownConverter();
		}
		else {
			throw new IllegalArgumentException();
		}
	}
	
	public abstract String convertContentToHTMLString(String content);
	
}
