package com.competition.worldcupv1.databasehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.competition.worldcupv1.dto.GameDTO;
import com.competition.worldcupv1.dto.PlayerDTO;
import com.competition.worldcupv1.dto.TeamDTO;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "worldCupV1";  
	private static final int DATABASE_VERSION = 1;
	
	// Table name
	private static final String DB_TABLE_TEAM= "team";
	private static final String DB_TABLE_PLAYER= "player";
	private static final String DB_TABLE_GAME= "game";
	
	// Table team column
	public final String TEAM_TEAMID = "team_id";
	public final String TEAM_TEAMNAME = "name";
	public final String TEAM_FLAG = "flag";
	public final String TEAM_GROUP = "groupTeam";
	
	// Table player column
	public final String PLAYER_PLAYERID = "player_id";
	public final String PLAYER_PLAYERNAME = "player_name";
	public final String PLAYER_TEAMID = "team_id";
	public final String PLAYER_POSITION = "player_position";
	public final String PLAYER_NUMBER = "player_number";
	
	
	// Table match column
	public final String MATCH_MATCHID = "_id";
	public final String MATCH_HOMETEAMID = "home_team_id";
	public final String MATCH_AWAYTEAMID = "away_team_id";
	public final String MATCH_STATUS = "status";
	public final String MATCH_KICKOFF = "kick_off";
	public final String MATCH_VENUE = "venue";
	
	// Table current game column
	public final String GAME_TIME = "time";
	public final String GAME_VENUE = "venue";
	public final String GAME_TEAM_1_ID = "team_1_id";
	public final String GAME_TEAM_1_FLAG = "team_1_flag";
	public final String GAME_TEAM_2_ID = "team_2_id";
	public final String GAME_TEAM_2_FLAG = "team_2_flag";
	public final String GAME_PLAYER_STATUS = "player_info_status";
	
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
		createTableGameInfo(db);
	}
	
	// ******************************** create tables *******************************
	
	// Create table team
	private void createTableTeam(SQLiteDatabase db){
		StringBuilder qb = new StringBuilder();
		qb.append(" CREATE TABLE IF NOT EXISTS team ( ");
		qb.append("  	_id integer primary key autoincrement, ");
		qb.append(" 	team_id integer, ");
		qb.append("  	name text not null,");
		qb.append("  	flag text not null,");
		qb.append("  	groupTeam text ); ");
		db.execSQL(qb.toString());
	}
		
	// Create table player
	private void createTablePlayer(SQLiteDatabase db){
		StringBuilder qb = new StringBuilder();
		qb.append(" CREATE TABLE IF NOT EXISTS player ( ");
		qb.append(" 	_id integer primary key autoincrement, ");
		qb.append(" 	player_id integer, ");
		qb.append(" 	player_name text not null, ");
		qb.append(" 	player_position text, ");
		qb.append(" 	player_number integer, ");
		qb.append(" 	team_id integer ");
		qb.append(" );");
		db.execSQL(qb.toString());
	}
			
	// Create table match
	private void createTableGameInfo(SQLiteDatabase db){
		StringBuilder qb = new StringBuilder();
		qb.append(" CREATE TABLE IF NOT EXISTS game ( ");
		qb.append(" 	id integer primary key autoincrement, ");
		qb.append(" 	time text not null, ");
		qb.append(" 	venue text not null, ");
		qb.append(" 	team_1_id integer, ");
		qb.append(" 	team_1_flag text not null, ");
		qb.append(" 	team_2_id integer, ");
		qb.append(" 	team_2_flag text not null, ");
		qb.append(" 	player_info_status integer ); ");
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
	
	public boolean isTableTeamFill(){
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
	
	public boolean isTableGameFill(){
        String query = "SELECT  * FROM " + DB_TABLE_GAME; 
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
		values.put(TEAM_TEAMNAME, team.getName());
		values.put(TEAM_FLAG, team.getFlag());
		values.put(TEAM_GROUP, team.getGroup());
        // insert to db
        db.insert(DB_TABLE_TEAM, // table
             null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values 
        // close db
        db.close();
    }
		
	//insert Game Values
	public void addGameInfo(GameDTO game){
        // get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase(); 
        // create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
		values.put(GAME_TIME, game.getTime());
		values.put(GAME_VENUE, game.getVenue());
		values.put(GAME_TEAM_1_ID, game.getTeam1Id());
		values.put(GAME_TEAM_1_FLAG, game.getTeam1Flag());
		values.put(GAME_TEAM_2_ID, game.getTeam2Id());
		values.put(GAME_TEAM_2_FLAG, game.getTeam2Flag());
		values.put(GAME_PLAYER_STATUS, game.getPlayerInfoStatus());
        // insert to db
        db.insert(DB_TABLE_GAME, // table
             null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values 
        // close db
        db.close();
    }
	
	public Cursor fetchGameDTO() throws SQLException{
		StringBuffer sql = new StringBuffer();
		final String constantStatement = "SELECT * "+ "FROM game m";
		sql.append(constantStatement);		
		Cursor mCursor = db.rawQuery(sql.toString(), null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;		
	}
	
	public Cursor fetchGameInfo() throws SQLException{
		StringBuffer sql = new StringBuffer();
		final String constantStatement = "SELECT m.time as time, m.venue as venue, m.player_info_status as playerInfoStatus, m.team_1_id as team1Id, t1.name as team1Name, t1.flag as team1Flag, t1.groupTeam as team1Group, "
										+ "m.team_2_id as team2Id, t2.name as team2Name, t2.flag as team2Flag, t2.groupTeam as team2Group "
										+ " FROM game m, team t1, team t2 where m.team_1_id = t1.team_id and m.team_2_id = t2.team_id";
		sql.append(constantStatement);		
		Cursor mCursor = db.rawQuery(sql.toString(), null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;		
	}
	
	public boolean isTablePlayerFill(){
        String query = "SELECT  * FROM " + DB_TABLE_PLAYER; 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);        
        int count = cursor.getCount();
        db.close();        
        if(count>0){
        	return true;
        }
		return false;
	}
	
	//insert player Values
	public void addPlayer(PlayerDTO player){
        // get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase(); 
        // create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(PLAYER_PLAYERID, player.getPlayerId());
		values.put(PLAYER_PLAYERNAME, player.getName());
		values.put(PLAYER_TEAMID, player.getTeamId());
		values.put(PLAYER_POSITION, player.getPosition());
		values.put(PLAYER_NUMBER, player.getNumber());
        // insert to db
        db.insert(DB_TABLE_PLAYER, // table
             null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values 
        // close db
        db.close();
    }
	
	public Cursor fetchPlayers(int teamId) throws SQLException{
		StringBuffer sql = new StringBuffer();
		final String constantStatement = "SELECT * "+ "FROM player p " + "WHERE p.team_id = " +teamId;
		sql.append(constantStatement);		
		Cursor mCursor = db.rawQuery(sql.toString(), null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;		
	}
	
	//delete table team
	public void deleteTableGame() {
		String deleteSQL = "DELETE FROM " + DB_TABLE_GAME;
		db.execSQL(deleteSQL);	
	}	
	//delete table players
	public void deleteTablePlayers() {
		String deleteSQL = "DELETE FROM " + DB_TABLE_PLAYER;
		db.execSQL(deleteSQL);	
	}
	
	//update table game set status 3 - final
	public void updateTableGame() {
		String updateSQL = "UPDATE  " + DB_TABLE_GAME +" SET player_info_status=3";
		db.execSQL(updateSQL);	
	}	
}
