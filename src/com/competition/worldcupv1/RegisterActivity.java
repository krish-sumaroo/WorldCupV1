package com.competition.worldcupv1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.competition.worldcupv1.asynctasks.CreateUserTask;
import com.competition.worldcupv1.asynctasks.CreateUserTask.CreateUserTaskListener;
import com.competition.worldcupv1.dto.UserDTO;
import com.competition.worldcupv1.utils.ConnectionUtility;
import com.competition.worldcupv1.utils.ConnectionUtility.ConnectionUtilityListener;
import com.competition.worldcupv1.webServicehelper.WebServiceHelper;

public class RegisterActivity extends Activity {
	Button btnRegister;
    Button btnLinkToLogin;
    EditText inputUserName;
    TextView registerErrorMsg;
    String uid = "";
    String countryCode ="";
    String resultString="";
    String country ="";
    
    // JSON Response node names
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_CREATED_AT = "created_at";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		// Importing all assets like buttons, text fields
        inputUserName = (EditText) findViewById(R.id.registerName);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
        //registerErrorMsg = (TextView) findViewById(R.id.register_error);
        
        final ConnectionUtility connectionUtility = new ConnectionUtility();					
        
        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {        
            public void onClick(View view) {
            	
            	if(( inputUserName.length() == 0 || inputUserName.equals("") || inputUserName == null))
                {    		    	
            		Toast.makeText(getApplicationContext(), "Please enter a user name", Toast.LENGTH_LONG).show(); 
                }
            	else{
                	final String userName = inputUserName.getText().toString();
                	final TelephonyManager mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    		    	countryCode = mTelephony.getNetworkCountryIso();
    		    	//get deviceID
    		        if (mTelephony.getDeviceId() != null){
    		        	uid = mTelephony.getDeviceId(); //*** use for mobiles
    		         }
    		        else{
    		        	uid = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID); //*** use for tablets
    		         }   		    	
    		    	countryCode = "1";
    		    	final UserDTO user = new UserDTO();
    		    	user.setName(userName);
    		    	user.setUid(uid);
    		    	user.setCountry(countryCode);
   		    	
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
    								Intent matchList = new Intent(getApplicationContext(), MatchListActivity.class);
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
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                // Close Registration View
                finish();
            }
        });
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
						Intent matchList = new Intent(getApplicationContext(), MatchListActivity.class);
		                // Close all views before launching matchList
		                matchList.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		                startActivity(matchList);                
		                // Close match list View
		                finish();
		             }
				}
				else{
                    // Error in registration
                    registerErrorMsg.setText("Error occured in registration");
               }				
			}
		});
	}
}
