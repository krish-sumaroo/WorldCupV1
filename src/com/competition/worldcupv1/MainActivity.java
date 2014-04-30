package com.competition.worldcupv1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

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

import com.competition.worldcupv1.asynctasks.LoginTask;
import com.competition.worldcupv1.asynctasks.LoginTask.LoginTaskListener;
import com.competition.worldcupv1.dto.UserDTO;
import com.competition.worldcupv1.service.TeamService;
import com.competition.worldcupv1.utils.ConnectionUtility;
import com.competition.worldcupv1.utils.ConnectionUtility.ConnectionUtilityListener;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.facebook.model.GraphUser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	
	// Your Facebook APP ID
    private static String APP_ID = "707333179297046"; // Replace your App ID here
 
    // Instance of Facebook Class
    private Facebook facebook;
    @SuppressWarnings("deprecation")
	private AsyncFacebookRunner mAsyncRunner;
    String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;
    
    private Button btnLoginFbk;
    private Button btnLogout;

	private ProgressDialog progressDialog;
	
	
	
	
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
 
    // Login button
    Button btnLoginTwitter;
    // Update status button
    Button btnLogoutTwitter;

 
    // Progress dialog
    ProgressDialog pDialog;
 
    // Twitter
    private static Twitter twitter;
    private static RequestToken requestToken;
     
    // Shared Preferences
    private static SharedPreferences mSharedPreferences;
     
    // Internet Connection detector
    private ConnectionDetector cd;
     
    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();
    
	private Button btnLogin;
	private Button btnLinkToRegister;
	private EditText txtUserName;
	private EditText txtPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
//		Intent i = new Intent(getApplicationContext(),LoginActivity.class);
//        startActivity(i);
//        // Close Registration View
//        finish();
		

		
		// ------------- TWITTER -------------------
		
		 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	        StrictMode.setThreadPolicy(policy);
         
	        cd = new ConnectionDetector(getApplicationContext());
	 
	        // Check if Internet present
	        if (!cd.isConnectingToInternet()) {
	            // Internet Connection is not present
	            alert.showAlertDialog(MainActivity.this, "Internet Connection Error",
	                    "Please connect to working Internet connection", false);
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
	        btnLogoutTwitter = (Button) findViewById(R.id.btnLogoutTwitter);

	        // Shared Preferences
	        mSharedPreferences = getApplicationContext().getSharedPreferences(
	                "MyPref", 0);
	 
	        /**
	         * Twitter login button click event will call loginToTwitter() function
	         * */
	        btnLoginTwitter.setOnClickListener(new View.OnClickListener() {
	 
	            @Override
	            public void onClick(View arg0) {
	                // Call login twitter function
	                loginToTwitter();
	            }
	        });
	 

	 
	        /**
	         * Button click event for logout from twitter
	         * */
	        btnLogoutTwitter.setOnClickListener(new View.OnClickListener() {
	 
	            @Override
	            public void onClick(View arg0) {
	                // Call logout twitter function
	                logoutFromTwitter();
	            }
	        });
	 
	        /** This if conditions is tested once is
	         * redirected from twitter page. Parse the uri to get oAuth
	         * Verifier
	         * */
	        if (!isTwitterLoggedInAlready()) {
	            Uri uri = getIntent().getData();
	            if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
	                // oAuth verifier
	                String verifier = uri
	                        .getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);
	 
	                try {
	                    // Get the access token
	                    AccessToken accessToken = twitter.getOAuthAccessToken(
	                            requestToken, verifier);
	 
	                    // Shared Preferences
	                    Editor e = mSharedPreferences.edit();
	 
	                    // After getting access token, access token secret
	                    // store them in application preferences
	                    e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
	                    e.putString(PREF_KEY_OAUTH_SECRET,
	                            accessToken.getTokenSecret());
	                    // Store login status - true
	                    e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
	                    e.commit(); // save changes
	 
	                    Log.e("Twitter OAuth Token", "> " + accessToken.getToken());
	 
	                    // Hide login button
	                    btnLoginTwitter.setVisibility(View.GONE);
	 
	                    // Show Update Twitter
	                    btnLogoutTwitter.setVisibility(View.VISIBLE);
	                     
	                    // Getting user details from twitter
	                    // For now i am getting his name only
	                    long userID = accessToken.getUserId();
	                    User user = twitter.showUser(userID);
	                    String username = user.getName();
	                     
	                    // Displaying in xml ui
	                    Toast.makeText(getApplicationContext(), "Welcome: " + username , Toast.LENGTH_LONG).show();
	                    Intent matchList = new Intent(getApplicationContext(), GameListActivity.class);
                        // Close all views before launching matchList
                        matchList.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(matchList);	
	                } catch (Exception e) {
	                    // Check log for login errors
	                    Log.e("Twitter Login Error", "> " + e.getMessage());
	                }
	            }
	        }
	        

			
			 if (isTwitterLoggedInAlready()) {
				 Intent matchList = new Intent(getApplicationContext(), GameListActivity.class);
	             // Close all views before launching matchList
	             matchList.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	             startActivity(matchList);
			 }

			// Importing all assets like buttons, text fields
				txtUserName = (EditText) findViewById(R.id.txt_username);
		        txtPassword = (EditText) findViewById(R.id.txt_pwd);
		        btnLogin = (Button) findViewById(R.id.btnLogin);
		        btnLinkToRegister = (Button) findViewById(R.id.btnRegister);
		        
		        // Login button Click Event
		        btnLogin.setOnClickListener(new View.OnClickListener() {
		 
		            public void onClick(View view) {
		            	final ConnectionUtility connectionUtility = new ConnectionUtility();
		            	if(( txtUserName.length() == 0 || txtUserName.equals("") || txtUserName == null) || (txtPassword.length() == 0 || txtPassword.equals("") || txtPassword == null))
		                {    		    	
		            		Toast.makeText(getApplicationContext(), "Please fill in all the fields", Toast.LENGTH_LONG).show(); 
		                }
		            	else{
		            		final String userName = txtUserName.getText().toString();
		                	final String password = txtPassword.getText().toString();
		            		final UserDTO user = new UserDTO(userName,"","","",password,"");
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
		    								Intent matchList = new Intent(getApplicationContext(), GameListActivity.class);
		                                    // Close all views before launching matchList
		                                    matchList.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		                                    startActivity(matchList);	    	
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
		                //finish();
		            }
		        });
	 
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
	                requestToken = twitter
	                        .getOAuthRequestToken(TWITTER_CALLBACK_URL);
	                this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
	                        .parse(requestToken.getAuthenticationURL())));
	            } catch (TwitterException e) {
	                e.printStackTrace();
	            }
	        } else {
	            // user already logged into twitter
	            Toast.makeText(getApplicationContext(),
	                    "Already Logged into twitter", Toast.LENGTH_LONG).show();
	        }
	    }
	 
	    /**
	     * Function to update status
	     * */
	    class updateTwitterStatus extends AsyncTask<String, String, String> {
	 
	        /**
	         * Before starting background thread Show Progress Dialog
	         * */
	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(MainActivity.this);
	            pDialog.setMessage("Updating to twitter...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(false);
	            pDialog.show();
	        }
	 
	        /**
	         * getting Places JSON
	         * */
	        protected String doInBackground(String... args) {
	            Log.d("Tweet Text", "> " + args[0]);
	            String status = args[0];
	            try {
	                ConfigurationBuilder builder = new ConfigurationBuilder();
	                builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
	                builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
	                 
	                // Access Token
	                String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
	                // Access Token Secret
	                String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");
	                 
	                AccessToken accessToken = new AccessToken(access_token, access_token_secret);
	                Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
	                 
	                // Update status
	                twitter4j.Status response = twitter.updateStatus(status);
	                 
	                Log.d("Status", "> " + response.getText());
	            } catch (TwitterException e) {
	                // Error in updating status
	                Log.d("Twitter Update Error", e.getMessage());
	            }
	            return null;
	        }
	 
	        /**
	         * After completing background task Dismiss the progress dialog and show
	         * the data in UI Always use runOnUiThread(new Runnable()) to update UI
	         * from background thread, otherwise you will get error
	         * **/
	        protected void onPostExecute(String file_url) {
	            // dismiss the dialog after getting all products
	            pDialog.dismiss();
	            // updating UI from Background Thread
	            runOnUiThread(new Runnable() {
	                @Override
	                public void run() {
	                    Toast.makeText(getApplicationContext(),
	                            "Status tweeted successfully", Toast.LENGTH_SHORT)
	                            .show();
	                    // Clearing EditText field
	                }
	            });
	        }
	 
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
	        btnLogoutTwitter.setVisibility(View.GONE);
	 
	        btnLoginTwitter.setVisibility(View.VISIBLE);
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
		
		/*		
		
		btnLoginFbk = (Button) findViewById(R.id.btnLoginFbk);
		btnLogout = (Button) findViewById(R.id.btnLogout);
		
		facebook = new Facebook(APP_ID);
        mAsyncRunner = new AsyncFacebookRunner(facebook);
		
	     //LOGIN FACEBOOK       
        btnLoginFbk.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		        loginToFacebook();
		     }
		});
        

	     //LOGIN FACEBOOK       
        btnLogout.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	
		    	
		    	  // Bundle b = new Bundle();
		    	 //  b.putString("method", "auth.expireSession");
		    	  // String response = request(b);
		    	   facebook.setAccessToken(null);
		    	  // setAccessToken(null);
		    	   facebook.setAccessExpires(0);
		    	 
	 	       // Util.clearCookies(MainActivity.this);

	 	        // your sharedPrefrence
	 	        Editor editor = getApplicationContext().getSharedPreferences("MYFB_TOKEN",    Context.MODE_PRIVATE).edit();
	 	        editor.clear();
	 	        editor.commit();
		     }
		});
		
		*/
//		mSplashThread = new Thread() {
//			@Override		
//			public void run() {
//		 try {
//             // Thread will sleep for 5 seconds
//             sleep(5*1000);
//              
//             // After 5 seconds redirect to another intent     		
//             // populate the database
//             
//             // insert team data
//             TeamService teamService = new TeamService();
//             teamService.insertTeamsData(getApplicationContext());
//             
//     		Intent i = new Intent(getApplicationContext(),LoginActivity.class);
//            startActivity(i);
//            // Close Registration View
//            finish();
//            
//         } catch (Exception e) {          
//         }
//     }		
//		};
//	    // start thread
//	    mSplashThread.start(); 


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		//test
		return true;
	}
	
	
	@SuppressWarnings("deprecation")
	public void loginToFacebook() {		
		System.out.print("login facebook");		
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
	            	// Function to handle cancel event
	            } 
                @SuppressWarnings("unused")
				@Override
			    public void onComplete(Bundle values) {
			        // Function to handle complete event
			        // Edit Preferences and update facebook acess_token
			        SharedPreferences.Editor editor = mPrefs.edit();
			        editor.putString("access_token",facebook.getAccessToken());
			        editor.putLong("access_expires",facebook.getAccessExpires());
			        editor.commit();			        
			        
			        //get Profile INFO
			        getProfileInformation() ;
			        
//			        //GET FRIEND LIST
//			        mAsyncRunner.request("me/friends", new FriendsRequestListener());
			        
			        GraphUser user = null ;
			        if (user != null) {
			            TextView welcome = (TextView) findViewById(R.id.welcome);
			            welcome.setText("Hello " + user.getName() + "!");
	     	                  }
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
 	 @SuppressWarnings("deprecation")
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
 	     final String name = profile.getString("name");
 	     
 	     // getting email of the user
 	     final String email = profile.getString("email");
 	     
 	     runOnUiThread(new Runnable() {

 	      @Override
 	      public void run() {
 	       Toast.makeText(getApplicationContext(), "Name: " + name + "\nEmail: " + email, Toast.LENGTH_LONG).show();
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
 	 
 	 @SuppressWarnings("deprecation")
 	 public void logoutFromFacebook() {
 	  mAsyncRunner.logout(MainActivity.this, new RequestListener() {
 		
 	   @Override
 	   public void onComplete(String response, Object state) {
 	    Log.d("Logout from Facebook", response);
 	    if (Boolean.parseBoolean(response) == true) {
 	     runOnUiThread(new Runnable() {
 	 
 	      @Override
 	      public void run() {

 	      }
 	 
 	     });
 	 
 	    }
 	   }

	@Override
	public void onIOException(IOException e, Object state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFileNotFoundException(FileNotFoundException e, Object state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMalformedURLException(MalformedURLException e, Object state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFacebookError(FacebookError e, Object state) {
		// TODO Auto-generated method stub
		
	}
 		

	
 	 });
 	 }
 	 
 	 
 	public void login (UserDTO user){
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
 	 

}
	
