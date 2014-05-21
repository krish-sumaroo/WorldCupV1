package com.competition.worldcupv1.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectionUtility {	
	private ConnectionUtilityListener utilityListener;	
	public boolean hasWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return (netInfo != null && netInfo.isConnectedOrConnecting());
	}	
	public void showToast(final Context context){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
    	builder.setMessage("Enable your internet connection and try again");
    	builder.setCancelable(false);
    	builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog, int id) {
	    		Boolean hasConnection = hasWifi(context);
	    		Log.d("**hasConnection**","=============== " + hasConnection);
	    		if(hasConnection){
	    			utilityListener.onInternetEnabled(true);
	    			dialog.cancel();
	    			//dialog.dismiss();
	    		}else{
	    			showToast(context);
	    		}
	    	}
    	 });
    	builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.cancel();
				utilityListener.exitApplication(true);	
				dialog.dismiss();
			}    		
    	});
    	 builder.create();
    	 builder.show();
	}		
	public ConnectionUtilityListener getUtilityListener() {
		return utilityListener;
	}
	public void setUtilityListener(ConnectionUtilityListener utilityListener) {
		this.utilityListener = utilityListener;
	}	
	public interface  ConnectionUtilityListener {
		public void onInternetEnabled(boolean result);		
		public void exitApplication(boolean result);
	}
}
