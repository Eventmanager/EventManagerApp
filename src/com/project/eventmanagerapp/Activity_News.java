package com.project.eventmanagerapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Activity_News extends Activity {
	
	LinearLayout ll = null;
	LayoutParams lp = null;
	Button refreshButton;
	final Context context = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);
		
		ll = (LinearLayout)findViewById(R.id.view_news_list);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		refreshButton = (Button) findViewById(R.id.btn_news_refresh);
		refreshButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				showAndRefreshDisplay();
			}
		});
		
		displayNews();
	}
	
	//Gets news from news source and displays it, newest news first (arraylist is reversed)
	private void displayNews(){
		ArrayList<NewsItem> newsList = NewsSource.getInstance(context).getNewsList();
		if(newsList.size() == 0){
			newsList = (ArrayList<NewsItem>) newsList.clone();
			newsList.add(new NewsItem("--- EMPTY ---", -1, "-1", "Maybe try a refresh"));
		}
		for(int i = newsList.size() - 1; i >= 0; i--){
			NewsItem ni = newsList.get(i);
			TextView title = new TextView(context);
			TextView content = new TextView(context);
			
			title.setText(ni.getTitle());
			title.setTextSize(25);
			title.setPadding(7, 0, 7, 0);
			content.setText(ni.getText());
			content.setTextSize(17);
			content.setPadding(3, 0, 3, 0);
			
			ll.addView(title, lp);
			ll.addView(content, lp);
			
			//horizontal seperator line
			View ruler = new View(context); ruler.setBackgroundColor(0xFF000000);
			ll.addView(ruler, new ViewGroup.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, 2));
		}
	}
	
	//Used by the refresh button to shut activity down, and start it up again, thereby removing the old list and triggering the creaton of a new one.
	public void showAndRefreshDisplay(){
		refreshButton.setText("Wait please...");
		refreshButton.setEnabled(false);
		new Thread(new Runnable() {
			public void run(){
				NewsSource.getInstance(context).updateNewsList();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				finish();
				startActivity(getIntent());
			}}
		).start();
	}
}