package com.competition.worldcupv1.webServicehelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.competition.worldcupv1.databasehelper.DatabaseHelper;
import com.competition.worldcupv1.dto.FriendDTO;
import com.competition.worldcupv1.dto.TeamDTO;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class WebServiceHelper {	
	private DatabaseHelper dbHelper;
	private Context context;
	private static final String webServiceURL = "http://10.0.2.2/WorldCup_web/index.php/resource/";

//	public WebServiceHelper(Context context) {
//		this.context = context;
//	}
	
	public WebServiceHelper() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	public List<TeamDTO>  extractTeamPlayersData() throws ClientProtocolException, IOException, JSONException{		
		List<TeamDTO> listTeam = new ArrayList<TeamDTO>();
		listTeam = null;				
		String url = webServiceURL+"initialise";
		HttpClient httpclient = new DefaultHttpClient();
	 
		try{
			HttpPost httppost = new HttpPost(url);
	        ResponseHandler<String> responseHandler = new BasicResponseHandler();
	        String responseBody = httpclient.execute(httppost, responseHandler);
	        JSONArray jArray = new JSONArray(responseBody);
	
	        //get team data
	        for (int i = 0; i < jArray.length(); i++) {        		        	
	        	//get row
	        	JSONObject jsonObj = jArray.getJSONObject(i);
	        	 
	        	int teamId = jsonObj.getInt("id");
	        	String teamName = jsonObj.getString("teamName");
	        	String coach = jsonObj.getString("coach");
	        	String countryCode = "MRU";
	        	//String group = jsonObj.getString("team_group");        	
	        	
	        	StringBuilder insertOrReplaceTeam = new StringBuilder();
	
				insertOrReplaceTeam.append(" INSERT OR REPLACE INTO team (_id, team_name, country_code, coach) ");
				insertOrReplaceTeam.append(" VALUES ( ");
				insertOrReplaceTeam.append(teamId+ ", '");
				insertOrReplaceTeam.append(teamName + "', '");
				insertOrReplaceTeam.append(countryCode + "', '");
				insertOrReplaceTeam.append(coach + "'); ");
	
				System.out.println(insertOrReplaceTeam.toString());
				dbHelper.executeSql(insertOrReplaceTeam.toString());
	        	
				//get players data
	        	JSONArray playersArray = jsonObj.getJSONArray("player");        	
	        	for (int j = 0; j < playersArray.length(); j++) {  
	        		
	        		JSONObject playerJsonObj = playersArray.getJSONObject(j);
	        	
		        	//get players per team
		        	int playerId = playerJsonObj.getInt("id");
		        	String playerName = playerJsonObj.getString("name");
		        	String position = playerJsonObj.getString("position");
		        	int number = playerJsonObj.getInt("number");
		        	
		        	StringBuilder insertOrReplacePlayer = new StringBuilder();
		
					insertOrReplacePlayer.append(" INSERT OR REPLACE INTO player (_id, player_name, team_id, position, number ) ");
					insertOrReplacePlayer.append(" VALUES ( ");
					insertOrReplacePlayer.append(playerId+ ", '");
					insertOrReplacePlayer.append(playerName + "', ");
					insertOrReplacePlayer.append(teamId + ", '");
					insertOrReplacePlayer.append(position +  "', ");
					insertOrReplacePlayer.append(number + "); ");
					
					System.out.println(insertOrReplacePlayer.toString());
					dbHelper.executeSql(insertOrReplacePlayer.toString());				
	        	}
	        }
		}
		finally {
	    	httpclient.getConnectionManager().shutdown();
	    }
        return listTeam;
	}
	
	@SuppressWarnings("unchecked")
	public void extractTeamData() throws ClientProtocolException, IOException, JSONException{
		String url = webServiceURL+"/getTeamList.php";
		HttpClient httpclient = new DefaultHttpClient();
	 
		try{
			HttpPost httppost = new HttpPost(url);
	        ResponseHandler<String> responseHandler = new BasicResponseHandler();
	        String responseBody = httpclient.execute(httppost, responseHandler);
	        JSONArray jArray = new JSONArray(responseBody);
		        
	        for (int i = 0; i < jArray.length(); i++) {        		        	
	        	//get row
	        	JSONObject jsonObj = jArray.getJSONObject(i);
	        	 
	        	int teamId = jsonObj.getInt("team_id");
	        	String teamName = jsonObj.getString("team_name");
	        	String countryCode = jsonObj.getString("country_code");
	        	String group = jsonObj.getString("team_group");
	        	        	
	        	StringBuilder insertOrReplaceTeam = new StringBuilder();
	
				insertOrReplaceTeam.append(" INSERT OR REPLACE INTO team (_id, team_name, country_code, group) ");
				insertOrReplaceTeam.append(" VALUES ( ");
				insertOrReplaceTeam.append(teamId+ ", '");
				insertOrReplaceTeam.append(teamName + "', '");
				insertOrReplaceTeam.append(countryCode + "', '");
				insertOrReplaceTeam.append(group + "'); ");
	
				System.out.println(insertOrReplaceTeam.toString());
				dbHelper.executeSql(insertOrReplaceTeam.toString());
	        }
		}
		finally {
	    	httpclient.getConnectionManager().shutdown();
	    }
	}
	
	public void extractPlayerData() throws ClientProtocolException, IOException, JSONException{
		String url = webServiceURL+"/getPlayerList.php";
		HttpClient httpclient = new DefaultHttpClient();
	 
		try{
			HttpPost httppost = new HttpPost(url);
	        ResponseHandler<String> responseHandler = new BasicResponseHandler();
	        String responseBody = httpclient.execute(httppost, responseHandler);
	        JSONArray jArray = new JSONArray(responseBody);
		        
	        for (int i = 0; i < jArray.length(); i++) {        		        	
	        	//get row
	        	JSONObject jsonObj = jArray.getJSONObject(i);
	        	 
	        	int playerId = jsonObj.getInt("player_id");
	        	String playerName = jsonObj.getString("player_name");
	        	int teamId = jsonObj.getInt("team_id");
	        	String position = jsonObj.getString("position");
	        	int number = jsonObj.getInt("number");
	        	
	        	StringBuilder insertOrReplacePlayer = new StringBuilder();
	
				insertOrReplacePlayer.append(" INSERT OR REPLACE INTO player (_id, player_name, team_id, position, number ) ");
				insertOrReplacePlayer.append(" VALUES ( ");
				insertOrReplacePlayer.append(playerId+ ", '");
				insertOrReplacePlayer.append(playerName + "', ");
				insertOrReplacePlayer.append(teamId + ", '");
				insertOrReplacePlayer.append(position +  "', ");
				insertOrReplacePlayer.append(number + "); ");
	
				System.out.println(insertOrReplacePlayer.toString());
				dbHelper.executeSql(insertOrReplacePlayer.toString());				
	        }
		}
		finally {
	    	httpclient.getConnectionManager().shutdown();
	    }
	}
	
	public void extractMatchData() throws ClientProtocolException, IOException, JSONException{
		String url = webServiceURL+"/getMatchList.php";
		HttpClient httpclient = new DefaultHttpClient();
	 
		HttpPost httppost = new HttpPost(url);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = httpclient.execute(httppost, responseHandler);
        JSONArray jArray = new JSONArray(responseBody);
	        
        for (int i = 0; i < jArray.length(); i++) {        		        	
        	//get row
        	JSONObject jsonObj = jArray.getJSONObject(i);
        	 
        	int matchId = jsonObj.getInt("match_id");
        	int homeTeamId = jsonObj.getInt("home_team_id");
        	int awayTeamId = jsonObj.getInt("away_team_id");
        	String status = jsonObj.getString("status");
        	String kickOff = jsonObj.getString("kick_off");
        	String venue = jsonObj.getString("venue");
        	
        	StringBuilder insertOrReplaceMatch = new StringBuilder();

			insertOrReplaceMatch.append(" INSERT OR REPLACE INTO match (_id, home_team_id, away_team_id, status, kick_off, venue) ");
			insertOrReplaceMatch.append(" VALUES ( ");
			insertOrReplaceMatch.append(matchId+ ", ");
			insertOrReplaceMatch.append(homeTeamId + ", ");
			insertOrReplaceMatch.append(awayTeamId + ", '");
			insertOrReplaceMatch.append(status + "', '");
			insertOrReplaceMatch.append(kickOff + "', '");
			insertOrReplaceMatch.append(venue + "'); ");

			System.out.println(insertOrReplaceMatch.toString());
			dbHelper.executeSql(insertOrReplaceMatch.toString());
        }
	}
	
	//create new user
	public String createUser(String name, String uid, String country, Context context) throws ClientProtocolException, IOException, JSONException{
		String url = webServiceURL+"crUser";
        HttpClient httpclient = new DefaultHttpClient();        
        try {        	
        	// Add data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("name", name));
            nameValuePairs.add(new BasicNameValuePair("uid", uid));
            nameValuePairs.add(new BasicNameValuePair("country", country));
            
	        HttpPost httppost = new HttpPost(url);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));        	
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httppost, responseHandler);          
            JSONObject jObject = new JSONObject(responseBody);            
          
            if(jObject.getString("status").equals("true")){  	
            	return "userCreated";
            }else{
            	Log.d("status:","false");
            	return "saveFailed";
            }          
        }
        finally {
	    	httpclient.getConnectionManager().shutdown();
	    }
	}
	
	// List Friends
	@SuppressWarnings("unchecked")
	public List<FriendDTO>  extractListFriends() throws ClientProtocolException, IOException, JSONException{		
		List<FriendDTO> listFriends = new ArrayList<FriendDTO>();		
		String url = webServiceURL+"listConnections";
		HttpClient httpclient = new DefaultHttpClient();	 
		try{
			HttpPost httppost = new HttpPost(url);
	        ResponseHandler<String> responseHandler = new BasicResponseHandler();
	        String responseBody = httpclient.execute(httppost, responseHandler);
	        JSONArray jArray = new JSONArray(responseBody);
		        
	        for (int i = 0; i < jArray.length(); i++) {        		        	
	        	//get row
	        	JSONObject jsonObj = jArray.getJSONObject(i);
	        	 
	        	int id = jsonObj.getInt("id");
	        	String name = jsonObj.getString("name");
	        	
	        	FriendDTO friendDTO = new FriendDTO(id, name);
	        	listFriends.add(friendDTO);
	        	
	        	System.out.print(">>>>>>>>>>>>>>> id = " + id +">>>>>>>>> name = " + name );        	
	        	Log.d("extractListConnections======", name);
	        }
		}
		finally{
        	httpclient.getConnectionManager().shutdown();
        }
		return listFriends;
	}
	
	// Activate user to premium
	public String actUserPremium(String uid, Context context) throws ClientProtocolException, IOException, JSONException{
		String url = webServiceURL+"userAc";
        HttpClient httpclient = new DefaultHttpClient();        
        try {        	
        	// Add data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("uid", uid));
            
	        HttpPost httppost = new HttpPost(url);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));        	
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httppost, responseHandler);          
            JSONObject jObject = new JSONObject(responseBody);            
          
            if(jObject.getString("status").equals("true")){  	
            	return "userPremiumActivated";
            }else{
            	Log.d("status:","false");
            	return "saveFailed";
            }          
        }
        finally {
	    	httpclient.getConnectionManager().shutdown();
	    }
	}
	
	// Accept friend request
	public void accFriendRequest(String uid, Context context) throws ClientProtocolException, IOException, JSONException{
		String url = webServiceURL+"join";
        HttpClient httpclient = new DefaultHttpClient();        
        try {        	
        	// Add data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("uid", uid));
            
	        HttpPost httppost = new HttpPost(url);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));        	
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httppost, responseHandler);
        }
        finally {
	    	httpclient.getConnectionManager().shutdown();
	    }
	}	
}
