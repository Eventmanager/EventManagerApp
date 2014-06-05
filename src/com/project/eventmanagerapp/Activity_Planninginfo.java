package com.project.eventmanagerapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class Activity_Planninginfo extends Activity {
	
	int current_podium;
	int current_event;
	JSONArray savedEvents = null;
	List<JSONObject> savedEventsList = new ArrayList<JSONObject>();
	SharedPreferences sharedPrefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_planninginfo);
		Intent myIntent = getIntent();
		current_podium = myIntent.getIntExtra("podium_id",0);
		current_event = myIntent.getIntExtra("event_id",0);

		TextView txt=new TextView(Activity_Planninginfo.this);
		txt.setText(myIntent.getStringExtra("event_name"));
		txt.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		LinearLayout mainlinear= (LinearLayout) findViewById(R.id.eventinfo_layout);
		mainlinear.addView(txt);
		
		sharedPrefs = getSharedPreferences("EventPrefs",MODE_PRIVATE);
		if(sharedPrefs.contains("savedevents"))
		{
			try {
				savedEvents = new JSONArray(sharedPrefs.getString("savedevents", null));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		else
			savedEvents = new JSONArray();
		
		savedEventsList = jsonToList(savedEvents);

		Log.d("String",savedEvents.toString());
		
		Button bt = new Button(this);
		bt.setText("Add event");
		bt.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		bt.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				saveEvent(current_podium,current_event);
				savedEvents = listToJson(savedEventsList);
				Editor editor = sharedPrefs.edit();
				editor.putString("savedevents",savedEvents.toString());
				editor.commit();
			}
		});
		
		Button bt2 = new Button(this);
		bt2.setText("Remove event");
		bt2.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		bt2.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				removeEvent(current_podium,current_event);
				savedEvents = listToJson(savedEventsList);
				Editor editor = sharedPrefs.edit();
				editor.putString("savedevents",savedEvents.toString());
				editor.commit();
			}
		});
		
		mainlinear.addView(bt);
		mainlinear.addView(bt2);
		
		
	}
	
	private void saveEvent(int podium, int event){
		try{
			for(JSONObject i : savedEventsList)
				if(i.getInt("podium") == podium && i.getInt("event") == event)
					return;
			savedEventsList.add(new JSONObject().put("podium", podium).put("event", event));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	private void removeEvent(int podium,int event){
		try {
			for(int i=0;i<savedEventsList.size();i++)
				if(savedEventsList.get(i).getInt("podium") == podium && savedEventsList.get(i).getInt("event") == event)
			      savedEventsList.remove(i);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private List jsonToList(JSONArray arr){
		List<JSONObject> tmp = new ArrayList<JSONObject>();
		for(int i=0;i<arr.length();i++)
			try {
				tmp.add((JSONObject) arr.get(i));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return tmp;
	}
	
	private JSONArray listToJson(List<JSONObject> l){
		JSONArray tmp = new JSONArray();
		for(JSONObject i: l)
			tmp.put(i);
		return tmp;
	}
	
}