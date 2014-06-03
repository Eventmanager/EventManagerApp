package com.project.eventmanagerapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class Activity_Options extends Activity {
	
	//Will be instatiated onCreate
	SharedPreferences sharedPref;
	Editor editor;
	
	EditText refreshTime;
	CheckBox newsNotify;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);
		
		//Preferences file and editor to change values
		sharedPref  = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
		editor = sharedPref.edit();
		
		//Get views
		refreshTime = (EditText) findViewById(R.id.in_refreshtime);
		newsNotify = (CheckBox) findViewById(R.id.in_newsnotifications);
		
		//Set newsNotify box to represent stored value
		newsNotify.setChecked(sharedPref.getBoolean("newsShowNotification", true));
		
		//User inputs in minutes how often NewsSource should get news from server
		refreshTime.addTextChangedListener(new TextWatcher(){
			@Override
			public void afterTextChanged(Editable s) {
				if(s.toString().matches("^-?\\d+$")){//REGEX means, is digit w/out other chars
					int userInput = 1000 * (Integer.parseInt(s.toString()));//x1000 for ms<sec
					if(userInput > 999){//To prevent negative or very low input
						editor.putInt("newsRefreshTimeMillis", userInput);
						editor.commit();
						Log.i("Options", "Saved a new setting for newsRefreshTimeMillis: " + userInput);
					}
				}
			}
			@Override
	        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
			@Override
	        public void onTextChanged(CharSequence s, int start, int before, int count){}
	    });
		
		//User inputs whether or not news notifications should be shown
		newsNotify.setOnCheckedChangeListener(new OnCheckedChangeListener(){
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
		    	editor.putBoolean("newsShowNotification", isChecked);
		    	editor.commit();
		    	Log.i("Options", "Saved a new setting for newsShowNotification: " + isChecked);
		    }
		});
	}
}