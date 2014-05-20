package com.competition.worldcupv1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.competition.worldcupv1.asynctasks.CreateUserTask;
import com.competition.worldcupv1.asynctasks.CreateUserTask.CreateUserTaskListener;
import com.competition.worldcupv1.dto.TeamDTO;
import com.competition.worldcupv1.dto.UserDTO;
import com.competition.worldcupv1.service.TeamService;
import com.competition.worldcupv1.service.UserService;
import com.competition.worldcupv1.utils.ConnectionUtility;
import com.competition.worldcupv1.utils.ConnectionUtility.ConnectionUtilityListener;
import com.competition.worldcupv1.utils.SessionManager;

public class TwitterFacebookRegistration extends Activity {
    private Spinner countryList;
    private Spinner favTeamList;
    private Thread mSplashThread;
    private Button btnCompleteRegist;
    String uid = "";	
	//Session Manager Class
    SessionManager session;
    private Button btnCancelReg;
    String loginUserName = null;
	String loginNickName = null;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xtra_registration);
		// Session class instance
        session = new SessionManager(getApplicationContext());	
		mSplashThread = new Thread() {    
	        public void run() {
	            try {
	            	runOnUiThread(new Runnable() {  
	                    @Override
	                    public void run() {
	                    	countryList = (Spinner) findViewById( R.id.spinnerCountry);
				            favTeamList = (Spinner) findViewById( R.id.spinnerTeam);
				            btnCompleteRegist = (Button) findViewById( R.id.btnCompleteRegist);
				            btnCancelReg = (Button) findViewById( R.id.btnCancelReg);
				            getCountryList();
				            getTeamList();
				            
				            // Link to Login Screen
				            btnCancelReg.setOnClickListener(new View.OnClickListener() { 
				                public void onClick(View view) {
				                	
				                	// get user data from session
					    		      HashMap<String, String> twitterUser = session.getLoginType();         
					    		      String loginType = twitterUser.get(SessionManager.KEY_LOGIN_TYPE);				    		      
					    		      if(loginType.equalsIgnoreCase("facebook")){
					    		    	  // Close Registration View
						                	Intent i = new Intent(getApplicationContext(),MainActivity.class);
								            startActivity(i);
								            finish();
					    		      }	
					    		      else{
					    		    	  finish();
					    		      }	
				                }
				            });
				            
				            btnCompleteRegist.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									String country = countryList.getSelectedItem().toString();
					            	String favTeam = favTeamList.getSelectedItem().toString();
									final ConnectionUtility connectionUtility = new ConnectionUtility();
					            	if((country.equalsIgnoreCase("Country")) || (favTeam.equalsIgnoreCase("Team")))
					                {   
					            		Toast toast = Toast.makeText(TwitterFacebookRegistration.this,"Please fill in all the fields", Toast.LENGTH_LONG);
					            		toast.setGravity(Gravity.CENTER, 0, 0);
					            		toast.show();
					                }
					            	else{
					                	final String countrySelected = countryList.getSelectedItem().toString();
					                	TeamDTO teamSelected = (TeamDTO) ( (Spinner) findViewById(R.id.spinnerTeam) ).getSelectedItem();
					                	final long favTeamId = teamSelected.getTeamId();					                    
					                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>> teamSelected = " + teamSelected.getTeamId());					                	
					                	final TelephonyManager mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
					    		    	//get deviceID
					    		        if (mTelephony.getDeviceId() != null){
					    		        	uid = mTelephony.getDeviceId(); //use for mobiles
					    		         }
					    		        else{
					    		        	uid = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID); //use for tablets
					    		         }  
					    		        
					    		      // get user data from session						    							    		      
					    		      HashMap<String, String> twitterUser = session.getLoginType();         
					    		      String loginType = twitterUser.get(SessionManager.KEY_LOGIN_TYPE);				    		      
					    		      if(loginType.equalsIgnoreCase("facebook")){
					    		    	  twitterUser = session.getFacebookDetails();
						    		      loginUserName = twitterUser.get(SessionManager.KEY_FACEBOOK_USERNAME);
						    		      loginNickName = twitterUser.get(SessionManager.KEY_FACEBOOK_NICKNAME);
					    		      }
					    		      else{
					    		    	 twitterUser = session.getTwitterDetails();   
					    		    	  loginUserName = twitterUser.get(SessionManager.KEY_TWITTER_TOKEN);
						    		      loginNickName = twitterUser.get(SessionManager.KEY_TWITTER_NICK);
					    		      }					    		      
					    		      final UserDTO user = new UserDTO(loginUserName,uid,countrySelected,loginNickName,"",favTeamId,"");					   		    	
					    		      try {
					    		    	  if(connectionUtility.hasWifi(getBaseContext())){
					    		    		  saveUser(user);	    						
					    					}
					    					else{
												connectionUtility.showToast(TwitterFacebookRegistration.this);
												connectionUtility.setUtilityListener(new ConnectionUtilityListener()  {				
													@Override
													public void onInternetEnabled(boolean result) {
														saveUser(user);
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
	                    }
	                });
	            } catch (Exception e) {
	                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>> e " + e.getMessage());
	            }
	        }
		};
	    // start thread
	    mSplashThread.start();
	}
	
	public void getCountryList(){		
		ArrayList<String> countries = new ArrayList<String>();
		String[] isoCountries = Locale.getISOCountries();
		 for (String country : isoCountries) {
	            Locale locale = new Locale("en", country);
	            String name = locale.getDisplayCountry();
	            if (!"".equals(name)) {
	            	countries.add(name);
	            }
	        }
        Collections.sort(countries);
        ArrayList<String> countriesSorted = new ArrayList<String>();
        countriesSorted.add("Country");
        countriesSorted.addAll(countries);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
        		TwitterFacebookRegistration.this, android.R.layout.simple_spinner_item, countriesSorted);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );  
        countryList = (Spinner) findViewById( R.id.spinnerCountry);
        countryList.setAdapter(spinnerArrayAdapter);
	}
	
	public void getTeamList(){
        TeamService teamService = new TeamService();
        ArrayList<TeamDTO> listFavTeam = new ArrayList<TeamDTO>();
        listFavTeam = teamService.getTeamName(getApplicationContext());        
        ArrayList<TeamDTO> teamList = new ArrayList<TeamDTO>();
        TeamDTO defaultTeam = new TeamDTO();
        defaultTeam.setName("Team");
        teamList.add(defaultTeam);
        teamList.addAll(listFavTeam);
        
        ArrayAdapter<TeamDTO> spinnerArrayAdapter = new ArrayAdapter<TeamDTO>(TwitterFacebookRegistration.this, android.R.layout.simple_spinner_item, teamList);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );       
        favTeamList = (Spinner) findViewById( R.id.spinnerTeam);
        favTeamList.setAdapter(spinnerArrayAdapter);
	}	

	public void saveUser (final UserDTO user){
        CreateUserTask createUserTask = new CreateUserTask();
        createUserTask.setUser(user);
        createUserTask.setContext(getApplicationContext());
        createUserTask.execute();        
        createUserTask.setcreateUserTaskListener(new CreateUserTaskListener() {			
			@Override
			public void onComplete(String result) {
				if (result != "") {   
                    if (result.equalsIgnoreCase("userCreated")) {				
						System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> result" + result);
						session.createLoginSession(user.getUserName(),user.getUid(),user.getNickName(),user.getFavTeam(),user.getCountry());	
						
						//register GCM
						UserService userService = new UserService();
						userService.registerGCM(TwitterFacebookRegistration.this);
												
						Intent matchList = new Intent(getApplicationContext(), GameListActivity.class);
		                // Close all views before launching matchList
		                matchList.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		                startActivity(matchList);                
		                // Close match list View
		                finish();
		             }
                    else{
                    	Toast toast = Toast.makeText(TwitterFacebookRegistration.this,"Username or NickName already exists", Toast.LENGTH_LONG);
	            		toast.setGravity(Gravity.CENTER, 0, 0);
	            		toast.show();
                    }                                 
				}
				else{
					Toast toast = Toast.makeText(TwitterFacebookRegistration.this,"Error occured in registration", Toast.LENGTH_LONG);
            		toast.setGravity(Gravity.CENTER, 0, 0);
            		toast.show();                    
               }				
			}
		});
	}	
}
