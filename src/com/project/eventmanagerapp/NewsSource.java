package com.project.eventmanagerapp;

import java.util.ArrayList;
import org.json.*;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class NewsSource {
	
	//NewsSource is a singleton, this is the place all news is stored, this class also gets the news from the server.
	//To avoid multiple threads syncing the news there can only be one NewsSource
	private static NewsSource instance = null;
	private UpdateTimer uTimer;
	private String lastCheckTime;
	
	private Context context;
	
	private ArrayList<NewsItem> newsList = new ArrayList<NewsItem>();
	
	private NewsSource(Context _context){
		context = _context;
		
		//Initiate auto updating news
		SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
		uTimer = new UpdateTimer(this, pref.getInt("newsRefreshTimeMillis", 60000), context);
		new Thread(uTimer).start();
		
		lastCheckTime = "1901-01-01 11:11:11";//Last check is so far in the past that every news item is new per default.
	}
	
	//You don't make a new newssource, you get the only existing instance, that is the point of a singleton construction
	public static NewsSource getInstance(Context context){
		if(instance == null){
			instance = new NewsSource(context);			
		}		
		return instance;
	}
	
	//Get news list
	public ArrayList<NewsItem> getNewsList(){
		/*if(newsList.isEmpty()){
			updateNewsList();
		}*/
		return newsList;
	}
	
	//Get latest news from server, if new news is found a push notification will be send to the user
	public void updateNewsList(){
		new Thread(new Runnable(){
			public void run(){
				NewsItem notifyNews = null;
				for(NewsItem newNI : requestNews()){
					//Check if newsitem is already in the list
					boolean isNew = true;
					for(NewsItem oldNI : newsList){
						if(newNI.getId().equals(oldNI.getId())){
							isNew = false;
						}
					}
					//The first new NewsItem gets a notification
					if(isNew){
						newsList.add(newNI);
						notifyNews = newNI;
					}
				}
				if(notifyNews != null){//Gets send after parsing through ALL, so it alwasy notifies you of the newest one 
					sendNotification(notifyNews);
					lastCheckTime = notifyNews.getDate();//Saves date of newest post so ik can use the ?before= query argument next request
				}
			}}
		).start();
	}
	
	//Calls getJSonFromServer(), then reads the JSon and turns it into a list<NewsItem>
	private ArrayList<NewsItem> requestNews(){
		ArrayList<NewsItem> list = new ArrayList<NewsItem>();
		String fullJSonString = null;

		try {
			Log.d("NewsSource", "At: " + System.currentTimeMillis() + " Requesting news from server");
			ServerCommunication sc = new ServerCommunication(context);
			fullJSonString = sc.getNewsJSonFromServer(lastCheckTime);
			Log.d("NewsSource", "At: " + System.currentTimeMillis() + " Got the news from server. Start parsing it");
		} catch (Exception e) {
			Log.w("NewsSource", "Could not get news from server, possible connection problem (?)");
			Log.e("NewsSource", Log.getStackTraceString(e));
			return list; //Return empty list, other methods should be able to handle that
			//long lastCheckTime isn't changed, this way connection loss doesn't make you miss out on news items 
		}
		
		try {
			list = JSonDecoder.decodeNewsJSon(fullJSonString);
		} catch (JSONException e) {
			Log.w("NewsSource", "Can't read the JSON gotten from the server/news");
			Log.e("NewsSource", Log.getStackTraceString(e));
			return list; //Return empty list, other methods should be able to handle that
			//long lastCheckTime isn't changed, this way connection loss doesn't make you miss out on news items
		}
		
		return list;
	}
	
	private void sendNotification(NewsItem ni){
		SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
		if(pref.getBoolean("newsShowNotification", true)){
			PushNotification.sendNotification(context, new Intent("com.project.eventmanagerapp.Activity_News"), ni.getTitle(), ni.getText(), 0, false);
		}
	}
}

//Timer class used to update news on set intervals
class UpdateTimer implements Runnable{
	private NewsSource newsSource;
	private long delay;
	
	UpdateTimer(NewsSource _newsSource, long _delay, Context _context){
		newsSource = _newsSource;
		delay = _delay;
		Log.d("UpdateTimer", "Will update news every " + delay + " MS");
	}
	
	@Override
	public void run() {
		while(true){
			try {
				newsSource.updateNewsList();
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
