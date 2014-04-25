package com.competition.worldcupv1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.competition.worldcupv1.asynctasks.LoginTask;
import com.competition.worldcupv1.asynctasks.LoginTask.LoginTaskListener;
import com.competition.worldcupv1.dto.UserDTO;
import com.competition.worldcupv1.utils.ConnectionUtility;
import com.competition.worldcupv1.utils.ConnectionUtility.ConnectionUtilityListener;

public class LoginActivity extends Activity {
	private Button btnLogin;
	private Button btnLinkToRegister;
	private EditText txtUserName;
	private EditText txtPassword;
	private TextView loginErrorMsg;
    
    // JSON Response node names
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
			
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
    						connectionUtility.showToast(LoginActivity.this);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
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
