package com.competition.worldcupv1.service;

import com.competition.worldcupv1.databasehelper.DatabaseHelper;

import android.content.Context;


public class TeamService {
	
	public void insertTeamsData(Context context){
	    DatabaseHelper db = new DatabaseHelper(context);

	    Boolean isTableFill = db.isTableMatchFill();
	}
}
