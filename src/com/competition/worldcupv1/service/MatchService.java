package com.competition.worldcupv1.service;

import java.util.ArrayList;
import java.util.List;

import com.competition.worldcupv1.databasehelper.DatabaseHelper;
import com.competition.worldcupv1.dto.MatchDTO;

import android.content.Context;

public class MatchService {

	 public List<MatchDTO> getAllMatch(Context context) {		 
		 DatabaseHelper db = new DatabaseHelper(context);
		 List<MatchDTO> matchList = new ArrayList<MatchDTO>();
		 matchList = db.getAllMatch();
		 db.close();
		 return matchList;		
	 }
}
