package com.competition.worldcupv1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.competition.worldcupv1.asynctasks.CheckUserNameTask;
import com.competition.worldcupv1.asynctasks.CheckUserNameTask.CheckUserNameTaskListener;
import com.competition.worldcupv1.asynctasks.LoginTask;
import com.competition.worldcupv1.asynctasks.LoginTask.LoginTaskListener;
import com.competition.worldcupv1.dto.UserDTO;
import com.competition.worldcupv1.utils.AlertDialogManager;
import com.competition.worldcupv1.utils.ConnectionDetector;
import com.competition.worldcupv1.utils.ConnectionUtility;
import com.competition.worldcupv1.utils.ConnectionUtility.ConnectionUtilityListener;
import com.competition.worldcupv1.utils.SessionManager;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

@SuppressWarnings("deprecation")
@SuppressLint("NewApi")
public class MainActivity extends Activity {	
	//----------------- FACEBOOK ------------------------
    private static String APP_ID = "707333179297046";
    private Facebook facebook;
    private AsyncFacebookRunner mAsyncRunner;
    String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;
    SessionManager session;
 	
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
    
    private static Twitter twitter;
    private static RequestToken requestToken;

    Button btnLoginTwitter;
    Button btnLoginFbk;
    ProgressDialog pDialog;
    private Button btnLogin;
	private Button btnLinkToRegister;
	private EditText txtUserName;
	private EditText txtPassword;
	UserDTO userDto = null;
	private CheckBox rememberMe;
     
    // Shared Preferences
    private static SharedPreferences mSharedPreferences;
     
    // Internet Connection detector
    private ConnectionDetector cd;
     
    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);		   
        mPrefs = getPreferences(MODE_PRIVATE);
		// Session class instance
        session = new SessionManager(getApplicationContext());	
	
		// initialise facebook
		facebook = new Facebook(APP_ID);
        mAsyncRunner = new AsyncFacebookRunner(facebook);
	
		boolean isLoggedIn = session.isLoggedIn();
		//if isloggged in go to list page directly
		if(isLoggedIn){
			Intent i = new Intent(getApplicationContext(), GameListActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);             
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);             
            // Staring Login Activity
            getApplicationContext().startActivity(i);
            finish();
		}
		else{
			//if not already logged in delete facebook token if in session
			 Editor editorFbk = mPrefs.edit();
			 editorFbk.remove("access_token");
			 editorFbk.remove("access_expires");
			 editorFbk.commit();
	            
			// -------------- FACEBOOK ----------------
			btnLoginFbk = (Button) findViewById(R.id.btnFbk);   
			btnLoginFbk.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View v) {
			       loginToFacebook();
			     }
			});	        		
			
			// ------------- TWITTER -------------------			
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		     StrictMode.setThreadPolicy(policy);
	         cd = new ConnectionDetector(getApplicationContext());		 
	         // Check if Internet present
	         if (!cd.isConnectingToInternet()) {
	        	 // Internet Connection is not present
	            alert.showAlertDialog(MainActivity.this, "Internet Connection Error","Please connect to working Internet connection", false);
	            // stop executing code by return
	            return;
	         }	         
	         // Check if twitter keys are set
	         if(TWITTER_CONSUMER_KEY.trim().length() == 0 || TWITTER_CONSUMER_SECRET.trim().length() == 0){
	        	 // Internet Connection is not present
	            alert.showAlertDialog(MainActivity.this, "Twitter oAuth tokens", "Please set your twitter oauth tokens first!", false);
	            // stop executing code by return
	            return;
	         }	 
	         // All UI elements
	         btnLoginTwitter = (Button) findViewById(R.id.btnLoginTwitter);
	         // Shared Preferences
	         mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);

	         btnLoginTwitter.setOnClickListener(new View.OnClickListener() {	 
	            @Override
	            public void onClick(View arg0) {
	            	// Call login twitter function
	                loginToTwitter();
	            }
	         }); 
		     if (!isTwitterLoggedInAlready()) {
	            Uri uri = getIntent().getData();
	            if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
	                // oAuth verifier
	                String verifier = uri.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);	 
	                try {
	                    // Get the access token
	                    AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
	                    // Getting user details from twitter
	                    long userID = accessToken.getUserId();
	                    userDto = new UserDTO(String.valueOf(userID), "", "", "", "", 0);
	                    completeRegistration(userDto, accessToken);
	                } catch (Exception e) {
	                    // Check log for login errors
	                    Log.e("Twitter Login Error", "> " + e.getMessage());
	                	}
	            	}
		     	}
				if (isTwitterLoggedInAlready()) {
					Intent gameList = new Intent(getApplicationContext(), GameListActivity.class);
					//Close all views before launching matchList
					gameList.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		            startActivity(gameList);
				 }
				//-------------------- NORMAL -------------------------------
				txtUserName = (EditText) findViewById(R.id.txt_username);
			    txtPassword = (EditText) findViewById(R.id.txt_pwd);
			    btnLogin = (Button) findViewById(R.id.btnLogin);
			    btnLinkToRegister = (Button) findViewById(R.id.btnRegister);
			    rememberMe = (CheckBox) findViewById(R.id.chkRemeber);
			        
		        // Login button Click Event (normal)
		        btnLogin.setOnClickListener(new View.OnClickListener() {		 
		            public void onClick(View view) {
		            	final ConnectionUtility connectionUtility = new ConnectionUtility();
		            	if(( txtUserName.length() == 0 || txtUserName.equals("") || txtUserName == null) || (txtPassword.length() == 0 || txtPassword.equals("") || txtPassword == null))
		                {    	
		            		Toast toast = Toast.makeText(MainActivity.this,"Please fill in all the fields", Toast.LENGTH_LONG);
		            		toast.setGravity(Gravity.CENTER, 0, 0);
		            		toast.show();
		                }
		            	else{
		            		final String userName = txtUserName.getText().toString();
		                	final String password = txtPassword.getText().toString();
		            		final UserDTO user = new UserDTO(userName,"","","",password,0);
		            		try {
		    					if(connectionUtility.hasWifi(getBaseContext())){
		    						login(user);
		    					}
		    					else{
		    						connectionUtility.showToast(MainActivity.this);
		    						connectionUtility.setUtilityListener(new ConnectionUtilityListener()  {				
		    							@Override
		    							public void onInternetEnabled(boolean result) {
		    								login(user);  
		    							}
		    							@Override
		    							public void exitApplication(boolean result) {
		    								onDestroy();
		    								finish();				
		    							}
		    						});	
		    					}
		    		    	}
							finally{
								
							} 
		            	}
		            }
		        });			        
			    // Link to Register Screen
			    btnLinkToRegister.setOnClickListener(new View.OnClickListener() {			 
			    	public void onClick(View view) {
			        	Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
			            startActivity(i);
			            finish();
			            }
			    });
			}	 
		}	 
	    /**
	     * Function to login twitter
	     * */
	    private void loginToTwitter() {
	        // Check if already logged in
	        if (!isTwitterLoggedInAlready()) {
	            ConfigurationBuilder builder = new ConfigurationBuilder();
	            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
	            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
	            Configuration configuration = builder.build();	             
	            TwitterFactory factory = new TwitterFactory(configuration);
	            twitter = factory.getInstance();	 
	            try {
	                requestToken = twitter.getOAuthRequestToken(TWITTER_CALLBACK_URL);
	                this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL())));
	            } catch (TwitterException e) {
	                e.printStackTrace();
	            }
	        } else {
	            // user already logged into twitter
	        	Toast toast = Toast.makeText(MainActivity.this,"Already Logged into twitter", Toast.LENGTH_LONG);
        		toast.setGravity(Gravity.CENTER, 0, 0);
        		toast.show();
	        }
	    }	 
	 
	    /**
	     * Check user already logged in your application using twitter Login flag is
	     * fetched from Shared Preferences
	     * */
	    private boolean isTwitterLoggedInAlready() {
	        // return twitter login status from Shared Preferences
	        return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
	    }
	 
	    protected void onResume() {
	        super.onResume();
	    }
	
	public void loginToFacebook() {		
	    mPrefs = getPreferences(MODE_PRIVATE);
	    String access_token = mPrefs.getString("access_token", null);
	    long expires = mPrefs.getLong("access_expires", 0);	 
	    if (access_token != null) {
	        facebook.setAccessToken(access_token);
	    }	 
	    if (expires != 0) {
	        facebook.setAccessExpires(expires);
	    }	 
	    if (!facebook.isSessionValid()) {
	        facebook.authorize(this,
            new String[] { "email", "publish_stream" },
            new DialogListener() {	 
	            @Override
	            public void onCancel() {
	            } 
                @Override
			    public void onComplete(Bundle values) {			        
			        //get Profile INFO
			        getProfileInformation() ;			        			        
			        //GraphUser user = null ;
	            }	 
                @Override
                public void onError(DialogError error) {
                    // Function to handle error	 
                } 
                @Override
                public void onFacebookError(FacebookError fberror) {
                    // Function to handle Facebook errors	 
                }
	        });
	    }
	}
 	
	/**
	 * Get Profile information by making request to Facebook Graph API
	* */
	public void getProfileInformation() {
		mAsyncRunner.request("me", new RequestListener() {
			@Override
		 	public void onComplete(String response, Object state) {
		 	Log.d("Profile", response);
		 	String json = response;
		 	try {
		 		// Facebook Profile JSON data
		 	    JSONObject profile = new JSONObject(json); 	     
		 	    // getting name of the user
		 	    final String nickName = profile.getString("name"); 	     
		 	    // getting email of the user
		 	    final String userName = profile.getString("email"); 	     
		 	    runOnUiThread(new Runnable() {
		 	    	@Override
		 	    	public void run() {		 	    		
		 	    		Editor e = mPrefs.edit();
		 	            e.remove("access_token");
		 	            e.remove("access_expires");
		 	            e.commit();
		 	    		
		 	            userDto = new UserDTO(userName, "", "", nickName, "", 0);
		 	            completeRegistrationFacebook(userDto);
		 	    	}
		 	    });		 	     
		 	 } catch (JSONException e) {
		 	     e.printStackTrace();
		 	}
			}
	 	   @Override
	 	   public void onIOException(IOException e, Object state) {
	 	   }
	 	   @Override
	 	   public void onFileNotFoundException(FileNotFoundException e,
	 	     Object state) {
	 	   }
	 	   @Override
	 	   public void onFacebookError(FacebookError e, Object state) {
	 	   }
	 	   @Override
	 	   public void onMalformedURLException(MalformedURLException e, Object state) {
			// TODO Auto-generated method stub			
	 	   }
		});
	}
 	 
 	public void login (final UserDTO user){
		LoginTask loginTask = new LoginTask();
		loginTask.setUser(user);
		loginTask.setContext(getApplicationContext());
		loginTask.execute();        
		loginTask.setLoginTaskListener(new LoginTaskListener() {			
			@Override
			public void onComplete(String result) {
				if (result != "") {   
                    if (result == "loginSuccess") {				
						System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> result" + result);
						if(rememberMe.isChecked()){
							session.createLoginSession(user.getUserName(),"","",0,"");
						}else{
							session.createTempSession(user.getUserName(),"","",0,"");
						}						
						Intent matchList = new Intent(getApplicationContext(), GameListActivity.class);
		                // Close all views before launching matchList
		                matchList.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		                startActivity(matchList);                
		                // Close match list View
		                finish();
		             }else{
		            	 Toast.makeText(getApplicationContext(), "Credentials not ok", Toast.LENGTH_LONG).show(); 
		             }
				}
				else{
                    // Error in registration
					Toast.makeText(getApplicationContext(), "Error occured in login", Toast.LENGTH_LONG).show(); 
               }				
			}
		});
	}
 	
	public void completeRegistration (final UserDTO user, final AccessToken accessToken){
		CheckUserNameTask checkUserNameTask = new CheckUserNameTask();
        checkUserNameTask.setUser(user);
        checkUserNameTask.setContext(getApplicationContext());
        checkUserNameTask.execute();        
        checkUserNameTask.setCheckUserNameTaskListener(new CheckUserNameTaskListener() {			
			@Override
			public void onComplete(String result) {
				if (result != "") {   
                    if (result.equalsIgnoreCase("userNameExist")) {
						 Editor e = mSharedPreferences.edit();	 
		                    // After getting access token, access token secret
		                    // store them in application preferences
		                    e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
		                    e.putString(PREF_KEY_OAUTH_SECRET,accessToken.getTokenSecret());
		                    // Store login status - true
		                    e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
		                    e.commit(); // save changes	 
		                    
		                    long userID = accessToken.getUserId();
		                    User user = null;
							try {
								user = twitter.showUser(userID);
							} catch (TwitterException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
		                    String nickname = user.getName();
		                    userDto = new UserDTO(String.valueOf(userID), "", "", nickname, "", 0);
		                    session.createLoginSession(String.valueOf(userID), "", nickname, 0, "");		
							Intent matchList = new Intent(getApplicationContext(), GameListActivity.class);
			                // Close all views before launching matchList
			                matchList.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			                startActivity(matchList);                
			                // Close match list View
			                finish();
		             	}
                    else{
                    	 long userID = accessToken.getUserId();
		                    User user = null;
							try {
								user = twitter.showUser(userID);
							} catch (TwitterException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
		                    String nickname = user.getName();	  
		                    session.addTwitterLoginSession(String.valueOf(userID), nickname);
		                    session.addLoginType("twitter");
	                    	Intent completeRegistration = new Intent(getApplicationContext(), TwitterFacebookRegistration.class);
			                // Close all views before launching matchList
	                    	completeRegistration.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			                startActivity(completeRegistration);                
			                // Close match list View
			                finish();
                    	}
					}
				else{
                    // Error in registration
					Toast.makeText(getApplicationContext(), "Error occured in registration", Toast.LENGTH_LONG).show(); 
               }				
			}
		});
	}
	
	public void completeRegistrationFacebook (final UserDTO user){
		CheckUserNameTask checkUserNameTask = new CheckUserNameTask();
        checkUserNameTask.setUser(user);
        checkUserNameTask.setContext(getApplicationContext());
        checkUserNameTask.execute();        
        checkUserNameTask.setCheckUserNameTaskListener(new CheckUserNameTaskListener() {			
			@Override
			public void onComplete(String result) {
				if (result != "") {   
                    if (result.equalsIgnoreCase("userNameExist")) {				
						System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> result" + result);		                    
	                    userDto = new UserDTO(user.getUserName(), "", "", user.getNickName(), "", 0);		                    
	                    session.createLoginSession(user.getUserName(), "", user.getNickName(), 0, "");		                    
						Intent matchList = new Intent(getApplicationContext(), GameListActivity.class);
		                // Close all views before launching matchList
		                matchList.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		                startActivity(matchList);                
		                // Close match list View
		                finish();
		             }
                    else{  
		                session.addFacebookLoginSession(user.getUserName(), user.getNickName());
		                session.addLoginType("facebook");
                    	Intent completeRegistration = new Intent(getApplicationContext(), TwitterFacebookRegistration.class);
		                // Close all views before launching matchList
                    	completeRegistration.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		                startActivity(completeRegistration);                
		                // Close match list View
		                finish();
                    }
				}
				else{
                    // Error in registration
					Toast.makeText(getApplicationContext(), "Error occured in registration", Toast.LENGTH_LONG).show(); 
               }				
			}
		});
	}
}