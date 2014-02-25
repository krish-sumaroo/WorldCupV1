package com.competition.worldcupv1.webServicehelper;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.competition.worldcupv1.databasehelper.DatabaseHelper;
import android.content.Context;

public class WebServiceHelper {
	
	private DatabaseHelper dbHelper;
	private Context context;
	private static final String webServiceURL = "http://worldcup2014.krish.ca/";

	public WebServiceHelper(Context context) {
		this.context = context;
	}
	
	public void extractTeamData() throws ClientProtocolException, IOException, JSONException{
		String url = webServiceURL+"/getTeamList.php";
		HttpClient httpclient = new DefaultHttpClient();
	 
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
	
	public void extractPlayerData() throws ClientProtocolException, IOException, JSONException{
		String url = webServiceURL+"/getPlayerList.php";
		HttpClient httpclient = new DefaultHttpClient();
	 
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
        	
        	StringBuilder insertOrReplacePlayer = new StringBuilder();

			insertOrReplacePlayer.append(" INSERT OR REPLACE INTO player (_id, player_name, team_id, position) ");
			insertOrReplacePlayer.append(" VALUES ( ");
			insertOrReplacePlayer.append(playerId+ ", '");
			insertOrReplacePlayer.append(playerName + "', ");
			insertOrReplacePlayer.append(teamId + ", '");
			insertOrReplacePlayer.append(position + "'); ");

			System.out.println(insertOrReplacePlayer.toString());
			dbHelper.executeSql(insertOrReplacePlayer.toString());				
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
}
