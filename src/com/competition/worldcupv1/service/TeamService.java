package com.competition.worldcupv1.service;

import java.util.ArrayList;
import java.util.List;

import com.competition.worldcupv1.databasehelper.DatabaseHelper;
import com.competition.worldcupv1.dto.PlayerDTO;
import com.competition.worldcupv1.dto.TeamDTO;

import android.content.Context;
import android.database.Cursor;

public class TeamService {	

	public ArrayList<TeamDTO> getTeamName(Context context){	
		ArrayList<TeamDTO> teamList = new ArrayList<TeamDTO>();		
		DatabaseHelper dbHelper =  new DatabaseHelper(context);
		dbHelper.open();
		Cursor cursor = null;
		cursor = dbHelper.fetchTeamName();
		cursor.moveToFirst();

		while (cursor.isAfterLast() == false) {	
			TeamDTO team = new TeamDTO();
			int teamId = (cursor.getInt(cursor.getColumnIndex("team_id")));
			String teamName = (cursor.getString(cursor.getColumnIndex("name")));
			team.setTeamId(teamId);
			team.setName(teamName);
			teamList.add(team);
			cursor.moveToNext();
		}		
		dbHelper.close();		
		System.out.println(">>>>>>>>> getTeamName = " + teamList.size());
		return teamList;
	}
	
	public void insertTeamsData(Context context, List<TeamDTO>  teamList){
		DatabaseHelper dbHelper =  new DatabaseHelper(context);
	    dbHelper.open();
	    
	    if(teamList.size()>0){
			for ( TeamDTO team: teamList) {
				dbHelper.addTeam(team);
			}
	    }
	    dbHelper.close();	
	}
}
