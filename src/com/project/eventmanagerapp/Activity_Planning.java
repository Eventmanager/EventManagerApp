package com.project.eventmanagerapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.GregorianCalendar;
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
	List<ArrayList<TextView>> textList;
	SharedPreferences sharedPrefs;
	JSONArray savedEvents = null;
	final Context context = this;
	ArrayList<ArrayList<PlanningEvent>> planninginfo = new ArrayList<ArrayList<PlanningEvent>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_planning);
		
		int eventstart = 20;
		int eventend = 2;
		
	    LinearLayout mainlinear= (LinearLayout) findViewById(R.id.planninglayout);
	    
	    
		try{
			planninginfo = PlanningManager.getInstance(context).getPlanning();
		}
		catch(ConcurrentModificationException e){
			e.printStackTrace();
		}
		
		linear = new LinearLayout[planninginfo.size()];
		textList = new ArrayList<ArrayList<TextView>>();

		for(int i=0;i<linear.length;i++)
		{
			linear[i] = new LinearLayout(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, pxtodp(60));
			mainlinear.addView(linear[i], params);
			linear[i].setOrientation(LinearLayout.HORIZONTAL);
			
			textList.add(i,new ArrayList<TextView>());
		
		
			TextView [] txt =new TextView[planninginfo.get(i).size()];
			
	
			for(int j=0;j<txt.length;j++)
			{
				TextView divider = new TextView(Activity_Planning.this);
				
				LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(pxtodp(2), LayoutParams.MATCH_PARENT);
			    llp.setMargins(pxtodp(5), 0, pxtodp(5), 0); // llp.setMargins(left, top, right, bottom);
			    divider.setLayoutParams(llp);
			    
				divider.setBackgroundColor(Color.GRAY);
				
				txt[j]=new TextView(Activity_Planning.this);
				txt[j].setText(planninginfo.get(i).get(j).getTitle());
				//Log.d("currduration", Float.toString(getDuration(planninginfo.get(i).get(j))));
				txt[j].setLayoutParams(new LayoutParams(pxtodp(100*getDuration(planninginfo.get(i).get(j))),LayoutParams.MATCH_PARENT));
				txt[j].setGravity(Gravity.CENTER);
				txt[j].setLines(1);
				txt[j].setSingleLine(true);
				txt[j].setEllipsize(TruncateAt.END);
				txt[j].setTextSize(20);
				txt[j].setOnClickListener(new EventClickListener(i,j,planninginfo.get(i).get(j)));
				
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
		//context.getSharedPreferences("EventPrefs", 0).edit().clear().commit(); // Removes all preferences.
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
			catch(IndexOutOfBoundsException e)
			{
				e.printStackTrace();
			}
			
		}
	}
	
	private float getDuration(PlanningEvent e){
		GregorianCalendar start = e.getStartTime();
		GregorianCalendar end = e.getEndTime();
		
		float startHour = start.get(Calendar.HOUR_OF_DAY)+start.get(Calendar.MINUTE)/60;
		float endHour = end.get(Calendar.HOUR_OF_DAY)+end.get(Calendar.MINUTE)/60;
		
		if(endHour < startHour)
			endHour+=24;
		
		float diff = endHour-startHour;
		return diff;
		
	}
	
	private int pxtodp(int px){
		return (int)(getResources().getDisplayMetrics().density*px);
	}
	private int pxtodp(float px){
		return (int)(getResources().getDisplayMetrics().density*px);
	}
	
	public class EventClickListener implements OnClickListener
	   {

	     int podium;
	     int event;
	     PlanningEvent e;
	     public EventClickListener(int p,int e,PlanningEvent n) {
	          this.podium = p;
	          this.event = e;
	          this.e = n;
	     }

	     @Override
	     public void onClick(View v)
	     {
	    	 Intent newpage = new Intent("com.project.eventmanagerapp.Activity_Planninginfo");		
	    	 newpage.putExtra("podium_id", this.podium);
	    	 newpage.putExtra("event_id", this.event);
	    	 newpage.putExtra("planning_event",this.e);
	    	 startActivity(newpage);
	     }

	  };
}