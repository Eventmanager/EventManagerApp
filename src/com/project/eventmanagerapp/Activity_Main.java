package com.project.eventmanagerapp;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

public class Activity_Main extends Activity {
	
	ArrayList<ArrayList<PlanningEvent>> planninginfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		NewsSource.getInstance(this);//Starts the news update loop
		
		planninginfo = PlanningManager.getInstance(this).getPlanning();
		
		checkPlanning(planninginfo);
		
		initButtons();
	}
	
	private void initButtons(){
		Button btn_map = (Button) findViewById(R.id.btn_map);
		Button btn_planning = (Button) findViewById(R.id.btn_planning);
		Button btn_news = (Button) findViewById(R.id.btn_news);
		Button btn_options = (Button) findViewById(R.id.btn_options);
		
		btn_map.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				startActivity(new Intent("com.project.eventmanagerapp.Activity_Map"));
			}
		});
		
		btn_planning.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				startActivity(new Intent("com.project.eventmanagerapp.Activity_Planning"));
			}
		});
		
		btn_news.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				startActivity(new Intent("com.project.eventmanagerapp.Activity_News"));
			}
		});
		
		btn_options.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				startActivity(new Intent("com.project.eventmanagerapp.Activity_Options"));				
			}
		});
	}
	
	private void checkPlanning(ArrayList<ArrayList<PlanningEvent>> planning){
		SharedPreferences sharedPrefs = this.getSharedPreferences("EventPrefs",MODE_PRIVATE);
		JSONArray savedEvents = new JSONArray();
		
		if(sharedPrefs.contains("savedevents"))
		{
			try {
				savedEvents = new JSONArray(sharedPrefs.getString("savedevents", null));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		ArrayList<JSONObject> savedEventsList = jsonToList(savedEvents);
		
		//GregorianCalendar current_date = new GregorianCalendar(2014,4,6,9,40,0); // Test date for Sonata Arctica event
		GregorianCalendar current_date = new GregorianCalendar();
		for(JSONObject i : savedEventsList)
		{
			String id;
			try {
				id = i.getString("id");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				id=null;
			}
			
			if(id == null)
				continue;
			
			for(ArrayList<PlanningEvent> l : planninginfo)
			{
				for(PlanningEvent e : l)
				{
					if(e.getId().equals(id))
					{
						if(e.getStartTime().getTimeInMillis() - current_date.getTimeInMillis() < 30 * 60 * 1000 && e.getStartTime().getTimeInMillis() - current_date.getTimeInMillis() > 0)
						{
							PushNotification.sendNotification(this, new Intent("com.project.eventmanagerapp.Activity_Planning"), "Upcoming event: "+e.getTitle(), "Go to the planning", 0, false);
						}
					}
				
				}
			}
		}
	}
	
	private ArrayList<JSONObject> jsonToList(JSONArray arr){
		ArrayList<JSONObject> tmp = new ArrayList<JSONObject>();
		for(int i=0;i<arr.length();i++)
			try {
				tmp.add((JSONObject) arr.get(i));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return tmp;
	}
}
