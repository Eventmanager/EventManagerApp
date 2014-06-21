package com.project.eventmanagerapp;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils.TruncateAt;
import android.text.format.DateFormat;
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
	PlanningEvent selectedEvent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_planninginfo);
		Intent myIntent = getIntent();
		selectedEvent = (PlanningEvent)myIntent.getSerializableExtra("planning_event");
		
		Format formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		
		((TextView)findViewById(R.id.info_event_title)).setText(selectedEvent.getTitle());
		((TextView)findViewById(R.id.info_event_start)).setText(Html.fromHtml("<b>Start date</b>: "+formatter.format(selectedEvent.getStartTime().getTime())));
		((TextView)findViewById(R.id.info_event_end)).setText(Html.fromHtml("<b>End date</b>: "+formatter.format(selectedEvent.getEndTime().getTime())));
		((TextView)findViewById(R.id.info_event_podium)).setText(Html.fromHtml("<b>Stage</b>: "+selectedEvent.getStage()));
		((TextView)findViewById(R.id.info_event_description)).setText(selectedEvent.getDescription());
		
		sharedPrefs = this.getSharedPreferences("EventPrefs",MODE_PRIVATE);
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
		
		final Button removeButton = ((Button) findViewById(R.id.button_remove_event));
		final Button saveButton = ((Button) findViewById(R.id.button_save_event));

		saveButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				saveEvent(selectedEvent.getId());
				savedEvents = listToJson(savedEventsList);
				Editor editor = sharedPrefs.edit();
				editor.putString("savedevents",savedEvents.toString());
				editor.commit();
				saveButton.setVisibility(View.GONE);
				removeButton.setVisibility(View.VISIBLE);
			}
		});
		
		removeButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				removeEvent(selectedEvent.getId());
				savedEvents = listToJson(savedEventsList);
				Editor editor = sharedPrefs.edit();
				editor.putString("savedevents",savedEvents.toString());
				editor.commit();
				removeButton.setVisibility(View.GONE);
				saveButton.setVisibility(View.VISIBLE);
			}
		});
		
		try{
			if(isEventSaved(selectedEvent.getId()))
				removeButton.setVisibility(View.VISIBLE);
			else
				saveButton.setVisibility(View.VISIBLE);
		}
		catch(JSONException e){
			Log.d("Exception", "Woops, json exception for the buttons");
		}
		
		
	}
	
	private void saveEvent(String id){
		try{
			if(!isEventSaved(id))
				savedEventsList.add(new JSONObject().put("id", id));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	private void removeEvent(String id){
		try {
			for(int i=0;i<savedEventsList.size();i++)
				if(savedEventsList.get(i).getString("id").equals(id))
			      savedEventsList.remove(i);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean isEventSaved(String id) throws JSONException{
		for(JSONObject i : savedEventsList)
			if(i.getString("id").equals(id))
				return true;
		return false;
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