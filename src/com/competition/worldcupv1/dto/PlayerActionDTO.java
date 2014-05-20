package com.competition.worldcupv1.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PlayerActionDTO implements Serializable{
	
	private Integer userId;
	private String uid;
	private String userLogin;
	private Integer playerId;
	private Integer actionId;
	private Integer gameId;
		
	public PlayerActionDTO() {
	}
	
	public PlayerActionDTO(Integer userId, String uid, String userLogin,
			Integer playerId, Integer actionId, Integer gameId) {
		super();
		this.userId = userId;
		this.uid = uid;
		this.userLogin = userLogin;
		this.playerId = playerId;
		this.actionId = actionId;
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
	public Integer getPlayerId() {
		return playerId;
	}
	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}
	public Integer getActionId() {
		return actionId;
	}
	public void setActionId(Integer actionId) {
		this.actionId = actionId;
	}
	public Integer getGameId() {
		return gameId;
	}
	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}
}
