package com.competition.worldcupv1.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.widget.Toast;

import com.competition.worldcupv1.Config;
import com.google.android.gcm.GCMRegistrar;

public class UserService {
	
	public UserService() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void registerGCM(Context context){  
		// Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(context);
 
        // Make sure the manifest permissions was properly set
        GCMRegistrar.checkManifest(context);
        
//
//		// Register custom Broadcast receiver to show messages on activity
//        registerReceiver(mHandleMessageReceiver, new IntentFilter(
//                Config.DISPLAY_MESSAGE_ACTION));
        
        // Register with GCM           
        GCMRegistrar.register(context, Config.GOOGLE_SENDER_ID); 
	}
	
	  // Create a broadcast receiver to get message and show on screen
private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
     
    @Override
    public void onReceive(Context context, Intent intent) {
         
        String newMessage = intent.getExtras().getString(Config.EXTRA_MESSAGE);
         
        // Waking up mobile if it is sleeping
        acquireWakeLock(context);
         
        // Display message on the screen
        //lblMessage.append(newMessage + "");        
         
        Toast.makeText(context,
                       "Got Message: " + newMessage,
                       Toast.LENGTH_LONG).show();
         
        // Releasing wake lock
        releaseWakeLock();
    }
};

private PowerManager.WakeLock wakeLock;

public  void acquireWakeLock(Context context) {
    if (wakeLock != null) wakeLock.release();

    PowerManager pm = (PowerManager)
                      context.getSystemService(Context.POWER_SERVICE);
     
    wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
            PowerManager.ACQUIRE_CAUSES_WAKEUP |
            PowerManager.ON_AFTER_RELEASE, "WakeLock");
     
    wakeLock.acquire();
}

public  void releaseWakeLock() {
    if (wakeLock != null) wakeLock.release(); wakeLock = null;
}
}
