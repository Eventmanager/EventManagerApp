package com.project.eventmanagerapp;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import android.util.Log;

public class NewsSource {
	
	//NewsSource is a singleton, this is the place all news is stored, this class also gets the news from the server.
	//To avoid multiple threads syncing the news there can only be one NewsSource
	private static NewsSource instance = null;
	private UpdateTimer uTimer;
	private long UPDATEDELAY = 60000;//TODO: get this value from options menu //TMP
	private GregorianCalendar lastCheckTime;
	
	private ArrayList<NewsItem> newsList = new ArrayList<NewsItem>();
	
	private NewsSource(){
		uTimer = new UpdateTimer(this, UPDATEDELAY);
		new Thread(uTimer).start();
		lastCheckTime = new GregorianCalendar(1900, 1, 1);//Last check is so far in the past that every news item is new per default.
	}
	
	//You don't make a new newssource, you get the only excisting instance, that is the point of a singleton construction
	public static NewsSource getInstance(){
		if(instance == null){
			instance = new NewsSource();			
		}		
		return instance;
	}
	
	//Get news list
	public ArrayList<NewsItem> getNewsList(){
		if(newsList.isEmpty()){
			updateNewsList();
		}
		return newsList;
	}
	
	//Get latest news from server, if new news is found a push notification will be send to the user
	public void updateNewsList(){
		boolean hasSendNotification = false;
		for(NewsItem newNI : requestNews()){
			//Check if newsitem is already in the list
			boolean isNew = true;
			for(NewsItem oldNI : newsList){
				if(newNI.getId() == oldNI.getId()){
					isNew = false;
				}
			}
			
			if(isNew){
				newsList.add(newNI);
				if(!hasSendNotification){
					sendNotification(newNI);
					hasSendNotification = true;
				}
			}
		}
	}
	
	//Function that communicates with the server
	private ArrayList<NewsItem> requestNews(){
		//TODO: get news from a server, make sure to use lastCheckTime to not stress server out to much
		ArrayList<NewsItem> list = new ArrayList<NewsItem>();
		
		//TODO: Remove this placeholder
		list.add(new NewsItem("Lorum!", 0, new GregorianCalendar(2007, 5, 18, 15, 26, 30), "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur fermentum risus urna. In elit enim, aliquam at risus quis, faucibus ullamcorper nisi. Donec nec metus nec lorem lobortis sodales ut at eros. Donec dictum est vel neque laoreet accumsan. Integer et nulla elit. Praesent sed venenatis nisi, eu dictum elit."));
		list.add(new NewsItem("GEN", (int)(Math.random() * 1000), new GregorianCalendar(), "" + System.currentTimeMillis()));
		
		lastCheckTime = new GregorianCalendar();//Calander w/ current date and time
		return list;
	}
	
	private void sendNotification(NewsItem ni){
		//TODO: send a push notification JORDI PLZ
		Log.i("NewsSource", "placeholder for actual notification: " + ni.getTitle());
	}
}

//Timer class used to update news on set intervals
class UpdateTimer implements Runnable{
	private NewsSource newsSource;
	private long delay;
	
	UpdateTimer(NewsSource _newsSource, long _delay){
		newsSource = _newsSource;
		delay = _delay;
	}
	
	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(delay);
				newsSource.updateNewsList();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
