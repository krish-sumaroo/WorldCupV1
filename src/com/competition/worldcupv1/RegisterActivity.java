package com.competition.worldcupv1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.competition.worldcupv1.asynctasks.GetRegistrationIdTask;
import com.competition.worldcupv1.asynctasks.GetRegistrationIdTask.GetRegistrationIdTaskListener;
import com.competition.worldcupv1.dto.GameDTO;
import com.competition.worldcupv1.dto.TeamDTO;
import com.competition.worldcupv1.dto.UserDTO;
import com.competition.worldcupv1.service.TeamService;
import com.competition.worldcupv1.service.UserService;
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
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);	
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
					            		
					                    if(!checkEmailCorrect(userName)){
					                    	Toast toast = Toast.makeText(RegisterActivity.this,"Invalid Email Addresss", Toast.LENGTH_LONG);
					                    	toast.setGravity(Gravity.CENTER, 0, 0);
					                    	toast.show();
					                    }
					                    else
					                    {
					                	
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
					    		        					    		    	
					    		    	final UserDTO user = new UserDTO(userName,uid,countrySelected,nickName,password,favTeamId,"");					   		    	
					    		    	try {
					    					if(connectionUtility.hasWifi(getBaseContext())){
					    						progressDialog = new ProgressDialog(RegisterActivity.this);
					    						progressDialog.setMessage("Loading ...");
					    						saveUser(user);	    						
					    					}
					    					else{
					    						connectionUtility.showToast(RegisterActivity.this);
					    						connectionUtility.setUtilityListener(new ConnectionUtilityListener()  {				
					    							@Override
					    							public void onInternetEnabled(boolean result) {
					    								progressDialog = new ProgressDialog(RegisterActivity.this);
					    								progressDialog.setMessage("Loading ...");
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

	public void saveUser (final UserDTO user){
        CreateUserTask createUserTask = new CreateUserTask();
        createUserTask.setUser(user);
        createUserTask.setContext(getApplicationContext());
        createUserTask.execute();        
        createUserTask.setcreateUserTaskListener(new CreateUserTaskListener() {			
			@Override
			public void onComplete(String result) {
				progressDialog.dismiss();
				if (result != "") {   
                    if (result.equalsIgnoreCase("userCreated")) {				
						System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> result" + result);
						session.createLoginSession(user.getUserName(),user.getUid(),user.getNickName(),user.getFavTeam(),user.getCountry());	
						
						//register GCM
						UserService userService = new UserService();
						userService.registerGCM(RegisterActivity.this);
						
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

    boolean checkEmailCorrect(String Email) {
        String pttn = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern p = Pattern.compile(pttn);
        Matcher m = p.matcher(Email);

        if (m.matches()) {
               return true;
        }
        return false;
    }
}