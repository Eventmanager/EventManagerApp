package com.project.eventmanagerapp;

//Newsitems are to be retrieved from a server and displayed in the news activity
public class NewsItem {
	private String title;
	private String text;
	
	public NewsItem(String _title, String _text){
		title = _title;
		text = _text;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
