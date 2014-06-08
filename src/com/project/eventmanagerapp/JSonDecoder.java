package com.project.eventmanagerapp;

import java.util.ArrayList;
import org.json.*;

import java.util.GregorianCalendar;

public class JSonDecoder {
	
	public static ArrayList<NewsItem> decodeNewsJSon(String fullJSonString) throws JSONException{
		ArrayList<NewsItem> returnList = new ArrayList<NewsItem>();
		
		JSONObject obj = new JSONObject(fullJSonString);
		
		//System.out.println(obj.getJSONObject("results"))
		JSONArray arr = obj.getJSONArray("posts");
		for (int i = 0; i < arr.length(); i++){
			JSONObject currentPost = arr.getJSONObject(i);
			String title = currentPost.getString("title");
			String id = currentPost.getString("id");
			String date = currentPost.getString("date");
			String contents = currentPost.getString("contents");//content'S'
			
			returnList.add(new NewsItem(title, id, date, contents));
		}
		
		return returnList;
	}

	public static ArrayList<PlanningEvent> decodePlanningJSon(String fullJSonString) throws JSONException{
		ArrayList<PlanningEvent> returnList = new ArrayList<PlanningEvent>();
		
		JSONObject obj = new JSONObject(fullJSonString);
		
		//System.out.println(obj.getJSONObject("results"))
		JSONArray arr = obj.getJSONArray("events");
		for (int i = 0; i < arr.length(); i++){
			JSONObject currentPost = arr.getJSONObject(i);
			String id = currentPost.getString("id");
			String title = currentPost.getString("title");
			String startTimeString = currentPost.getString("starttime");
			String endTimeString = currentPost.getString("endtime");
			String stage = currentPost.getString("stage");
			String description = currentPost.getString("description");
			
			GregorianCalendar startTime = convertStringToGregorianCalendar(startTimeString);
			GregorianCalendar endTime = convertStringToGregorianCalendar(endTimeString);
			
			returnList.add(new PlanningEvent(id, title, startTime, endTime, stage, description)); //Description is currently an empty string. Replace it with a variable.
		}
		
		return returnList;
	}
	
	public static GregorianCalendar convertStringToGregorianCalendar(String inDate) throws NumberFormatException{
		int year = Integer.parseInt(inDate.substring(0, 4));
		int month = Integer.parseInt(inDate.substring(5, 7));
		int day = Integer.parseInt(inDate.substring(8, 10));
		int hour = Integer.parseInt(inDate.substring(11, 13));
		int min = Integer.parseInt(inDate.substring(14, 16));
		int sec = Integer.parseInt(inDate.substring(17, 19));
		
		GregorianCalendar returnVal = new GregorianCalendar();
		
		returnVal.set(year, month, day, hour, min, sec);
		return returnVal;
	}
}






