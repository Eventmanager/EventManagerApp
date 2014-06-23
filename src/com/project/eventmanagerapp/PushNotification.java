package com.project.eventmanagerapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;

public class PushNotification {
	
	/*
	 * Sends a push notification.
	 * @Params startActivity An intent of the activity to be started when the notification is clicked
	 * @Params icon This must be a referral to R.drawable.XXX If you don't want an custom icon you can use input 0
	 */
	public static void sendNotification(Context context, Intent startActivity, String title, String body, int icon, boolean doVibrate){
		NotificationManager NM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				
		int IconRefAddress = (icon != 0) ? icon : android.R.drawable.stat_notify_more;
		
		Notification notify = new Notification(IconRefAddress, title, System.currentTimeMillis());
		
		PendingIntent pending = PendingIntent.getActivity(context, 0, startActivity, 0);
		notify.setLatestEventInfo(context, title, body, pending);
		NM.notify(0, notify);
		Log.i("PushNotification", "Just sent out a notification: " + title + " --- " + body);
		
		if(doVibrate){
			Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(500);
			long[] pattern = {0, 500, 500, 500};
			v.vibrate(pattern, -1);
		}
	}
	
	/*
	 * Removes the applications push notification from the pull-down menu
	 */
	public static void removeNotification(Context context){
		((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
	}
}