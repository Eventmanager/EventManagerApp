package com.project.eventmanagerapp;

import java.util.GregorianCalendar;

//Newsitems are to be retrieved from a server and displayed in the news activity
public class NewsItem {
	private String title;
	private int id;
	private GregorianCalendar date;
	private String text;
	
	public NewsItem(String _title, int _id, GregorianCalendar _date, String _text){
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

	public int getId() {
		return id;
	}

	public GregorianCalendar getDate() {
		return date;
	}

	public void setDate(GregorianCalendar date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
