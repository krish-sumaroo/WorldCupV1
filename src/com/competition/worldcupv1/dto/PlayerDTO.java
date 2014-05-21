package com.competition.worldcupv1.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PlayerDTO implements Serializable{
	
	private Integer playerId;
	private String name;
	private String position;
	private Integer number;
	private Integer teamId;
	
	public Integer getPlayerId() {
		return playerId;
	}
	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public Integer getTeamId() {
		return teamId;
	}
	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}
	public PlayerDTO(Integer playerId, String name, String position,
			Integer number, Integer teamId) {
		super();
		this.playerId = playerId;
		this.name = name;
		this.position = position;
		this.number = number;
		this.teamId = teamId;
	}
	public PlayerDTO() {
	}
	
}
