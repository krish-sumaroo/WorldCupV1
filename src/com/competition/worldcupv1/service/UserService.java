package com.competition.worldcupv1.service;

import android.content.Context;
import com.competition.worldcupv1.Config;
import com.google.android.gcm.GCMRegistrar;

public class UserService {
	
	public void registerGCM(Context context){  
		// Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(context);
 
        // Make sure the manifest permissions was properly set
        GCMRegistrar.checkManifest(context);
        
        // Register with GCM           
        GCMRegistrar.register(context, Config.GOOGLE_SENDER_ID); 
	}
}
