package com.competition.worldcupv1.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ScorePredictionDTO implements Serializable{
	
	private Integer userId;
	private String uid;
	private String userLogin;
	private Integer team1Score;
	private Integer team2Score;
	private Integer gameId;
	
	public ScorePredictionDTO() {
		
	}
	
	public ScorePredictionDTO(Integer userId, String uid, String userLogin,
			Integer team1Score, Integer team2Score, Integer gameId) {
		super();
		this.userId = userId;
		this.uid = uid;
		this.userLogin = userLogin;
		this.team1Score = team1Score;
		this.team2Score = team2Score;
		this.gameId = gameId;
	}

	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUserLogin() {
		return userLogin;
	}
	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}
	public Integer getTeam1Score() {
		return team1Score;
	}
	public void setTeam1Score(Integer team1Score) {
		this.team1Score = team1Score;
	}
	public Integer getTeam2Score() {
		return team2Score;
	}
	public void setTeam2Score(Integer team2Score) {
		this.team2Score = team2Score;
	}
	public Integer getGameId() {
		return gameId;
	}
	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}
}
