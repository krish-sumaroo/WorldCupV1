package com.competition.worldcupv1.webServicehelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.competition.worldcupv1.databasehelper.DatabaseHelper;
import com.competition.worldcupv1.dto.UserDTO;
import com.competition.worldcupv1.utils.WebServiceUtility;

public class WebServiceHelper {	
	private DatabaseHelper dbHelper;  
	
	public WebServiceHelper() {
		// TODO Auto-generated constructor stub
	}
	
	//create new user
	public String createUser(Context context, UserDTO user) throws ClientProtocolException, IOException, JSONException{
		String url = "users/join/";
		String result="";
      
    	// Add data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
        nameValuePairs.add(new BasicNameValuePair("username", user.getUserName()));
        nameValuePairs.add(new BasicNameValuePair("password", user.getPassword()));
        nameValuePairs.add(new BasicNameValuePair("nickname", user.getNickName()));
        nameValuePairs.add(new BasicNameValuePair("favTeam", String.valueOf(user.getFavTeam())));
        nameValuePairs.add(new BasicNameValuePair("uid", user.getUid()));
        nameValuePairs.add(new BasicNameValuePair("country", user.getCountry()));       
        
        //use the generic list fn to post JSON obj
		WebServiceUtility webServiceUtility = new WebServiceUtility();
		JSONObject jObject = webServiceUtility.postData(nameValuePairs, url);
		
        if(jObject.getString("status").equals("true")){  	
        	result= "userCreated";
        }else{
        	Log.d("status:","false");
        	String errorMsg = jObject.getString("msg");
        	result= errorMsg;
        }
		return result;
	}
	
	//login
	public String login(Context context, UserDTO user) throws ClientProtocolException, IOException, JSONException{
		String url = "users/login/";
		String result="";
      
    	// Add data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
        nameValuePairs.add(new BasicNameValuePair("username", user.getUserName()));
        nameValuePairs.add(new BasicNameValuePair("password", user.getPassword()));
        
        //use the generic list fn to post JSON obj
		WebServiceUtility webServiceUtility = new WebServiceUtility();
		JSONObject jObject = webServiceUtility.postData(nameValuePairs, url);
		
		System.out.println(">>>>>>>>>>>>>>> result login = " +jObject.getString("status") );
		
        if(jObject.getString("status").equals("true")){  	
        	result= "loginSuccess";
        }else{
        	Log.d("status:","false");
        	result= "loginFailed";
        }
		return result;
	}
	
	//login
		public String checkUserName(Context context, UserDTO user) throws ClientProtocolException, IOException, JSONException{
			String url = "users/checkUsername/";
			String result="";
	      
	    	// Add data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
	        nameValuePairs.add(new BasicNameValuePair("username", user.getUserName()));
	        
	        //use the generic list fn to post JSON obj
			WebServiceUtility webServiceUtility = new WebServiceUtility();
			JSONObject jObject = webServiceUtility.postData(nameValuePairs, url);			
			System.out.println(">>>>>>>>>>>>>>> result login = " +jObject.getString("status") );
			
	        if(jObject.getString("status").equals("true")){  	
	        	result= "userNameNotExist";
	        }else{
	        	Log.d("status:","false");
	        	result= "userNameExist";
	        }
			return result;
		}
}
