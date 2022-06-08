package com.lessayer.entity;

public enum Language {
	EN("English"), FR("French"), ZH_TW("Chinese");
	
	private final String language;
	
	Language(String language) {
		this.language = language;
	}
	
	public String getLanguageName() {
		return language;
	}
}
