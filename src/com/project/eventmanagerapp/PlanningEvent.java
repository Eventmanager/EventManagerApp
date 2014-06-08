package com.project.eventmanagerapp;

import java.io.Serializable;
import java.util.GregorianCalendar;

@SuppressWarnings("serial")
public class PlanningEvent implements Serializable{
	private String id;
	private String title;
	private GregorianCalendar startTime;
	private GregorianCalendar endTime;
	private String stage;
	private String description;
	
	public PlanningEvent(String _id, String _title, GregorianCalendar _startTime, GregorianCalendar _endTime, String _stage, String _description) {
		id = _id;
		title = _title;
		startTime = _startTime;
		endTime = _endTime;
		stage = _stage;
		description = _description;
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
	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
