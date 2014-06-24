package com.project.eventmanagerapp;

import java.util.ArrayList;
import org.json.JSONException;

import android.content.Context;
import android.util.Log;

public class MapManager {

	//This class is a singleton, so we'll keep track if it exists yet.
	private static MapManager instance = null;
	private Context context;
	private ArrayList<MapImage> mapImages = new ArrayList<MapImage>();
	private ArrayList<MapShape> mapShapes = new ArrayList<MapShape>();
	
	private MapManager(Context _context){
		context = _context;
	}
	
	public static MapManager getInstance(Context context){
		if(instance == null){
			instance = new MapManager(context);			
		}		
		return instance;
	}
	
	
	
	public ArrayList<ArrayList> getMapItems(){
		Thread c = new Thread(new Runnable(){
			public void run(){
				mapImages = requestImages();
				mapShapes = requestShapes();
				
			}}
		);
		c.start();
		try {
			c.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<ArrayList> list = new ArrayList<ArrayList>();
		list.add(mapImages);
		list.add(mapShapes);
		return list;
	}
	
	private ArrayList<MapImage> requestImages(){
		ArrayList<MapImage> list = new ArrayList<MapImage>();
		String fullJSonString = null;

		try {
			Log.d("MapManager", "At: " + System.currentTimeMillis() + " Requesting map images from server");
			ServerCommunication sc = new ServerCommunication(context);
			fullJSonString = sc.getMapItemJSonFromServer();
			Log.d("MapManager", "At: " + System.currentTimeMillis() + " Got the map images from server. Start parsing it");
		} catch (Exception e) {
			Log.w("MapManager", "Could not get map images from server, possible connection problem (?)");
			Log.e("MapManager", Log.getStackTraceString(e));
			return list; //Return empty list, other methods should be able to handle that
			//long lastCheckTime isn't changed, this way connection loss doesn't make you miss out on news items 
		}
		
		try {
			list = JSonDecoder.decodeMapImageJSon(fullJSonString);
		} catch (JSONException e) {
			Log.w("MapManager", "Can't read the JSON gotten from the server/mapitems");
			Log.e("MapManager", Log.getStackTraceString(e));
			return list; //Return empty list, other methods should be able to handle that
			//long lastCheckTime isn't changed, this way connection loss doesn't make you miss out on news items
		}
		
		return list;
	}
	
	private ArrayList<MapShape> requestShapes(){
		ArrayList<MapShape> list = new ArrayList<MapShape>();
		String fullJSonString = null;

		try {
			Log.d("MapManager", "At: " + System.currentTimeMillis() + " Requesting map shapes from server");
			ServerCommunication sc = new ServerCommunication(context);
			fullJSonString = sc.getMapItemJSonFromServer();
			Log.d("MapManager", "At: " + System.currentTimeMillis() + " Got the map shapes from server. Start parsing it");
		} catch (Exception e) {
			Log.w("MapManager", "Could not get map shapes from server, possible connection problem (?)");
			Log.e("MapManager", Log.getStackTraceString(e));
			return list; //Return empty list, other methods should be able to handle that
			//long lastCheckTime isn't changed, this way connection loss doesn't make you miss out on news items 
		}
		
		try {
			list = JSonDecoder.decodeMapShapesJSon(fullJSonString);
		} catch (JSONException e) {
			Log.w("MapManager", "Can't read the JSON gotten from the server/mapitems");
			Log.e("MapManager", Log.getStackTraceString(e));
			return list; //Return empty list, other methods should be able to handle that
			//long lastCheckTime isn't changed, this way connection loss doesn't make you miss out on news items
		}
		
		return list;
	}
}
