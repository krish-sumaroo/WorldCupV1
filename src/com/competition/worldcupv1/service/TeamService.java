package com.competition.worldcupv1.service;

import java.util.ArrayList;
import java.util.List;

import com.competition.worldcupv1.databasehelper.DatabaseHelper;
import com.competition.worldcupv1.dto.TeamDTO;

import android.content.Context;
import android.database.Cursor;

public class TeamService {	
	public void insertTeamsData(Context context){
	    DatabaseHelper db = new DatabaseHelper(context);
	    Boolean isTableFill = db.isTableMatchFill();	    
	    if(!isTableFill){
		    //createTeam(long _id, String team_name, String country_code, String coach)
		    TeamDTO team1 = new TeamDTO("Spain", "ES", "X");
		    team1.setTeamId(1);
		    TeamDTO team2 = new TeamDTO("Uruguay", "UR", "X");
		    team1.setTeamId(2);
		    TeamDTO team3 = new TeamDTO("England", "EN", "X");
		    team1.setTeamId(3);
		    TeamDTO team4 = new TeamDTO("Canada", "CA", "X");
		    team1.setTeamId(4);
		    
		    db.addTeam(team1);
		    db.addTeam(team2);
		    db.addTeam(team3);
		    db.addTeam(team4);
	    }	   
	}
	
	public ArrayList<TeamDTO> getTeamName(Context context){	
		ArrayList<TeamDTO> teamList = new ArrayList<TeamDTO>();		
		DatabaseHelper dbHelper =  new DatabaseHelper(context);
		dbHelper.open();
		Cursor cursor = null;
		cursor = dbHelper.fetchTeamName();
		cursor.moveToFirst();

		while (cursor.isAfterLast() == false) {	
			TeamDTO team = new TeamDTO();
			int teamId = (cursor.getInt(cursor.getColumnIndex("_id")));
			String teamName = (cursor.getString(cursor.getColumnIndex("team_name")));
			team.setTeamId(teamId);
			team.setTeamName(teamName);
			teamList.add(team);
			cursor.moveToNext();
		}		
		dbHelper.close();		
		System.out.println(">>>>>>>>> getTeamName = " + teamList.size());
		return teamList;
	}	
}
