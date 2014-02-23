package com.competition.worldcupv1.service;

import java.util.ArrayList;
import java.util.List;

import com.competition.worldcupv1.databasehelper.DatabaseHelper;
import com.competition.worldcupv1.dto.PlayerDTO;
import com.competition.worldcupv1.enums.Role;

import android.content.Context;
import android.database.Cursor;


public class PlayerService {
	public List<PlayerDTO> getTeamPlayers(Context context, Integer teamId){		
		List<PlayerDTO> playerList = new ArrayList<PlayerDTO>();		
		DatabaseHelper dbHelper =  new DatabaseHelper(context);
		dbHelper.open();
		Cursor cursor = null;
		cursor = dbHelper.fetchTeamPlayers(teamId);
		cursor.moveToFirst();

		while (cursor.isAfterLast() == false) {		
			PlayerDTO playerDTO = new PlayerDTO();	
			playerDTO.setPlayerId(cursor.getInt(cursor.getColumnIndex("_id")));
			playerDTO.setPlayerName(cursor.getString(cursor.getColumnIndex("player_name")));
			playerDTO.setRole(Role.COACH);
			playerDTO.setTeamId(cursor.getInt(cursor.getColumnIndex("team_id")));

			playerList.add(playerDTO);
			cursor.moveToNext();
		}		
		dbHelper.close();		
		System.out.println(">>>>>>>>> getTeamPlayers = " + playerList.size());
		return playerList;
	}
}
