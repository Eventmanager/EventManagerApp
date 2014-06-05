package com.project.eventmanagerapp;

import java.util.GregorianCalendar;

public class PlanningEvent {
	private String id;
	private String title;
	private GregorianCalendar startTime;
	private GregorianCalendar endTime;
	private String stage;
	
	public PlanningEvent(String _id, String _title, GregorianCalendar _startTime, GregorianCalendar _endTime, String _stage) {
		id = _id;
		title = _title;
		startTime = _startTime;
		endTime = _endTime;
		stage = _stage;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public GregorianCalendar getStartTime() {
		return startTime;
	}

	public void setStartTime(GregorianCalendar startTime) {
		this.startTime = startTime;
	}

	public GregorianCalendar getEndTime() {
		return endTime;
	}

	public void setEndTime(GregorianCalendar endTime) {
		this.endTime = endTime;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getId() {
		return id;
	}
}
