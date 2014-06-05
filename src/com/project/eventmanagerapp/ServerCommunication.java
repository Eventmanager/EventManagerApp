package com.project.eventmanagerapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;

public class ServerCommunication {
	
	public String serverAddress;
	public Context context;
	
	public ServerCommunication(Context _context){
		context = _context;
		serverAddress = context.getString(R.string.server_address);
	}
	
	public String getNewsJSonFromServer(String lastCheckTime) throws IOException{
		String beforeArg = "after=" + lastCheckTime;
		String queryParams = beforeArg.replace(" ", "%20");//Don't use spaces, but %20
		queryParams += "&count=30";
		URL url = new URL(serverAddress + "news?" + queryParams);//No hardcoding here! Server address is saved in res/values/strings.xml
		URLConnection con = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		
		String fullContent = "";
		String parseString;
		while((parseString  = in.readLine()) != null){
			fullContent += parseString;
		}
		in.close();

		return fullContent;
	}
	
	public String getPlanningJSonFromServer() throws IOException{
		URL url = new URL(serverAddress + "events");//No hardcoding here! Server address is saved in res/values/strings.xml
		URLConnection con = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		
		String fullContent = "";
		String parseString;
		while((parseString  = in.readLine()) != null){
			fullContent += parseString;
		}
		in.close();

		return fullContent;
	}
}
