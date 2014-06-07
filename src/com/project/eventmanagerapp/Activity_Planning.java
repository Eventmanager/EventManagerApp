package com.project.eventmanagerapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class Activity_Planning extends Activity {
	
	LinearLayout [] linear;
	List<List> textList;
	SharedPreferences sharedPrefs;
	JSONArray savedEvents = null;
	final Context context = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_planning);
		
		int eventstart = 20;
		int eventend = 2;
		
	    LinearLayout mainlinear= (LinearLayout) findViewById(R.id.planninglayout);
	    
	    Event[][] planninginfo = new Event[][]{	{new Event("Red Hot Chili Peppers",0,1.2f),new Event("Avenged Sevenfold",1.2f,2.2f),new Event("Avicii",2.2f,3.5f)},
	    										{new Event("Coldplay",0,1.5f),new Event("Above and Beyond",1.5f,2.5f),new Event("Andrew Rayel",2.5f,3.8f)},
	    										{new Event("Sonata Arctica",0,1f), new Event("Justin Bieber",1f,1.5f), new Event("Pharrell Williams",1.5f,2.4f), new Event("Afrojack",2.4f,3.5f)}};
	    
	    
		linear = new LinearLayout[planninginfo.length];
		textList = new ArrayList<List>();
		
		ArrayList<ArrayList<PlanningEvent>> temp = PlanningManager.getInstance(context).getPlanning();
		Log.d("Eerste event", temp.get(0).get(0).getTitle());
		Log.d("Tweede event", temp.get(0).get(1).getTitle());

		for(int i=0;i<linear.length;i++)
		{
			linear[i] = new LinearLayout(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, pxtodp(60));
			mainlinear.addView(linear[i], params);
			linear[i].setOrientation(LinearLayout.HORIZONTAL);
			
			textList.add(i,new ArrayList<TextView>());
		
		
			TextView [] txt =new TextView[planninginfo[i].length];
			
	
			for(int j=0;j<txt.length;j++)
			{
				TextView divider = new TextView(Activity_Planning.this);
				
				LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(pxtodp(2), LayoutParams.MATCH_PARENT);
			    llp.setMargins(pxtodp(5), 0, pxtodp(5), 0); // llp.setMargins(left, top, right, bottom);
			    divider.setLayoutParams(llp);
			    
				divider.setBackgroundColor(Color.GRAY);
				
				txt[j]=new TextView(Activity_Planning.this);
				txt[j].setText(planninginfo[i][j].getName());
				txt[j].setLayoutParams(new LayoutParams(pxtodp(100*planninginfo[i][j].getDuration()),LayoutParams.MATCH_PARENT));
				txt[j].setGravity(Gravity.CENTER);
				txt[j].setLines(1);
				txt[j].setSingleLine(true);
				txt[j].setEllipsize(TruncateAt.END);
				txt[j].setTextSize(20);
				txt[j].setOnClickListener(new EventClickListener(i,j,planninginfo[i][j].getName()));
				
				textList.get(i).add(txt[j]);
				
				linear[i].addView(txt[j]);
				if(j != txt.length-1)
					linear[i].addView(divider);
			}
		}
		
		LinearLayout li = new LinearLayout(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, pxtodp(60));
		mainlinear.addView(li, params);
		li.setOrientation(LinearLayout.HORIZONTAL);
		
		eventend +=24;
		
		for(int curtime = eventstart;curtime <= eventend;curtime++)
		{
			if(curtime >= 24)
			{
				curtime = 0;
				eventend -=24;
			}
			
			TextView time = new TextView(Activity_Planning.this);
			time.setText(curtime+":00");
			LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		    if(curtime != eventstart)
		    	llp.setMargins(pxtodp(100), 0, 0, 0); // llp.setMargins(left, top, right, bottom);
		    time.setLayoutParams(llp);
			li.addView(time);
		}
		
		
	}
	
	@Override
	protected void onResume(){
		super.onResume();
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
		
		for(List l : textList){
			for(Object v : l){
				((TextView)v).setBackgroundColor(Color.WHITE);
			}
		}
		
		for(int i=0;i<savedEvents.length();i++)
		{
			try {
				int podium = ((JSONObject) savedEvents.get(i)).getInt("podium");
				int event = ((JSONObject) savedEvents.get(i)).getInt("event");
				((TextView) textList.get(podium).get(event)).setBackgroundColor(Color.RED);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private int pxtodp(int px){
		return (int)(getResources().getDisplayMetrics().density*px);
	}
	private int pxtodp(float px){
		return (int)(getResources().getDisplayMetrics().density*px);
	}
	
	private class Event{
		
		String name = "";
		float start = 0;
		float end = 0;

		public Event(String n, float s, float e){
			this.name = n;
			this.start = s;
			this.end = e;
		}
		
		private void setName(String n){
			this.name = n;
		}
		private void setStart(float s){
			this.start = s;
		}
		private void setEnd(float e){
			this.end = e;
		}
		private String getName(){
			return this.name;
		}
		private float getStart(){
			return this.start;
		}
		private float getEnd(){
			return this.end;
		}
		private float getDuration(){
			return this.end-this.start;
		}
	}
	
	public class EventClickListener implements OnClickListener
	   {

	     int podium;
	     int event;
	     String name;
	     public EventClickListener(int p,int e,String n) {
	          this.podium = p;
	          this.event = e;
	          this.name = n;
	     }

	     @Override
	     public void onClick(View v)
	     {
	    	 Intent newpage = new Intent("com.project.eventmanagerapp.Activity_Planninginfo");		
	    	 newpage.putExtra("podium_id", this.podium);
	    	 newpage.putExtra("event_id", this.event);
	    	 newpage.putExtra("event_name",this.name);
	    	 startActivity(newpage);
	     }

	  };
}