package com.competition.worldcupv1.webServicehelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.competition.worldcupv1.dto.GameDTO;
import com.competition.worldcupv1.dto.PlayerDTO;
import com.competition.worldcupv1.dto.TeamDTO;
import com.competition.worldcupv1.dto.UserDTO;
import com.competition.worldcupv1.utils.WebServiceUtility;

public class WebServiceHelper {	
	
	public WebServiceHelper() {
		// TODO Auto-generated constructor stub
	}
	
	//create new user
	public String createUser(Context context, UserDTO user) throws ClientProtocolException, IOException, JSONException{
		String url = "users/join/";
		String result="";
      
		//String pwd = user.getPassword();
    	// Add data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
        nameValuePairs.add(new BasicNameValuePair("username", user.getUserName()));
        nameValuePairs.add(new BasicNameValuePair("password", user.getPassword()));
        nameValuePairs.add(new BasicNameValuePair("nickname", user.getNickName()));
        nameValuePairs.add(new BasicNameValuePair("favTeam", String.valueOf(user.getFavTeam())));
        nameValuePairs.add(new BasicNameValuePair("uid", user.getUid()));
        nameValuePairs.add(new BasicNameValuePair("country", user.getCountry()));
        nameValuePairs.add(new BasicNameValuePair("regisId", user.getRegId()));    
        
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
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair("username", user.getUserName()));
        nameValuePairs.add(new BasicNameValuePair("password", user.getPassword()));
        nameValuePairs.add(new BasicNameValuePair("uid", user.getUid()));
        
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
	
	//get current game info
	public GameDTO insertGameInfo(Context context) throws ClientProtocolException, IOException, JSONException{
		String url = "games/getGame/";
		GameDTO gameInfo = new GameDTO();
      
		WebServiceUtility webServiceUtility = new WebServiceUtility();
		JSONObject jObject = webServiceUtility.extractList(url);
		String statusUpdated = jObject.getString("status");
		if(statusUpdated.equalsIgnoreCase("SUCCESS")){
			JSONObject dataObject = new JSONObject(jObject.getString("data"));
			gameInfo.setTime(dataObject.getString("time"));
			boolean playerStatus = dataObject.getBoolean("playersInfoStatus");
			//if status is final
			if(playerStatus){
				gameInfo.setPlayerInfoStatus(3);
			}
			else{
				gameInfo.setPlayerInfoStatus(2);
			}
			JSONObject team1Object = new JSONObject(dataObject.getString("team1"));
			gameInfo.setTeam1Id(Integer.parseInt(team1Object.getString("id")));
			gameInfo.setTeam1Flag(team1Object.getString("flag"));
        
			JSONObject team2Object = new JSONObject(dataObject.getString("team2"));
			gameInfo.setTeam2Id(Integer.parseInt(team2Object.getString("id")));
			gameInfo.setTeam2Flag(team2Object.getString("flag"));
			
			gameInfo.setVenue("Anfied");
		}
	        
		//dbHelper.addGameInfo(gameInfo);	  
		return gameInfo;
	}
	
	// Get players
    public List<PlayerDTO> getAllPlayers(int team1Id, int team2Id) throws ClientProtocolException, IOException, JSONException {
    	String url = "games/getActivePlayers/";
        List<PlayerDTO> playerList = new LinkedList<PlayerDTO>();
        WebServiceUtility webServiceUtility = new WebServiceUtility();
		JSONObject jObject = webServiceUtility.extractList(url);
		String statusUpdated = jObject.getString("status");
		if(statusUpdated.equalsIgnoreCase("SUCCESS")){
			JSONObject dataObject = new JSONObject(jObject.getString("data"));
			
			JSONObject team1Object = new JSONObject(dataObject.getString("team1"));
			JSONArray players1Array = team1Object.getJSONArray("players");
			for (int i = 0; i < players1Array.length(); i++) {
				PlayerDTO player = new PlayerDTO();
	        	JSONObject jsonObj = players1Array.getJSONObject(i);
	        	player.setPlayerId(Integer.parseInt(jsonObj.getString("playerId")));
	        	player.setName(jsonObj.getString("name"));
	        	player.setNumber(Integer.parseInt(jsonObj.getString("number")));
	        	player.setPosition(jsonObj.getString("position"));
	        	player.setTeamId(team1Id);	        	
	        	playerList.add(player);
	        	//dbHelper.addPlayer(player);
	        }
			
			JSONObject team2Object = new JSONObject(dataObject.getString("team2"));
			JSONArray players2Array = team2Object.getJSONArray("players");
			for (int i = 0; i < players2Array.length(); i++) {
				PlayerDTO player = new PlayerDTO();
	        	JSONObject jsonObj = players2Array.getJSONObject(i);
	        	player.setPlayerId(Integer.parseInt(jsonObj.getString("playerId")));
	        	player.setName(jsonObj.getString("name"));
	        	player.setNumber(Integer.parseInt(jsonObj.getString("number")));
	        	player.setPosition(jsonObj.getString("position"));
	        	player.setTeamId(team2Id);
	        	playerList.add(player);
	        	//dbHelper.addPlayer(player);
	        }	        
		}
		return playerList;         
    }
    
	// Get team list
    public List<TeamDTO> getAllTeam() throws ClientProtocolException, IOException, JSONException {
    	String url = "games/getTeams/";
        List<TeamDTO> teamList = new LinkedList<TeamDTO>();
        WebServiceUtility webServiceUtility = new WebServiceUtility();
		JSONObject jObject = webServiceUtility.extractList(url);
		String statusUpdated = jObject.getString("status");
		if(statusUpdated.equalsIgnoreCase("SUCCESS")){
	        JSONArray dataArray = jObject.getJSONArray("data");	        
	        for (int i = 0; i < dataArray.length(); i++) {
	        	TeamDTO team = new TeamDTO();
	        	JSONObject teamObj = dataArray.getJSONObject(i);
	        	team.setTeamId(Integer.parseInt(teamObj.getString("id")));
	        	team.setName(teamObj.getString("name"));
	        	team.setGroup(teamObj.getString("group"));
	        	team.setFlag(teamObj.getString("flag"));
	        	teamList.add(team);
	        }	        
		}
		return teamList;         
    }
    
	
	// LostPassword - send email
	public String lostPassword(String email, Context context) throws ClientProtocolException, IOException, JSONException{
		String url = "users/lossPwd/"; 
		String result="";
       	
    	// Add data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("email", email));
        //use the generic list fn to post JSON obj
		WebServiceUtility webServiceUtility = new WebServiceUtility();
		JSONObject jObject = webServiceUtility.postData(nameValuePairs, url);			
		System.out.println(">>>>>>>>>>>>>>> result login = " +jObject.getString("status") );
		
        if(jObject.getString("status").equals("true")){  	
        	result= "emailSend";
        }else{
        	Log.d("status:","false");
        	result= "emailNotSend";
        }
		return result;
              
	}
	
	// Send regisId for the device when login or register
	public void regisisterGCM(String uid, String regId, Context context) throws ClientProtocolException, IOException, JSONException{
		String url = "users/regisGCM"; 
       	
    	// Add data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("uid", uid));
        nameValuePairs.add(new BasicNameValuePair("regId", regId));
        //use the generic list fn to post JSON obj
		WebServiceUtility webServiceUtility = new WebServiceUtility();
		webServiceUtility.postData(nameValuePairs, url);			
            
	}
}
