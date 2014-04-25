package com.competition.worldcupv1.webServicehelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.competition.worldcupv1.databasehelper.DatabaseHelper;
import com.competition.worldcupv1.dto.FriendDTO;
import com.competition.worldcupv1.dto.TeamDTO;
import com.competition.worldcupv1.dto.UserDTO;
import com.competition.worldcupv1.utils.WebServiceUtility;

public class WebServiceHelper {	
	private DatabaseHelper dbHelper;  

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
		String url = "initialise";
		//use the generic list fn to retrive JSON array
		WebServiceUtility webServiceUtility = new WebServiceUtility();
		JSONArray jArray = webServiceUtility.extractList(url);
		
		if(jArray.length()!=0){	
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
        return listTeam;
	}
	
	@SuppressWarnings("unchecked")
	public void extractTeamData() throws ClientProtocolException, IOException, JSONException{
		String url = "getTeamList.php";
		//use the generic list fn to retrive JSON array
		WebServiceUtility webServiceUtility = new WebServiceUtility();
		JSONArray jArray = webServiceUtility.extractList(url);
		
		if(jArray.length()!=0){		        
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
	}
	
	public void extractPlayerData() throws ClientProtocolException, IOException, JSONException{
		String url = "getPlayerList.php";
		//use the generic list fn to retrive JSON array
		WebServiceUtility webServiceUtility = new WebServiceUtility();
		JSONArray jArray = webServiceUtility.extractList(url);
		
		if(jArray.length()!=0){
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
	}
	
	public void extractMatchData() throws ClientProtocolException, IOException, JSONException{
		String url = "getMatchList.php";
		//use the generic list fn to retrive JSON array
		WebServiceUtility webServiceUtility = new WebServiceUtility();
		JSONArray jArray = webServiceUtility.extractList(url);
		
		if(jArray.length()!=0){
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
	}
	
	
	//create new user
	public String createUser(Context context, UserDTO user) throws ClientProtocolException, IOException, JSONException{
		String url = "crUser";
		String result="";
      
    	// Add data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
        nameValuePairs.add(new BasicNameValuePair("userName", user.getUserName()));
        nameValuePairs.add(new BasicNameValuePair("uid", user.getUid()));
        nameValuePairs.add(new BasicNameValuePair("country", user.getCountry()));
        nameValuePairs.add(new BasicNameValuePair("nickName", user.getNickName()));
        nameValuePairs.add(new BasicNameValuePair("password", user.getPassword()));
        nameValuePairs.add(new BasicNameValuePair("favTeam", user.getFavTeam()));
        
        //use the generic list fn to post JSON obj
		WebServiceUtility webServiceUtility = new WebServiceUtility();
		JSONObject jObject = webServiceUtility.postData(nameValuePairs, url);
		
        if(jObject.getString("status").equals("true")){  	
        	result= "userCreated";
        }else{
        	Log.d("status:","false");
        	result= "saveFailed";
        }
		return result;
	}
	
	// List Friends
	@SuppressWarnings("unchecked")
	public List<FriendDTO>  extractListFriends() throws ClientProtocolException, IOException, JSONException{		
		String url = "listConnections";
		List<FriendDTO> listFriends = new ArrayList<FriendDTO>();
		//use the generic list fn to retrive JSON array
		WebServiceUtility webServiceUtility = new WebServiceUtility();
		JSONArray jArray = webServiceUtility.extractList(url);
		
		if(jArray.length()!=0){
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
		return listFriends;
	}
	
	// Activate user to premium
	public String actUserPremium(String uid, Context context) throws ClientProtocolException, IOException, JSONException{
		String url = "userAc";
		String result="";
       	// Add data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("uid", uid));
        
        //use the generic list fn to post JSON obj
		WebServiceUtility webServiceUtility = new WebServiceUtility();
		JSONObject jObject = webServiceUtility.postData(nameValuePairs, url);
		
		if(jObject !=null){
	        if(jObject.getString("status").equals("true")){
	        	result = "userPremiumActivated";
	
	        }else{
	        	Log.d("status:","false");
	        	result ="saveFailed";
	        }          
		}
		return result;   
	}
	
	// Accept friend request
	public void accFriendRequest(String uid, Context context) throws ClientProtocolException, IOException, JSONException{
		String url = "join";    
       	
    	// Add data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("uid", uid));
                
        //use the generic list fn to post JSON obj
		WebServiceUtility webServiceUtility = new WebServiceUtility();
		JSONObject jObject = webServiceUtility.postData(nameValuePairs, url);
              
	}
	
	//login
	public String login(Context context, UserDTO user) throws ClientProtocolException, IOException, JSONException{
		String url = "login";
		String result="";
      
    	// Add data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
        nameValuePairs.add(new BasicNameValuePair("userName", user.getUserName()));
        nameValuePairs.add(new BasicNameValuePair("password", user.getPassword()));
        
        //use the generic list fn to post JSON obj
		WebServiceUtility webServiceUtility = new WebServiceUtility();
		JSONObject jObject = webServiceUtility.postData(nameValuePairs, url);
		
        if(jObject.getString("status").equals("true")){  	
        	result= "loginSuccess";
        }else{
        	Log.d("status:","false");
        	result= "loginFailed";
        }
		return result;
	}
}
