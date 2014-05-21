package com.competition.worldcupv1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {
	
	private static String PLAYER_FINAL_LIST = "PLAYER FINAL LIST";
	private static String GAME_OVER = "GAME OVER";
	private static String MESSAGE = "MESSAGE";
	
	 public GCMIntentService() {
		 // Call extended class Constructor GCMBaseIntentService
		 super(Config.GOOGLE_SENDER_ID);
	}

	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onMessage(Context arg0, Intent arg1) {	
		Bundle msgJson = arg1.getExtras();
		System.out.println(">>>>>>>>>>>>>>>>>> msgJson = " + msgJson);
		String message = arg1.getExtras().getString("msg");   
		
//		/*
//		// get push action
//		Controller  controller = (Controller) getApplicationContext(); 
//		String actionVal = msgJson.getString("action");
//		if(actionVal.equalsIgnoreCase(PLAYER_FINAL_LIST)){
//			controller.pushActionFinalPlayerList();
//		}
//		if(actionVal.equalsIgnoreCase(GAME_OVER)){
//			controller.pushActionGameOver();
//		}
//		if(actionVal.equalsIgnoreCase(MESSAGE)){
//			controller.pushActionMessage();
//		}
//		*/
	        
		Intent intent = new Intent(Config.DISPLAY_MESSAGE_ACTION);
		intent.putExtra(Config.EXTRA_MESSAGE, arg1);
		// Send Broadcast to Broadcast receiver with message
		arg0.sendBroadcast(intent);
		generateNotification(arg0, message);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		//Get Global Controller Class object (see application tag in AndroidManifest.xml)
       Log.i(TAG, "Device registered: regId = " + registrationId);
       System.out.println(">>>>>>>>>>>>>>>>>> registrationId = " + registrationId);
       Controller  controller = (Controller) getApplicationContext();        
       String uid = controller.getUID();
       controller.registerGCM(uid, registrationId, context);
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub		
	}
	
	 private static void generateNotification(Context context, String message) {	     
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
         
        NotificationManager notificationManager = (NotificationManager)
        context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);         
        String title = context.getString(R.string.app_name);         
        Intent notificationIntent = new Intent(context, MainActivity.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//	         
//	        // Play default notification sound
//	       // notification.defaults |= Notification.DEFAULT_SOUND;
//	         
//	        //notification.sound = Uri.parse(
//	                               "android.resource://"
//	                               + context.getPackageName()
//	                               + "your_sound_file_name.mp3");
         
        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);
	 }
}
