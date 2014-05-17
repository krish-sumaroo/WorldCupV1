package com.competition.worldcupv1;

import android.app.Application;
import android.content.Context;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import com.competition.worldcupv1.asynctasks.GetRegistrationIdTask;
import com.competition.worldcupv1.asynctasks.GetRegistrationIdTask.GetRegistrationIdTaskListener;

public class Controller extends Application{

	public void registerGCM (String uid, String regId, Context context){
		GetRegistrationIdTask getRegistrationIdTask = new GetRegistrationIdTask();
		getRegistrationIdTask.setUid(uid);
		getRegistrationIdTask.setRegisId(regId);
		getRegistrationIdTask.setContext(getApplicationContext());
		getRegistrationIdTask.execute();        
		getRegistrationIdTask.setGetRegistrationIdTaskListener(new GetRegistrationIdTaskListener() {			
			@Override
			public void onComplete(String result) {
				
			}
		});
	}
	
	public String getUID(){
		String uid ="";
		final TelephonyManager mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    	//get deviceID
        if (mTelephony.getDeviceId() != null){
        	uid = mTelephony.getDeviceId(); //use for mobiles
         }
        else{
        	uid = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID); //use for tablets
         }
		return uid;  
	}
}
