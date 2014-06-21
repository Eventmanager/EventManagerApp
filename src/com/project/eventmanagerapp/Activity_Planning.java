package com.project.eventmanagerapp;

import java.text.SimpleDateFormat;
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
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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

public class Activity_Planning extends FragmentActivity {
	
	LinearLayout [] linear;
	List<ArrayList<TextView>> textList;
	SharedPreferences sharedPrefs;
	JSONArray savedEvents = null;
	final Context context = this;
	ArrayList<ArrayList<PlanningEvent>> planninginfo = new ArrayList<ArrayList<PlanningEvent>>();
	GregorianCalendar eventstart = null,eventend = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_planning);
		
	    LinearLayout mainlinear= (LinearLayout) findViewById(R.id.planninglayout);
	    
		try{
			planninginfo = PlanningManager.getInstance(context).getPlanning();
		}
		catch(ConcurrentModificationException e){
			e.printStackTrace();
		}
		
		linear = new LinearLayout[planninginfo.size()];
		textList = new ArrayList<ArrayList<TextView>>();
		
		for(ArrayList<PlanningEvent> l : planninginfo)
			for(PlanningEvent e : l)
			{
				if(eventstart == null)
					eventstart = (GregorianCalendar) e.getStartTime().clone();
				else if(e.getStartTime().compareTo(eventstart) < 0)
					eventstart = (GregorianCalendar) e.getStartTime().clone();
				
				if(eventend == null)
					eventend = (GregorianCalendar) e.getEndTime().clone();
				else if(e.getEndTime().compareTo(eventend) > 0)
					eventend = (GregorianCalendar) e.getEndTime().clone();
			}

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
				txt[j].setLayoutParams(new LayoutParams(pxtodp(100f*getDuration(planninginfo.get(i).get(j))-12f),LayoutParams.MATCH_PARENT));
				txt[j].setGravity(Gravity.CENTER);
				txt[j].setLines(1);
				txt[j].setSingleLine(true);
				txt[j].setEllipsize(TruncateAt.END);
				txt[j].setTextSize(20);
				txt[j].setBackgroundColor(Color.LTGRAY);
				txt[j].setOnClickListener(new EventClickListener(planninginfo.get(i).get(j)));
				
				textList.get(i).add(txt[j]);
				
				if(j==0)
				{
					TextView empty = new TextView(Activity_Planning.this);
					empty.setLayoutParams(new LayoutParams(pxtodp(16f+100f*gregorianDifference(eventstart,planninginfo.get(i).get(j).getStartTime())),LayoutParams.MATCH_PARENT));
					linear[i].addView(empty);
					
					TextView div = new TextView(Activity_Planning.this);
				    div.setLayoutParams(llp);
					div.setBackgroundColor(Color.GRAY);
					linear[i].addView(div);
				}
				else
				{
					GregorianCalendar previous = (GregorianCalendar) planninginfo.get(i).get(j-1).getEndTime().clone();
					previous.add(Calendar.MINUTE,3);
					if(planninginfo.get(i).get(j).getStartTime().compareTo(previous) > 0)
					{
						TextView empty = new TextView(Activity_Planning.this);
						empty.setLayoutParams(new LayoutParams(pxtodp(-12f+100f*gregorianDifference(planninginfo.get(i).get(j-1).getEndTime(),planninginfo.get(i).get(j).getStartTime())),LayoutParams.MATCH_PARENT));
						linear[i].addView(empty);
						
						TextView div = new TextView(Activity_Planning.this);
					    div.setLayoutParams(llp);
						div.setBackgroundColor(Color.GRAY);
						linear[i].addView(div);
					}
				}
				
				linear[i].addView(txt[j]);
				//if(j != txt.length-1)
					linear[i].addView(divider);
			}
		}
		
		LinearLayout li = new LinearLayout(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, pxtodp(60));
		mainlinear.addView(li, params);
		li.setOrientation(LinearLayout.HORIZONTAL);
		
		TextView time = null;
		
		int iter = 0;
		if(eventend.get(Calendar.MINUTE) > 0)
			eventend.add(Calendar.HOUR_OF_DAY,1);
		while(eventstart.compareTo(eventend) < 0)
		{
			int w = 0;
			String content = "";
			if(time != null)
			{
				time.measure(0, 0);
				w = time.getMeasuredWidth();
				content = (String) time.getText();
			}
			
			time = new TextView(Activity_Planning.this);
			time.setText(eventstart.get(Calendar.HOUR_OF_DAY)+":00");
			LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			if(iter!=0)
				llp.setMargins(pxtodp(100-w), 0, 0, 0); // llp.setMargins(left, top, right, bottom);
		    time.setLayoutParams(llp);
			li.addView(time);
			iter++;
			eventstart.add(Calendar.HOUR_OF_DAY, 1);
		}
		
		
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		//context.getSharedPreferences("EventPrefs", 0).edit().clear().commit(); // Removes all preferences.
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
				((TextView)v).setBackgroundColor(0x22cccccc);
			}
		}
		
		for(int i=0;i<savedEvents.length();i++)
		{
			try {
				String id = ((JSONObject) savedEvents.get(i)).getString("id");
				for(int p = 0;p<planninginfo.size();p++)
					for(int e = 0;e<planninginfo.get(p).size();e++)
						if(id.equals(planninginfo.get(p).get(e).getId()))
							((TextView) textList.get(p).get(e)).setBackgroundColor(0xff66ff66);
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
		
		return gregorianDifference(start,end);
		
	}
	
	private float gregorianDifference(GregorianCalendar first, GregorianCalendar second)
	{
		float startHour = first.get(Calendar.HOUR_OF_DAY)+first.get(Calendar.MINUTE)/60f;
		float endHour = second.get(Calendar.HOUR_OF_DAY)+second.get(Calendar.MINUTE)/60f;
		
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

	     PlanningEvent e;
	     public EventClickListener(PlanningEvent n) {
	          this.e = n;
	     }

	     @Override
	     public void onClick(View v)
	     {
	    	 Intent newpage = new Intent("com.project.eventmanagerapp.Activity_Planninginfo");		
	    	 newpage.putExtra("planning_event",this.e);
	    	 startActivity(newpage);
	     }

	  };
	  
	  @Override
	  public void onDestroy() {
		  super.onDestroy();
	      planninginfo = new ArrayList<ArrayList<PlanningEvent>>();
	  }
}