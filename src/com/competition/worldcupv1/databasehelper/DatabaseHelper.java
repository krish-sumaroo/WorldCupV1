package com.competition.worldcupv1.databasehelper;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import com.competition.worldcupv1.dto.MatchDTO;
import com.competition.worldcupv1.dto.TeamDTO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
		
	private static final String insertResultOK = "Data inserted successfully";
	private static final String insertResultKO = "Could not insert data";
	private static final String updateResultOK = "Data updated successfully";
	private static final String updateResultKO = "Could not update data";
	private String result = "No update found";
	
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
	
	// ******************************** create content values ***********************
	
	private ContentValues createTeamContentValues(long _id, String team_name, String country_code, String coach) {
		ContentValues values = new ContentValues();
		values.put(PLAYER_PLAYERID, _id);
		values.put(TEAM_TEAMNAME, team_name);
		values.put(TEAM_COUNTRYCODE, country_code);
		values.put(TEAM_COACH, coach);
		return values;
	}
		
	private ContentValues createPlayerContentValues(long _id, String player_name, long team_id, String position, long number) {
		ContentValues values = new ContentValues();
		values.put(TEAM_TEAMID, _id);
		values.put(PLAYER_PLAYERNAME, player_name);
		values.put(PLAYER_TEAMID, team_id);
		values.put(PLAYER_POSITION, position);
		values.put(PLAYER_NUMBER, number);
		return values;
	}
	
	private ContentValues createMatchContentValues(long _id, long home_team_id, long away_team_id, String status, String kick_off, String venue) {
		ContentValues values = new ContentValues();
		values.put(MATCH_MATCHID, _id);
		values.put(MATCH_HOMETEAMID, home_team_id);
		values.put(MATCH_AWAYTEAMID, away_team_id);
		values.put(MATCH_STATUS, status);
		values.put(MATCH_KICKOFF, kick_off);
		values.put(MATCH_VENUE, venue);
		return values;
	}

	// ****************************** insert - table values ***********************
	
	public long createTeam(long _id, String team_name, String country_code, String coach) {
		ContentValues values = createTeamContentValues( _id,  team_name,  country_code,  coach);
		return db.insert(DB_TABLE_TEAM, null, values);
	}
	
	public long createPlayer(long _id, String player_name, long team_id, String position, long number) {
		ContentValues values = createPlayerContentValues(  _id,  player_name,  team_id,  position, number);
		return db.insert(DB_TABLE_PLAYER, null, values);
	}
	
	public long createMatch( long _id, long home_team_id, long away_team_id, String status, String kick_off, String venue) {
		ContentValues values = createMatchContentValues( _id,  home_team_id,  away_team_id,  status,  kick_off,  venue);
		return db.insert(DB_TABLE_MATCH, null, values);
	}
	
	// ****************************** update - table values ***********************
	
	//todo
	
	
	// ***************************** query to list in app *************************
	
    // Get ALL Match
    public List<MatchDTO> getAllMatch() {
        List<MatchDTO> matchList = new LinkedList<MatchDTO>();
 
        // 1. build the query
        String query = "select m._id,m.home_team_id,m.away_team_id, homeTeam.team_name, homeTeam.country_code, awayTeam.team_name,awayTeam.country_code, m.kick_off, m.venue "+
        				"from match m, team homeTeam, team awayTeam " +
        				"where m.home_team_id = homeTeam._id "+
        				"and m.away_team_id=awayTeam._id;";
 
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        
        System.out.println(">>>>>>>>>>>>> getAllMatch count = " + cursor.getCount());
        // 3. go over each row, build match and add it to list
        MatchDTO match = null;
        TeamDTO homeTeam = new TeamDTO();
        TeamDTO awayTeam = new TeamDTO();
              
        cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
          match = new MatchDTO();
          match.setMatchId(Integer.parseInt(cursor.getString(0)));
          
          match.setHomeTeam(homeTeam);
          match.getHomeTeam().setTeamId(Integer.parseInt(cursor.getString(1)));
          
          match.setAwayTeam(awayTeam);
          match.getAwayTeam().setTeamId(Integer.parseInt(cursor.getString(2)));
          match.getHomeTeam().setTeamName(cursor.getString(3));
          match.getHomeTeam().setCountryCode(cursor.getString(4));
          match.getAwayTeam().setTeamName(cursor.getString(5));
          match.getAwayTeam().setCountryCode(cursor.getString(6));
          match.setKickOff(Date.valueOf(cursor.getString(7)));
          match.setVenue(cursor.getString(8));
          matchList.add(match);
          cursor.moveToNext();
		}
		db.close();

        Log.d("getAllMatch()", matchList.toString());
       // db.close();
        return matchList;
    }
    
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
	
}
