package com.project.eventmanagerapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PlanningManager {

	//This class is a singleton, so we'll keep track if it exists yet.
	private static PlanningManager instance = null;
	private Context context;
	private ArrayList<ArrayList<PlanningEvent>> planningList = new ArrayList<ArrayList<PlanningEvent>>();
	private ArrayList<String> podiumList = new ArrayList<String>();
	
	private PlanningManager(Context _context){
		context = _context;
	}
	
	public static PlanningManager getInstance(Context context){
		if(instance == null){
			instance = new PlanningManager(context);			
		}		
		return instance;
	}
	
	
	
	public ArrayList<ArrayList<PlanningEvent>> getPlanning(){
		Thread c = new Thread(new Runnable(){
			public void run(){
				NewsItem notifyNews = null;
				for(PlanningEvent planE : requestPlanning()){
					if(!podiumList.contains(planE.getStage()))
					{
						podiumList.add(planE.getStage());
						planningList.add(new ArrayList<PlanningEvent>());
					}
					
					int podium_id = podiumList.indexOf(planE.getStage());
					
					ArrayList<PlanningEvent> currentPodiumList = planningList.get(podium_id);
					if(currentPodiumList.size() != 0)
					{
						boolean notyetadded = true;
						for(PlanningEvent p : currentPodiumList)
							if(planE.getStartTime().compareTo(p.getStartTime()) < 0 && notyetadded)
							{
								planningList.get(podium_id).add(currentPodiumList.indexOf(p),planE);
								notyetadded = false;
							}
						if(notyetadded)
							planningList.get(podium_id).add(planE);
					}
					else
						planningList.get(podium_id).add(planE);
								
				}
				
			}}
		);
		c.start();
		try {
			c.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return planningList;
	}
	
	private ArrayList<PlanningEvent> requestPlanning(){
		ArrayList<PlanningEvent> list = new ArrayList<PlanningEvent>();
		String fullJSonString = null;

		try {
			Log.d("PlanningManager", "At: " + System.currentTimeMillis() + " Requesting planning from server");
			ServerCommunication sc = new ServerCommunication(context);
			fullJSonString = sc.getPlanningJSonFromServer();
			Log.d("PlanningManager", "At: " + System.currentTimeMillis() + " Got the planning from server. Start parsing it");
		} catch (Exception e) {
			Log.w("PlanningManager", "Could not get planning from server, possible connection problem (?)");
			Log.e("PlanningManager", Log.getStackTraceString(e));
			return list; //Return empty list, other methods should be able to handle that
			//long lastCheckTime isn't changed, this way connection loss doesn't make you miss out on news items 
		}
		
		try {
			list = JSonDecoder.decodePlanningJSon(fullJSonString);
		} catch (JSONException e) {
			Log.w("PlanningManager", "Can't read the JSON gotten from the server/planning");
			Log.e("PlanningManager", Log.getStackTraceString(e));
			return list; //Return empty list, other methods should be able to handle that
			//long lastCheckTime isn't changed, this way connection loss doesn't make you miss out on news items
		}
		
		return list;
	}
}
