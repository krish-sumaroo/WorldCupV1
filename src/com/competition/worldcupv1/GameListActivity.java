package com.competition.worldcupv1;

import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.competition.worldcupv1.utils.SessionManager;
import com.facebook.Session;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;

@SuppressWarnings("deprecation")
public class GameListActivity extends Activity {	
	//----------------- TWITTER ------------------------
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
   	
   	//----------------- FACEBOOK ---------------------
    private static String APP_ID = "707333179297046";
    //Instance of Facebook Class
    private Facebook facebook;
    private AsyncFacebookRunner mAsyncRunner;
    String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;

   	Button btnLogout;
   	TextView logInName;
   	ProgressDialog pDialog;
   	private static SharedPreferences mSharedPreferences;
   	SessionManager session;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_list);	
		//--------------- facebook -------------
		facebook = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(facebook);
		mPrefs = getPreferences(MODE_PRIVATE);
		        
        session = new SessionManager(getApplicationContext());		
	    btnLogout = (Button) findViewById(R.id.btnLogoutTwitter);
	    logInName = (TextView) findViewById( R.id.logInName);
	    
        HashMap<String, String> user = session.getUserDetails();   
        String nickName = user.get(SessionManager.KEY_NICKNAME);
        logInName.setText(nickName);	    
		// Shared Preferences
		mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
		btnLogout.setOnClickListener(new View.OnClickListener() {	 
			@Override
	        public void onClick(View arg0) {
	            logoutFromTwitter();
	            callFacebookLogout(getApplicationContext(),mPrefs);
	            session.logoutUser();	           
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
    private void logoutFromTwitter() {
        // Clear the shared preferences
        Editor e = mSharedPreferences.edit();
        e.remove(PREF_KEY_OAUTH_TOKEN);
        e.remove(PREF_KEY_OAUTH_SECRET);
        e.remove(PREF_KEY_TWITTER_LOGIN);
        e.commit();
    }
    
	public static void callFacebookLogout(Context context, SharedPreferences mPrefs) {
	    Session session = Session.getActiveSession();
	    if (session != null) {
	        if (!session.isClosed()) {
	            session.closeAndClearTokenInformation();
	            //clear your preferences if saved
	            Editor e = mPrefs.edit();
	            e.remove("access_token");
	            e.remove("access_expires");
	            e.commit();	           
	        }
	    } else {
	        session = new Session(context);
	        //Session.setActiveSession(session);
	        session.closeAndClearTokenInformation();
	        //clear your preferences if saved
            Editor e = mPrefs.edit();
            e.remove("access_token");
            e.remove("access_expires");
            e.commit();
	    }
	}
}
