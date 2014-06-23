package com.project.eventmanagerapp;

import java.util.ArrayList;
import org.json.*;
import android.graphics.Color;
import android.util.Log;

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
			
			returnList.add(new PlanningEvent(id, title, startTime, endTime, stage, description));
		}
		
		return returnList;
	}
	
	public static ArrayList<MapShape> decodeMapShapesJSon(String fullJSonString) throws JSONException{
		ArrayList<MapShape> returnList = new ArrayList<MapShape>();
		
		JSONObject obj = new JSONObject(fullJSonString);
		
		//System.out.println(obj.getJSONObject("results"))
		JSONArray arr = obj.getJSONArray("shapes");
		for (int i = 0; i < arr.length(); i++){
			JSONObject currentShape = arr.getJSONObject(i);
			int width = currentShape.getInt("width");
			JSONArray latis = currentShape.getJSONArray("latitudes");
			JSONArray longis = currentShape.getJSONArray("longitudes");
			JSONObject JScolor = currentShape.getJSONObject("color");
			JSONObject JSstroke = currentShape.getJSONObject("stroke");
			
			int pointCount = latis.length();
			float[][] points = new float[pointCount][2];
			for(int c = 0; c < pointCount; c++){
				points[c][0] = (float) latis.getDouble(c);
				points[c][1] = (float) longis.getDouble(c);
			}
			
			int color = Color.argb(JScolor.getInt("a"), JScolor.getInt("r"), JScolor.getInt("g"), JScolor.getInt("b"));
			int stroke = Color.argb(JSstroke.getInt("a"), JSstroke.getInt("r"), JSstroke.getInt("g"), JSstroke.getInt("b"));
			
			returnList.add(new MapShape(width, points, color, stroke));
		}
		
		return returnList;
	}
	
	public static ArrayList<MapImage> decodeMapImageJSon(String fullJSonString) throws JSONException{
		ArrayList<MapImage> returnList = new ArrayList<MapImage>();
		
		JSONObject obj = new JSONObject(fullJSonString);
		
		//System.out.println(obj.getJSONObject("results"))
		JSONArray arr = obj.getJSONArray("images");
		for (int i = 0; i < arr.length(); i++){
			JSONObject currentImage = arr.getJSONObject(i);
			int width = currentImage.getInt("width");
			float rotation = (float) currentImage.getDouble("rotation");
			String imageName = currentImage.getString("imagename");
			float latitude = (float) currentImage.getDouble("latitude");
			float longitude = (float) currentImage.getDouble("longitude");
			
			returnList.add(new MapImage(width, rotation, imageName, latitude, longitude));
		}
		
		return returnList;
	}
	
	public static GregorianCalendar convertStringToGregorianCalendar(String inDate) throws NumberFormatException{
		int year = Integer.parseInt(inDate.substring(0, 4));
		int month = Integer.parseInt(inDate.substring(5, 7))-1;
		int day = Integer.parseInt(inDate.substring(8, 10));
		int hour = Integer.parseInt(inDate.substring(11, 13));
		int min = Integer.parseInt(inDate.substring(14, 16));
		int sec = Integer.parseInt(inDate.substring(17, 19));
		
		GregorianCalendar returnVal = new GregorianCalendar();
		
		returnVal.set(year, month, day, hour, min, sec);
		return returnVal;
	}
}






