package com.competition.worldcupv1;

import java.util.HashMap;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.competition.worldcupv1.asynctasks.CreateGameInfoTask;
import com.competition.worldcupv1.asynctasks.CreateGameInfoTask.CreateGameInfoTaskListener;
import com.competition.worldcupv1.asynctasks.CreatePlayerListTask;
import com.competition.worldcupv1.asynctasks.CreatePlayerListTask.CreatePlayerListTaskListener;
import com.competition.worldcupv1.dto.GameDTO;
import com.competition.worldcupv1.dto.GameInfoDTO;
import com.competition.worldcupv1.dto.PlayerDTO;
import com.competition.worldcupv1.fragment.TeamPlayersOneFrag;
import com.competition.worldcupv1.fragment.TeamPlayersTwoFrag;
import com.competition.worldcupv1.service.GameService;
import com.competition.worldcupv1.service.PlayerService;
import com.competition.worldcupv1.utils.ConnectionUtility;
import com.competition.worldcupv1.utils.ConnectionUtility.ConnectionUtilityListener;
import com.competition.worldcupv1.utils.SessionManager;
import com.facebook.Session;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;

@SuppressWarnings("deprecation")
public class GameListActivity extends FragmentActivity   {
	private SlidingPaneLayout mPane;
	GameService gameService = new GameService();
	PlayerService playerService = new PlayerService();
	private static final int PLAYER_STATUS_INFO_FINAL = 3;
	private static final int PLAYER_STATUS_INFO_NOT_FINAL = 2;
	TextView txtTeam1Name;
	TextView txtTeam2Name;
	TextView txtVenue;
	TextView txtTime;
	
	//----------------- TWITTER ------------------------
	static String TWITTER_CONSUMER_KEY = "E0iHRkluCMiKQcFGuhMRvErqI";
	static String TWITTER_CONSUMER_SECRET = "NjgLibGPE0lyJL9XiNly0OI6wb1UtTmBLXVmwZ0wqOYFwe1bBE";

	// Preference Constants
	static String PREFERENCE_NAME = "twitter_oauth";
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
   	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
   	static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";

   	// Twitter oauth urls
   	static final String URL_TWITTER_AUTH = "auth_url";
   	static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
   	static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
   	
   	//----------------- FACEBOOK ---------------------
    private static String APP_ID = "707333179297046";
    //Instance of Facebook Class
    private Facebook facebook;
    private AsyncFacebookRunner mAsyncRunner;
    String FILENAME = "AndroidSSO_data";
    private SharedPreferences mPrefs;

   	ProgressDialog pDialog;
   	private static SharedPreferences mSharedPreferences;
   	SessionManager session;
	final ConnectionUtility connectionUtility = new ConnectionUtility();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		
		
		//--------------- facebook -------------
		facebook = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(facebook);
		mPrefs = getPreferences(MODE_PRIVATE);
		        
        session = new SessionManager(getApplicationContext());		
	   
	    
        HashMap<String, String> user = session.getUserDetails();   
        
		// Shared Preferences
		mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
				
		//check if gameInfo table empty			
		boolean isTableGameFill = gameService.isTableGameFill(getApplicationContext());
		if(isTableGameFill){
			uponStatusDisplayPage2();
		}else{		
			
			try {
				if(connectionUtility.hasWifi(getBaseContext())){
					//if empty we populate the table
					populateTableGameInfo();
				}
				else{
					connectionUtility.showToast(GameListActivity.this);
					connectionUtility.setUtilityListener(new ConnectionUtilityListener()  {				
						@Override
						public void onInternetEnabled(boolean result) {
							//if empty we populate the table
							populateTableGameInfo();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}
	
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Take appropriate action for each action item click
	        switch (item.getItemId()) {
	        case R.id.action_search:
	        	logoutAppl();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	        }
	    }

	public void  populateTablePlayers (int team1Id, int team2Id){
		CreatePlayerListTask populatePlayersTask = new CreatePlayerListTask();
		populatePlayersTask.setContext(getApplicationContext());
		populatePlayersTask.setTeam1Id(team1Id);
		populatePlayersTask.setTeam2Id(team2Id);
		populatePlayersTask.execute();        
		populatePlayersTask.setCreatePlayerListTaskListener(new CreatePlayerListTaskListener() {
			@Override
			public void onComplete(List<PlayerDTO> result) {
				playerService.insertPlayerData(getApplicationContext(),result);		
				
				//display page players
				 setContentView(R.layout.main_teamplayers_fragment);
				 mPane = (SlidingPaneLayout) findViewById(R.id.pane);		 
				 mPane.openPane();
				  
				 getSupportFragmentManager().beginTransaction()
				 .add(R.id.pane1, new TeamPlayersOneFrag(), "pane1").commit();
				 getSupportFragmentManager().beginTransaction()
				 .add(R.id.pane2, new TeamPlayersTwoFrag(), "pane2").commit();
			}});	
		}

	public void  populateTableGameInfo (){
		CreateGameInfoTask populateGameUnfoTask = new CreateGameInfoTask();
	    populateGameUnfoTask.setContext(getApplicationContext());
	    populateGameUnfoTask.execute();        
	    populateGameUnfoTask.setCreateGameInfoTaskListener(new CreateGameInfoTaskListener() {	
			@Override
			public void onComplete(GameDTO result) {
				gameService.insertGameData(getApplicationContext(),result);
				//gameService.(getApplicationContext());
				//go check the player status info
				uponStatusDisplayPage ();
				
			}});
	}
	
	public int  verifyGamePlayerStatus (){
		return playerService.getPlayerInfoStatus(getApplicationContext());
	}
	
	public void  uponStatusDisplayPage (){
		int checkPlayerInfoStatus =  verifyGamePlayerStatus();
		if(checkPlayerInfoStatus == PLAYER_STATUS_INFO_NOT_FINAL){
			// display only game info page
			GameInfoDTO gameInfo = gameService.getGameInfoDTO(getApplicationContext());
			setContentView(R.layout.game_info);
			txtTeam1Name = (TextView) findViewById( R.id.txtTeam1Name);
			txtTeam2Name = (TextView) findViewById( R.id.txtTeam2Name);
			txtVenue = (TextView) findViewById( R.id.txtVenue);
			txtTime = (TextView) findViewById( R.id.txtTime);
			txtTeam1Name.setText(gameInfo.getTeam1Name());
			txtTeam2Name.setText(gameInfo.getTeam2Name());
			txtVenue.setText(gameInfo.getVenue());
			txtTime.setText(gameInfo.getTime());
		}
		else if (checkPlayerInfoStatus == PLAYER_STATUS_INFO_FINAL){			
			final GameDTO currentGame = gameService.getGame(getApplicationContext());			
			boolean isTablePlayerFill = gameService.isTablePlayerFill(getApplicationContext());
			if(!isTablePlayerFill){
				try {
					if(connectionUtility.hasWifi(getBaseContext())){
						//populate table game players 
						populateTablePlayers (currentGame.getTeam1Id(), currentGame.getTeam2Id());	
					}
					else{
						connectionUtility.showToast(GameListActivity.this);
						connectionUtility.setUtilityListener(new ConnectionUtilityListener()  {				
							@Override
							public void onInternetEnabled(boolean result) {
								//populate table game players 
								populateTablePlayers (currentGame.getTeam1Id(), currentGame.getTeam2Id());	
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
	
	
	public void  uponStatusDisplayPage2 (){
		int checkPlayerInfoStatus =  verifyGamePlayerStatus();
		if(checkPlayerInfoStatus == PLAYER_STATUS_INFO_NOT_FINAL){
			// display only game info page
			GameInfoDTO gameInfo = gameService.getGameInfoDTO(getApplicationContext());
			setContentView(R.layout.game_info);
			txtTeam1Name = (TextView) findViewById( R.id.txtTeam1Name);
			txtTeam2Name = (TextView) findViewById( R.id.txtTeam2Name);
			txtVenue = (TextView) findViewById( R.id.txtVenue);
			txtTime = (TextView) findViewById( R.id.txtTime);
			txtTeam1Name.setText(gameInfo.getTeam1Name());
			txtTeam2Name.setText(gameInfo.getTeam2Name());
			txtVenue.setText(gameInfo.getVenue());
			txtTime.setText(gameInfo.getTime());
		}
		else if (checkPlayerInfoStatus == PLAYER_STATUS_INFO_FINAL){			
			final GameDTO currentGame = gameService.getGame(getApplicationContext());			
			boolean isTablePlayerFill = gameService.isTablePlayerFill(getApplicationContext());
			if(!isTablePlayerFill){
				try {
					if(connectionUtility.hasWifi(getBaseContext())){
						//populate table game players 
						populateTablePlayers (currentGame.getTeam1Id(), currentGame.getTeam2Id());	
					}
					else{
						connectionUtility.showToast(GameListActivity.this);
						connectionUtility.setUtilityListener(new ConnectionUtilityListener()  {				
							@Override
							public void onInternetEnabled(boolean result) {
								//populate table game players 
								populateTablePlayers (currentGame.getTeam1Id(), currentGame.getTeam2Id());	
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
				
				//display page players
				 setContentView(R.layout.main_teamplayers_fragment);
				 mPane = (SlidingPaneLayout) findViewById(R.id.pane);		 
				 mPane.openPane();
				  
				 getSupportFragmentManager().beginTransaction()
				 .add(R.id.pane1, new TeamPlayersOneFrag(), "pane1").commit();
				 getSupportFragmentManager().beginTransaction()
				 .add(R.id.pane2, new TeamPlayersTwoFrag(), "pane2").commit();
			}	
			else{
				
				//display page players
				 setContentView(R.layout.main_teamplayers_fragment);
				 mPane = (SlidingPaneLayout) findViewById(R.id.pane);		 
				 mPane.openPane();
				  
				 getSupportFragmentManager().beginTransaction()
				 .add(R.id.pane1, new TeamPlayersOneFrag(), "pane1").commit();
				 getSupportFragmentManager().beginTransaction()
				 .add(R.id.pane2, new TeamPlayersTwoFrag(), "pane2").commit();
			}

		}
	}
	
    private void logoutFromTwitter() {
        // Clear the shared preferences
        Editor e = mSharedPreferences.edit();
        e.remove(PREF_KEY_OAUTH_TOKEN);
        e.remove(PREF_KEY_OAUTH_SECRET);
        e.remove(PREF_KEY_TWITTER_LOGIN);
        e.commit();
    }
    
	public static void callFacebookLogout(Context context, SharedPreferences mPrefs) {
	    Session session = Session.getActiveSession();
	    if (session != null) {
	        if (!session.isClosed()) {
	            session.closeAndClearTokenInformation();
	            //clear your preferences if saved
	            Editor e = mPrefs.edit();
	            e.remove("access_token");
	            e.remove("access_expires");
	            e.commit();	           
	        }
	    } else {
	        session = new Session(context);
	        //Session.setActiveSession(session);
	        session.closeAndClearTokenInformation();
	        //clear your preferences if saved
            Editor e = mPrefs.edit();
            e.remove("access_token");
            e.remove("access_expires");
            e.commit();
	    }
	}

	public void logoutAppl(){
        logoutFromTwitter();
        callFacebookLogout(getApplicationContext(),mPrefs);
        session.logoutUser();	           
        Intent mainAct = new Intent(getApplicationContext(), MainActivity.class);
        // Close all views before launching matchList
        mainAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainAct);
        finish();
	}
}
