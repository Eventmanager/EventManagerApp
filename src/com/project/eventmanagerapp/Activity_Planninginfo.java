package com.project.eventmanagerapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class Activity_Planninginfo extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_planninginfo);
		Intent myIntent = getIntent();

		TextView txt=new TextView(Activity_Planninginfo.this);
		txt.setText(myIntent.getStringExtra("eventname"));
		txt.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		LinearLayout mainlinear= (LinearLayout) findViewById(R.id.eventinfo_layout);
		mainlinear.addView(txt);
		
	}
	
}