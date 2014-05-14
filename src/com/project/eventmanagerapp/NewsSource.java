package com.project.eventmanagerapp;

import java.util.ArrayList;

import android.util.Log;

public class NewsSource {
	
	//NewsSource is a singleton, this is the place all news is stored, this class also gets the news from the server.
	//To avoid multiple threads syncing the news there can only be one NewsSource
	private static NewsSource instance = null;
	
	private ArrayList<NewsItem> newsList = new ArrayList<NewsItem>();
	
	private NewsSource(){}
	
	public static NewsSource getInstance(){
		if(instance == null){
			instance = new NewsSource();			
		}		
		return instance;
	}
	
	public ArrayList<NewsItem> getNewsList(){
		if(newsList.isEmpty()){
			updateNewsList();
		}
		return newsList;
	}
	
	//Gets latest news from server
	public void updateNewsList(){
		boolean hasSendNotification = false;
		for(NewsItem newNI : requestNews()){
			//Check if newsitem is already in the list
			boolean isNew = true;
			for(NewsItem oldNI : newsList){
				if(newNI.getTitle().equals(oldNI.getTitle()) && newNI.getText().equals(oldNI.getText())){
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
	
	private ArrayList<NewsItem> requestNews(){
		//TODO: get news from a server
		ArrayList<NewsItem> list = new ArrayList<NewsItem>();
		
		list.add(new NewsItem("Lorum!", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur fermentum risus urna. In elit enim, aliquam at risus quis, faucibus ullamcorper nisi. Donec nec metus nec lorem lobortis sodales ut at eros. Donec dictum est vel neque laoreet accumsan. Integer et nulla elit. Praesent sed venenatis nisi, eu dictum elit."));
		list.add(new NewsItem("Ipsem", "Nulla eget lacus porta, consectetur leo facilisis, aliquet ligula. Vivamus quis molestie libero. Ut adipiscing felis odio, nec eleifend nunc condimentum non. Fusce id dui mauris. Pellentesque ac metus vestibulum, scelerisque nibh volutpat, iaculis est. Nunc ultricies leo ac tortor euismod consequat."));
		list.add(new NewsItem("Molestie faucibus", "Pellentesque molestie faucibus auctor. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas."));
		
		return list;
	}
	
	private void sendNotification(NewsItem ni){
		//TODO: send a push notification
		Log.i("NewsSource", "placeholder for actual notification: " + ni.getTitle());
	}
}
