package com.competition.worldcupv1.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GameDTO implements Serializable{
	
	private Integer gameId;
	private String time;
	private String venue;
	private Integer team1Id;
	private String team1Flag;
	private Integer team2Id;
	private String team2Flag;
	private Integer playerInfoStatus;
	
	public Integer getGameId() {
		return gameId;
	}
	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getVenue() {
		return venue;
	}
	public void setVenue(String venue) {
		this.venue = venue;
	}
	public Integer getTeam1Id() {
		return team1Id;
	}
	public void setTeam1Id(Integer team1Id) {
		this.team1Id = team1Id;
	}

	public String getTeam1Flag() {
		return team1Flag;
	}
	public void setTeam1Flag(String team1Flag) {
		this.team1Flag = team1Flag;
	}
	public Integer getTeam2Id() {
		return team2Id;
	}
	public void setTeam2Id(Integer team2Id) {
		this.team2Id = team2Id;
	}

	public String getTeam2Flag() {
		return team2Flag;
	}
	public void setTeam2Flag(String team2Flag) {
		this.team2Flag = team2Flag;
	}
	public Integer getPlayerInfoStatus() {
		return playerInfoStatus;
	}
	public void setPlayerInfoStatus(Integer playerInfoStatus) {
		this.playerInfoStatus = playerInfoStatus;
	}
		
}
