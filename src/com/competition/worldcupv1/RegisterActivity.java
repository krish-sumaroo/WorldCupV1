package com.competition.worldcupv1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.competition.worldcupv1.asynctasks.CreateUserTask;
import com.competition.worldcupv1.asynctasks.CreateUserTask.CreateUserTaskListener;
import com.competition.worldcupv1.dto.UserDTO;
import com.competition.worldcupv1.service.TeamService;
import com.competition.worldcupv1.utils.ConnectionUtility;
import com.competition.worldcupv1.utils.ConnectionUtility.ConnectionUtilityListener;

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
    private Thread mSplashThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);         
		
		mSplashThread = new Thread() {    
	        public void run() {
	            try {
	            	runOnUiThread(new Runnable() {  
	                    @Override
	                    public void run() {
	                    	
	                    	
	                    	
	                      				// Thread will sleep for 5 seconds
			//sleep(1*1000);
          
			// After 5 seconds redirect to another intent     		
			// populate the database
          
			// Importing all assets like buttons, text fields
            countryList = (Spinner) findViewById( R.id.spinnerCountry);
           // countryList.setPromptId(R.string.default_country);
            favTeamList = (Spinner) findViewById( R.id.spinnerTeam);
            btnRegisterUser = (Button) findViewById(R.id.btnRegisterSave);
            btnLinkToLogin = (Button) findViewById(R.id.btnRegisterCancel);
            txtUserName = (EditText) findViewById( R.id.editTextRegUsrName);
            txtNickName = (EditText) findViewById( R.id.editTextNickName);
            txtPassword = (EditText) findViewById( R.id.editTextPwd);
            
            btnRegisterUser.setOnClickListener(new OnClickListener() {	
				@Override
				public void onClick(View v) {						
					String country = countryList.getSelectedItem().toString();
	            	String favTeam = favTeamList.getSelectedItem().toString();
	            	final ConnectionUtility connectionUtility = new ConnectionUtility();
	            	if(( txtUserName.length() == 0 || txtUserName.equals("") || txtUserName == null) || (txtNickName.length() == 0 || txtNickName.equals("") || txtNickName == null) || (txtPassword.length() == 0 || txtPassword.equals("") || txtPassword == null) || (country.equalsIgnoreCase("Country")) || (favTeam.equalsIgnoreCase("Team")))
	                {    		    	
	            		Toast.makeText(getApplicationContext(), "Please fill in all the fields", Toast.LENGTH_LONG).show(); 
	                }
	            	else{
	                	final String userName = txtUserName.getText().toString();
	                	final String nickName = txtNickName.getText().toString();
	                	final String password = txtPassword.getText().toString();
	                	final String countrySelected = countryList.getSelectedItem().toString();
	                	final String favTeamSelected = favTeamList.getSelectedItem().toString();
	                	
	                	final TelephonyManager mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
	    		    	//get deviceID
	    		        if (mTelephony.getDeviceId() != null){
	    		        	uid = mTelephony.getDeviceId(); //use for mobiles
	    		         }
	    		        else{
	    		        	uid = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID); //use for tablets
	    		         }   		    	
	    		    	final UserDTO user = new UserDTO(userName,uid,countrySelected,nickName,password,favTeamSelected);
	   		    	
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
        	
            // Link to Login Screen
            btnLinkToLogin.setOnClickListener(new View.OnClickListener() { 
                public void onClick(View view) {
                    //Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                    //startActivity(i);
                    // Close Registration View
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
    mSplashThread.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}
	
	public void saveUser (UserDTO user){
        CreateUserTask createUserTask = new CreateUserTask();
        createUserTask.setUser(user);
        createUserTask.setContext(getApplicationContext());
        createUserTask.execute();        
        createUserTask.setcreateUserTaskListener(new CreateUserTaskListener() {
			
			@Override
			public void onComplete(String result) {
				if (result != "") {   
                    if (result == "userCreated") {				
						System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> result" + result);
						Intent matchList = new Intent(getApplicationContext(), GameListActivity.class);
		                // Close all views before launching matchList
		                matchList.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		                startActivity(matchList);                
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
	
	public void getCountryList(){		
		Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
       // countries.add("Country");
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length()>0 && !countries.contains(country)) {
                countries.add(country);
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
        ArrayList<String> listTeamName = new ArrayList<String>();
        //listTeamName.add("Team");
        listTeamName = teamService.getTeamName(getApplicationContext());
        
        ArrayList<String> teamList = new ArrayList<String>();
        teamList.add("Team");
        teamList.addAll(listTeamName);
        
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_item, teamList);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );       
        favTeamList = (Spinner) findViewById( R.id.spinnerTeam);
        favTeamList.setAdapter(spinnerArrayAdapter);
	}
	
	public void insertTeamList(){
        TeamService teamService = new TeamService();
        teamService.insertTeamsData(RegisterActivity.this);
	}
}
