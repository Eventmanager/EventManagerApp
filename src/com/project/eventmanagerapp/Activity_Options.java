package com.project.eventmanagerapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class Activity_Options extends Activity {
	
	//Will be instatiated onCreate
	SharedPreferences sharedPref;
	Editor editor;
	
	EditText refreshTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);
		
		//Preferences file and editor to change values
		sharedPref  = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
		editor = sharedPref.edit();
		
		//User inputs in minutes how often NewsSource should get news from server
		refreshTime = (EditText) findViewById(R.id.in_refreshtime);
		
		refreshTime.addTextChangedListener(new TextWatcher(){
			@Override
			public void afterTextChanged(Editable s) {
				if(s.toString().matches("^-?\\d+$")){//REGEX means, is digit w/out other chars
					int userInput = 1000 * (Integer.parseInt(s.toString()));//x1000 for ms<sec
					if(userInput > 999){//To prevent negative or very low input
						editor.putInt("newsRefreshTimeMillis", userInput);
						editor.commit();
					}
				}
			}
			@Override
	        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
			@Override
	        public void onTextChanged(CharSequence s, int start, int before, int count){}
	    }); 
	}
}