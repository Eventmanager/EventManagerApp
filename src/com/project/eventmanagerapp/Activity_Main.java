package com.project.eventmanagerapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Activity_Main extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		NewsSource.getInstance(this);//Starts the news update loop
		
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
}
