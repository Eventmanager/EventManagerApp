package com.project.eventmanagerapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Activity_News extends Activity {
	
	LinearLayout ll = null;
	LayoutParams lp = null;
	final Context context = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);
		
		ll = (LinearLayout)findViewById(R.id.view_news_list);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		displayNews();
	}
	
	private void displayNews(){
		for(NewsItem ni : NewsSource.getInstance().getNewsList()){
			TextView title = new TextView(context);
			TextView content = new TextView(context);
			
			title.setText(ni.getTitle());
			title.setTextSize(25);
			content.setText(ni.getText());
			content.setTextSize(17);
			
			ll.addView(title, lp);
			ll.addView(content, lp);
			
			//horizontal seperator line
			/*
			View ruler = new View(myContext); ruler.setBackgroundColor(0xFF00FF00);
			theParent.addView(ruler, new ViewGroup.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, 2));
			*/
		}
	}
}