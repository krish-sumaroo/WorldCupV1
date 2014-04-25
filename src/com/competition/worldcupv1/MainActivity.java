package com.competition.worldcupv1;

import com.competition.worldcupv1.service.TeamService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {
	
	private Thread mSplashThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mSplashThread = new Thread() {
			@Override		
			public void run() {
		 try {
             // Thread will sleep for 5 seconds
             sleep(5*1000);
              
             // After 5 seconds redirect to another intent     		
             // populate the database
             
             // insert team data
             TeamService teamService = new TeamService();
             teamService.insertTeamsData(getApplicationContext());
             
     		Intent i = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
            // Close Registration View
            finish();
            
         } catch (Exception e) {          
         }
     }		
		};
	    // start thread
	    mSplashThread.start(); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		//test
		return true;
	}	
   }
