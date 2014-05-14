package com.competition.worldcupv1.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.competition.worldcupv1.databasehelper.DatabaseHelper;
import com.competition.worldcupv1.dto.GameDTO;
import com.competition.worldcupv1.dto.GameInfoDTO;
import com.competition.worldcupv1.dto.PlayerDTO;
import com.competition.worldcupv1.dto.TeamDTO;

public class GameService {
	public boolean isTableGameFill(Context context){
	    DatabaseHelper db = new DatabaseHelper(context);
	    Boolean isTableFill = db.isTableGameFill();
		return isTableFill;	   
	}
	
	public Integer getPlayerInfoStatus(Context context){	
		int playerInfoStatus = 0 ;
		DatabaseHelper dbHelper =  new DatabaseHelper(context);
		dbHelper.open();
		Cursor cursor = null;
		cursor = dbHelper.fetchGameDTO();
		cursor.moveToFirst();

		while (cursor.isAfterLast() == false) {	
			playerInfoStatus = (cursor.getInt(cursor.getColumnIndex("player_info_status")));
			cursor.moveToNext();
		}		
		dbHelper.close();		
		return playerInfoStatus;
	}
	
	public GameInfoDTO getGameInfoDTO(Context context){	
		GameInfoDTO gameInfo = new GameInfoDTO();
		DatabaseHelper dbHelper =  new DatabaseHelper(context);
		dbHelper.open();
		Cursor cursor = null;
		cursor = dbHelper.fetchGameInfo();
		cursor.moveToFirst();

		while (cursor.isAfterLast() == false) {	
			gameInfo.setPlayerInfoStatus((cursor.getInt(cursor.getColumnIndex("playerInfoStatus"))));
			gameInfo.setTime((cursor.getString(cursor.getColumnIndex("time"))));
			gameInfo.setVenue((cursor.getString(cursor.getColumnIndex("venue"))));
			
			gameInfo.setTeam1Id((cursor.getInt(cursor.getColumnIndex("team1Id"))));
			gameInfo.setTeam1Name((cursor.getString(cursor.getColumnIndex("team1Name"))));
			gameInfo.setTeam1Flag((cursor.getString(cursor.getColumnIndex("team1Flag"))));
			gameInfo.setTeam1Group((cursor.getString(cursor.getColumnIndex("team1Group"))));
			
			gameInfo.setTeam2Id((cursor.getInt(cursor.getColumnIndex("team2Id"))));
			gameInfo.setTeam2Name((cursor.getString(cursor.getColumnIndex("team2Name"))));
			gameInfo.setTeam2Group((cursor.getString(cursor.getColumnIndex("team2Group"))));
			gameInfo.setTeam2Flag((cursor.getString(cursor.getColumnIndex("team2Flag"))));

			cursor.moveToNext();
		}		
		dbHelper.close();		
		return gameInfo;
	}
	
	public void insertGameData(Context context, GameDTO gameDTO){
		DatabaseHelper dbHelper =  new DatabaseHelper(context);
	    dbHelper.open();
	    dbHelper.addGameInfo(gameDTO);
	    dbHelper.close();	
	}
	
	public GameDTO getGame(Context context){	
		DatabaseHelper dbHelper =  new DatabaseHelper(context);
		dbHelper.open();
		Cursor cursor = null;
		cursor = dbHelper.fetchGameDTO();
		cursor.moveToFirst();
		GameDTO currentGame = new GameDTO();
		while (cursor.isAfterLast() == false) {			
			currentGame.setTeam1Id(cursor.getInt(cursor.getColumnIndex("team_1_id")));
			currentGame.setTeam2Id(cursor.getInt(cursor.getColumnIndex("team_2_id")));
			cursor.moveToNext();
		}		
		dbHelper.close();		
		return currentGame;
	}
	
	
	public void insertPlayerData(Context context, List<PlayerDTO>  listPlayers){
		DatabaseHelper dbHelper =  new DatabaseHelper(context);
	    dbHelper.open();
	    
	    if(listPlayers.size()>0){
			for ( PlayerDTO player: listPlayers) {
				dbHelper.addPlayer(player);
			}
	    }
	    dbHelper.close();	
	}
	public boolean isTablePlayerFill(Context context){
	    DatabaseHelper db = new DatabaseHelper(context);
	    Boolean isTableFill = db.isTablePlayerFill();
		return isTableFill;	   
	}
	
	public List<PlayerDTO> getPlayers(Context context,int teamId){	
		ArrayList<PlayerDTO> playersList = new ArrayList<PlayerDTO>();
		
		DatabaseHelper dbHelper =  new DatabaseHelper(context);
		dbHelper.open();
		Cursor cursor = null;
		cursor = dbHelper.fetchPlayers(teamId);
		cursor.moveToFirst();

		while (cursor.isAfterLast() == false) {	
			PlayerDTO player = new PlayerDTO();
			player.setPlayerId((cursor.getInt(cursor.getColumnIndex("player_id"))));
			player.setName((cursor.getString(cursor.getColumnIndex("player_name"))));
			player.setNumber((cursor.getInt(cursor.getColumnIndex("player_number"))));
			player.setPosition((cursor.getString(cursor.getColumnIndex("player_position"))));
			player.setTeamId((cursor.getInt(cursor.getColumnIndex("team_id"))));
			playersList.add(player);
			cursor.moveToNext();
		}		
		dbHelper.close();		
		return playersList;
	}
	
	public void deleteTableGame(Context context){
		DatabaseHelper dbHelper =  new DatabaseHelper(context);
	    dbHelper.open();
	    dbHelper.deleteTableGame();
	    dbHelper.close();	
	}
	
	public void deleteTablePlayers(Context context){
		DatabaseHelper dbHelper =  new DatabaseHelper(context);
	    dbHelper.open();
	    dbHelper.deleteTablePlayers();
	    dbHelper.close();	
	}
}
