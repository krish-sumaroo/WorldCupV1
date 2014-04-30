package com.competition.worldcupv1;

import twitter4j.Twitter;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class GameListActivity extends Activity {
	
	//----------------- TWITTER ------------------------
	 // Constants
   /**
    * Register your here app https://dev.twitter.com/apps/new and get your
    * consumer key and secret
    * */
   static String TWITTER_CONSUMER_KEY = "E0iHRkluCMiKQcFGuhMRvErqI";
   static String TWITTER_CONSUMER_SECRET = "NjgLibGPE0lyJL9XiNly0OI6wb1UtTmBLXVmwZ0wqOYFwe1bBE";

   // Preference Constants
   static String PREFERENCE_NAME = "twitter_oauth";
   static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
   static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
   static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

   static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";

   // Twitter oauth urls
   static final String URL_TWITTER_AUTH = "auth_url";
   static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
   static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

   // Update status button
   Button btnLogoutTwitter;


   // Progress dialog
   ProgressDialog pDialog;

   // Twitter
   private static Twitter twitter;
   private static RequestToken requestToken;
    
   // Shared Preferences
   private static SharedPreferences mSharedPreferences;
   


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_list);
		
	    btnLogoutTwitter = (Button) findViewById(R.id.btnLogoutTwitter);
	    
		   // Shared Preferences
		   mSharedPreferences = getApplicationContext().getSharedPreferences(
		           "MyPref", 0);
		   

	        /**
	         * Button click event for logout from twitter
	         * */
	        btnLogoutTwitter.setOnClickListener(new View.OnClickListener() {
	 
	            @Override
	            public void onClick(View arg0) {
	                // Call logout twitter function
	                logoutFromTwitter();
	                
                    Intent mainAct = new Intent(getApplicationContext(), MainActivity.class);
                    // Close all views before launching matchList
                    mainAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainAct);
	            }
	        });
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}
	

    /**
     * Function to logout from twitter
     * It will just clear the application shared preferences
     * */
    private void logoutFromTwitter() {
        // Clear the shared preferences
        Editor e = mSharedPreferences.edit();
        e.remove(PREF_KEY_OAUTH_TOKEN);
        e.remove(PREF_KEY_OAUTH_SECRET);
        e.remove(PREF_KEY_TWITTER_LOGIN);
        e.commit();
 
        // After this take the appropriate action
        // I am showing the hiding/showing buttons again
        // You might not needed this code
       // btnLogoutTwitter.setVisibility(View.GONE);

    }

}
