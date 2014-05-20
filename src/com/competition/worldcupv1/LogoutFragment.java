package com.competition.worldcupv1;

import java.util.HashMap;

import com.competition.worldcupv1.utils.ConnectionUtility;
import com.competition.worldcupv1.utils.SessionManager;
import com.facebook.Session;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("NewApi")
public class LogoutFragment extends Fragment {
	
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
    
	ProgressDialog pDialog;
   	private static SharedPreferences mSharedPreferences;
   	SessionManager session;
	final ConnectionUtility connectionUtility = new ConnectionUtility();

	public LogoutFragment() {
		
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_logout, container, false);
         
        return rootView;
    }
	
	@Override 
    public void onActivityCreated(Bundle savedInstanceState) { 
        super.onActivityCreated(savedInstanceState); 

		//--------------- facebook -------------
		facebook = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(facebook);
		mPrefs = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
		        
        session = new SessionManager(getActivity());	    
        HashMap<String, String> user = session.getUserDetails();   
        
		// Shared Preferences
		mSharedPreferences = getActivity().getSharedPreferences("MyPref", 0);		
		logoutAppl();
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
	
	public void logoutAppl(){
        logoutFromTwitter();
        callFacebookLogout(getActivity(),mPrefs);
        session.logoutUser();	           
        Intent mainAct = new Intent(getActivity(), MainActivity.class);
        // Close all views before launching matchList
        mainAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainAct);
        ((MainContainerActivity)getActivity()).finish();
	}
}
