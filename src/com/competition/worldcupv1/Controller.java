package com.competition.worldcupv1;

import java.util.List;

import android.app.Application;
import android.content.Context;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.widget.Toast;

import com.competition.worldcupv1.asynctasks.CreateGameInfoTask;
import com.competition.worldcupv1.asynctasks.CreatePlayerListTask;
import com.competition.worldcupv1.asynctasks.CreateGameInfoTask.CreateGameInfoTaskListener;
import com.competition.worldcupv1.asynctasks.CreatePlayerListTask.CreatePlayerListTaskListener;
import com.competition.worldcupv1.asynctasks.GetRegistrationIdTask;
import com.competition.worldcupv1.asynctasks.GetRegistrationIdTask.GetRegistrationIdTaskListener;
import com.competition.worldcupv1.dto.GameDTO;
import com.competition.worldcupv1.dto.PlayerDTO;
import com.competition.worldcupv1.service.GameService;
import com.competition.worldcupv1.service.PlayerService;

public class Controller extends Application{
	
	PlayerService playerService = new PlayerService();
	GameService gameService = new GameService();
	
	public void registerGCM (String uid, String regId, Context context){
		GetRegistrationIdTask getRegistrationIdTask = new GetRegistrationIdTask();
		getRegistrationIdTask.setUid(uid);
		getRegistrationIdTask.setRegisId(regId);
		getRegistrationIdTask.setContext(getApplicationContext());
		getRegistrationIdTask.execute();        
		getRegistrationIdTask.setGetRegistrationIdTaskListener(new GetRegistrationIdTaskListener() {			
			@Override
			public void onComplete(String result) {
				
			}
		});
	}
	
	public String getUID(){
		String uid ="";
		final TelephonyManager mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    	//get deviceID
        if (mTelephony.getDeviceId() != null){
        	uid = mTelephony.getDeviceId(); //use for mobiles
         }
        else{
        	uid = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID); //use for tablets
         }
		return uid;  
	}
	
	public void pushActionFinalPlayerList(){
		GameService gameService = new GameService();
		GameDTO currentGame = gameService.getGame(getApplicationContext());
		//update status set to 3 - final
		gameService.updateGameFinal(getApplicationContext());
		if(currentGame!=null){
			//delete table player if has any data
			playerService.deleteTablePlayers(getApplicationContext());
			//populate with final list Players
			populateTablePlayers(currentGame.getTeam1Id(),currentGame.getTeam2Id());
		}
	}
	
	public void pushActionGameOver(){
		//display message game over
		Toast toast = Toast.makeText(getApplicationContext(),"Game Over", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		//delete table game data
		gameService.deleteTableGame(getApplicationContext());
		//delete table player data
		playerService.deleteTablePlayers(getApplicationContext());		
		//get next game data
		populateTableGameInfo();
	}
	
	public void pushActionMessage(){
		Toast toast = Toast.makeText(getApplicationContext(),"Message", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
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
		}});
	}
}
