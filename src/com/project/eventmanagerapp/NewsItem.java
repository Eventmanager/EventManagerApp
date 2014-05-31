package com.project.eventmanagerapp;

//Newsitems are to be retrieved from a server and displayed in the news activity
public class NewsItem {
	private String title;
	private String id;
	private String date; //Saved as string, not as actual useable date because it is for display purposes only.
	private String text;
	
	public NewsItem(String _title, String _id, String _date, String _text){
		title = _title;
		id = _id;
		date = _date;
		text = _text;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
