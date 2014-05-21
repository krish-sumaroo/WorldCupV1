package com.competition.worldcupv1.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.competition.worldcupv1.databasehelper.DatabaseHelper;
import com.competition.worldcupv1.dto.PlayerDTO;

public class PlayerService {
	
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
	
	public void deleteTablePlayers(Context context){
		DatabaseHelper dbHelper =  new DatabaseHelper(context);
	    dbHelper.open();
	    dbHelper.deleteTablePlayers();
	    dbHelper.close();	
	}
}
