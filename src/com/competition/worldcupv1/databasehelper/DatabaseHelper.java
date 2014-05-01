package com.competition.worldcupv1.databasehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.competition.worldcupv1.dto.TeamDTO;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "worldCupV1";  
	private static final int DATABASE_VERSION = 1;
	
	// Table name
	private static final String DB_TABLE_TEAM= "team";
	private static final String DB_TABLE_PLAYER= "player";
	private static final String DB_TABLE_MATCH= "match";
	
	// Table team column
	public final String TEAM_TEAMID = "_id";
	public final String TEAM_TEAMNAME = "team_name";
	public final String TEAM_COUNTRYCODE = "country_code";
	public final String TEAM_COACH = "coach";
	
	// Table player column
	public final String PLAYER_PLAYERID = "_id";
	public final String PLAYER_PLAYERNAME = "player_name";
	public final String PLAYER_TEAMID = "team_id";
	public final String PLAYER_POSITION = "position";
	public final String PLAYER_NUMBER = "number";
		
	// Table match column
	public final String MATCH_MATCHID = "_id";
	public final String MATCH_HOMETEAMID = "home_team_id";
	public final String MATCH_AWAYTEAMID = "away_team_id";
	public final String MATCH_STATUS = "status";
	public final String MATCH_KICKOFF = "kick_off";
	public final String MATCH_VENUE = "venue";
	
	private SQLiteDatabase db ;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);	
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTables(db);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub		
	}
	
	public DatabaseHelper open() throws SQLException {
		db = this.getWritableDatabase();
		return this;
	}
	
	private void createTables(SQLiteDatabase db){
		createTableTeam(db);
		createTablePlayer(db);
		createTableMatch(db);
	}
	
	// ******************************** create tables *******************************
	
	// Create table team
	private void createTableTeam(SQLiteDatabase db){
		StringBuilder qb = new StringBuilder();
		qb.append(" CREATE TABLE IF NOT EXISTS team ( ");
		qb.append("  	_id integer primary key autoincrement, ");
		qb.append("  	team_name text not null,");
		qb.append("  	country_code text not null, ");
		qb.append("  	coach text); ");
		db.execSQL(qb.toString());
	}
		
	// Create table player
	private void createTablePlayer(SQLiteDatabase db){
		StringBuilder qb = new StringBuilder();
		qb.append(" CREATE TABLE IF NOT EXISTS player ( ");
		qb.append(" 	_id integer primary key autoincrement, ");
		qb.append(" 	player_name text not null, ");
		qb.append(" 	team_id integer, ");
		qb.append(" 	position text not null, ");
		qb.append(" 	number integer, ");
		qb.append(" 	FOREIGN KEY(team_id) REFERENCES team(_id) " );
		qb.append(" );");
		db.execSQL(qb.toString());
	}
			
	// Create table match
	private void createTableMatch(SQLiteDatabase db){
		StringBuilder qb = new StringBuilder();
		qb.append(" CREATE TABLE IF NOT EXISTS match ( ");
		qb.append(" 	_id integer primary key autoincrement, ");
		qb.append(" 	home_team_id integer, ");
		qb.append(" 	away_team_id integer, ");
		qb.append(" 	status text not null, ");
		qb.append(" 	kick_off text not null, ");
		qb.append(" 	venue text not null, ");
		qb.append(" 	FOREIGN KEY(home_team_id) REFERENCES team(_id), " );
		qb.append(" 	FOREIGN KEY(away_team_id) REFERENCES team(_id) " );
		qb.append(" );");
		db.execSQL(qb.toString());
	}

	/*
	public Cursor fetchTeamPlayers(Integer teamId) throws SQLException{
		StringBuffer sql = new StringBuffer();
		final String constantStatement = "SELECT * "+ "FROM player p";
		sql.append(constantStatement).append(" WHERE p.team_id= ").append(teamId);		
		Cursor mCursor = db.rawQuery(sql.toString(), null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;		
	}
		*/
	public Cursor fetchTeamName() throws SQLException{
		StringBuffer sql = new StringBuffer();
		final String constantStatement = "SELECT * "+ "FROM team t";
		sql.append(constantStatement);		
		Cursor mCursor = db.rawQuery(sql.toString(), null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;		
	}

	public Cursor executeQuery(String sql, String[] selectionArgs){
		Cursor mCursor = db.rawQuery(sql.toString(), selectionArgs);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public void executeSql(String sql) {
		db.execSQL(sql);		
	}
	
	public boolean isTableMatchFill(){
        String query = "SELECT  * FROM " + DB_TABLE_TEAM; 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);        
        int count = cursor.getCount();
        db.close();        
        if(count>0){
        	return true;
        }
		return false;
	}
	
	//insert Team Values
	public void addTeam(TeamDTO team){
        // get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase(); 
        // create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();        
        values.put(TEAM_TEAMID, team.getTeamId());
		values.put(TEAM_TEAMNAME, team.getTeamName());
		values.put(TEAM_COUNTRYCODE, team.getCountryCode());
		values.put(TEAM_COACH, team.getCoach());
        // insert to db
        db.insert(DB_TABLE_TEAM, // table
             null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values 
        // close db
        db.close();
    }	
}
