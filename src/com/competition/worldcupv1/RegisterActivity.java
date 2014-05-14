package com.competition.worldcupv1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.competition.worldcupv1.asynctasks.CreateGameInfoTask;
import com.competition.worldcupv1.asynctasks.CreateGameInfoTask.CreateGameInfoTaskListener;
import com.competition.worldcupv1.asynctasks.CreateUserTask;
import com.competition.worldcupv1.asynctasks.CreateUserTask.CreateUserTaskListener;
import com.competition.worldcupv1.dto.GameDTO;
import com.competition.worldcupv1.dto.TeamDTO;
import com.competition.worldcupv1.dto.UserDTO;
import com.competition.worldcupv1.service.TeamService;
import com.competition.worldcupv1.utils.ConnectionUtility;
import com.competition.worldcupv1.utils.ConnectionUtility.ConnectionUtilityListener;
import com.competition.worldcupv1.utils.SessionManager;
import com.google.android.gcm.GCMRegistrar;

public class RegisterActivity extends Activity {
	Button btnRegister;
    Button btnLinkToLogin;
    EditText inputUserName;
    TextView registerErrorMsg;
    String uid = "";
    Button btnRegisterUser;
	private EditText txtUserName;
	private EditText txtNickName;
	private EditText txtPassword;
    private Spinner countryList;
    private Spinner favTeamList;    
    private Thread thread;
    SessionManager session;
    ImageView imageInfoUsr;
    ImageView imageInfoCountry;
    ImageView imageInfoTeam;
    
    // Asyntask
    AsyncTask<Void, Void, Void> mRegisterTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);		
		
		
		//test ();
		
		
		//Session Manager
        session = new SessionManager(getApplicationContext());
		thread = new Thread() {    
	        public void run() {
	            try {
	            	runOnUiThread(new Runnable() {  
	                    @Override
	                    public void run() {
				            countryList = (Spinner) findViewById( R.id.spinnerCountry);
				            favTeamList = (Spinner) findViewById( R.id.spinnerTeam);
				            btnRegisterUser = (Button) findViewById(R.id.btnRegisterSave);
				            btnLinkToLogin = (Button) findViewById(R.id.btnRegisterCancel);
				            txtUserName = (EditText) findViewById( R.id.editTextRegUsrName);
				            txtNickName = (EditText) findViewById( R.id.editTextNickName);
				            txtPassword = (EditText) findViewById( R.id.editTextPwd);
				            imageInfoUsr = (ImageView) findViewById( R.id.imageInfoUsr);
				            imageInfoCountry = (ImageView) findViewById( R.id.imageInfoCountry);
				            imageInfoTeam = (ImageView) findViewById( R.id.imageInfoTeam);				            
				            
				            btnRegisterUser.setOnClickListener(new OnClickListener() {	
								@SuppressWarnings("unused")
								@Override
								public void onClick(View v) {						
									String country = countryList.getSelectedItem().toString();
					            	String favTeam = favTeamList.getSelectedItem().toString();
					            	final ConnectionUtility connectionUtility = new ConnectionUtility();
					            	if(( txtUserName.length() == 0 || txtUserName.equals("") || txtUserName == null) || (txtNickName.length() == 0 || txtNickName.equals("") || txtNickName == null) || (txtPassword.length() == 0 || txtPassword.equals("") || txtPassword == null) || (country.equalsIgnoreCase("Country")) || (favTeam.equalsIgnoreCase("Team")))
					                {    		    	 
					            		Toast toast = Toast.makeText(RegisterActivity.this,"Please fill in all the fields", Toast.LENGTH_LONG);
					            		toast.setGravity(Gravity.CENTER, 0, 0);
					            		toast.show();
					                }
					            	else{
					                	final String userName = txtUserName.getText().toString();
					                	final String nickName = txtNickName.getText().toString();
					                	final String password = txtPassword.getText().toString();
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
					    		        

					    		        
					    		    	final UserDTO user = new UserDTO(userName,uid,countrySelected,nickName,password,favTeamId);		
					    		    	//************************** GCM ************************************
					    		    	
					    		        // GCM
					    		        // Make sure the device has the proper dependencies.
					    		        GCMRegistrar.checkDevice(RegisterActivity.this);
					    		 
					    		        // Make sure the manifest permissions was properly set
					    		        GCMRegistrar.checkManifest(RegisterActivity.this);
					    		        
					    		     
										// Register custom Broadcast receiver to show messages on activity
					    		        registerReceiver(mHandleMessageReceiver, new IntentFilter(
					    		                Config.DISPLAY_MESSAGE_ACTION));
					    		        
					    		     // Get GCM registration id
					    		        final String regId = GCMRegistrar.getRegistrationId(RegisterActivity.this);
					    		        String regId2="";
					    		        
					    		        
					    		     // Check if regid already presents
					    		        if (regId.equals("")) {
					    		             
					    		            // Register with GCM           
					    		            GCMRegistrar.register(RegisterActivity.this, Config.GOOGLE_SENDER_ID);
					    		            
					    		            regId2 = GCMRegistrar.getRegistrationId(RegisterActivity.this);
					    		            
					    		            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> regId2 = " + regId2);
					    		             
					    		        } else {
					    		             
					    		            // Device is already registered on GCM Server
					    		            if (GCMRegistrar.isRegisteredOnServer(RegisterActivity.this)) {
					    		                 
					    		                // Skips registration.             
					    		                Toast.makeText(getApplicationContext(),
					    		                              "Already registered with GCM Server",
					    		                              Toast.LENGTH_LONG).
					    		                              show();
					    		             
					    		            } else {
					    		                 
					    		                // Try to register again, but not in the UI thread.
					    		                // It's also necessary to cancel the thread onDestroy(),
					    		                // hence the use of AsyncTask instead of a raw thread.
					    		                 
					    		            	user.setRegId(regId);
					    		            	try {
							    					if(connectionUtility.hasWifi(getBaseContext())){
							    						saveUser(user);	    						
							    					}
							    					else{
							    						connectionUtility.showToast(RegisterActivity.this);
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
					    		      					    		              
					                }
								}
							});	
				            
				            imageInfoUsr.setOnClickListener(new View.OnClickListener() { 
				                public void onClick(View view) {
				                	Toast toast = Toast.makeText(RegisterActivity.this,"Please enter your email address", Toast.LENGTH_LONG);
				            		toast.setGravity(Gravity.CENTER, 0, 0);
				            		toast.show();
				                }
				            });
				            
				            imageInfoCountry.setOnClickListener(new View.OnClickListener() { 
				                public void onClick(View view) {
				                	Toast toast = Toast.makeText(RegisterActivity.this,"Please select your country", Toast.LENGTH_LONG);
				            		toast.setGravity(Gravity.CENTER, 0, 0);
				            		toast.show();
				                }
				            });
				            
				            imageInfoTeam.setOnClickListener(new View.OnClickListener() { 
				                public void onClick(View view) {
				                	Toast toast = Toast.makeText(RegisterActivity.this,"Please select your favorite team", Toast.LENGTH_LONG);
				            		toast.setGravity(Gravity.CENTER, 0, 0);
				            		toast.show();
				                }
				            });
				        	
				            // Link to Login Screen
				            btnLinkToLogin.setOnClickListener(new View.OnClickListener() { 
				                public void onClick(View view) {
				                    // Close Registration View
				                	Intent i = new Intent(getApplicationContext(),MainActivity.class);
						            startActivity(i);
						            finish();
				                }
				            });
				            insertTeamList();
				            getCountryList();
				            getTeamList();
	                    }
	                });
	            } catch (Exception e) {
	                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>> e " + e.getMessage());
	            }
	        }
		};
	    // start thread
	    thread.start();
	}
	
	public void  test (){
		CreateGameInfoTask createUserTask = new CreateGameInfoTask();
        createUserTask.setContext(getApplicationContext());
        createUserTask.execute();        
        createUserTask.setCreateGameInfoTaskListener(new CreateGameInfoTaskListener() {

			@Override
			public void onComplete(GameDTO result) {
				// TODO Auto-generated method stub
				
			}});
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
						Intent matchList = new Intent(getApplicationContext(), GameListActivity.class);
		                // Close all views before launching matchList
		                matchList.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		                startActivity(matchList);                
		                // Close match list View
		                finish();
		             }
                    if(result.equalsIgnoreCase("Username or Nickname already exists")){
                    	// Error in registration
                    	Toast toast = Toast.makeText(RegisterActivity.this,result, Toast.LENGTH_LONG);
	            		toast.setGravity(Gravity.CENTER, 0, 0);
	            		toast.show();
                    }                   
				}
				else{
                    // Error in registration
					Toast toast = Toast.makeText(RegisterActivity.this,"Error occured in registration", Toast.LENGTH_LONG);
            		toast.setGravity(Gravity.CENTER, 0, 0);
            		toast.show();
               }				
			}
		});
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
        		RegisterActivity.this, android.R.layout.simple_spinner_item, countriesSorted);
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
        
        ArrayAdapter<TeamDTO> spinnerArrayAdapter = new ArrayAdapter<TeamDTO>(RegisterActivity.this, android.R.layout.simple_spinner_item, teamList);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );       
        favTeamList = (Spinner) findViewById( R.id.spinnerTeam);
        favTeamList.setAdapter(spinnerArrayAdapter);
	}	
	public void insertTeamList(){
       // TeamService teamService = new TeamService();
        //teamService.insertTeamsData(RegisterActivity.this);
	}
	
	  // Create a broadcast receiver to get message and show on screen
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
         
        @Override
        public void onReceive(Context context, Intent intent) {
             
            String newMessage = intent.getExtras().getString(Config.EXTRA_MESSAGE);
             
            // Waking up mobile if it is sleeping
            acquireWakeLock(getApplicationContext());
             
            // Display message on the screen
            //lblMessage.append(newMessage + "");        
             
            Toast.makeText(getApplicationContext(),
                           "Got Message: " + newMessage,
                           Toast.LENGTH_LONG).show();
             
            // Releasing wake lock
            releaseWakeLock();
        }
    };
    
    private PowerManager.WakeLock wakeLock;
    
    public  void acquireWakeLock(Context context) {
        if (wakeLock != null) wakeLock.release();
 
        PowerManager pm = (PowerManager)
                          context.getSystemService(Context.POWER_SERVICE);
         
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "WakeLock");
         
        wakeLock.acquire();
    }
 
    public  void releaseWakeLock() {
        if (wakeLock != null) wakeLock.release(); wakeLock = null;
    }
    
    @Override
    protected void onDestroy() {
        // Cancel AsyncTask
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            // Unregister Broadcast Receiver
            unregisterReceiver(mHandleMessageReceiver);
             
            //Clear internal resources.
            GCMRegistrar.onDestroy(this);
             
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error",
                      "> " + e.getMessage());
        }
        super.onDestroy();
    }
}