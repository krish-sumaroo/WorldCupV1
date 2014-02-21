package com.competition.worldcupv1.dto;

import java.io.Serializable;

import com.competition.worldcupv1.enums.Role;


/**
 * @author roma
 *
 */
@SuppressWarnings("serial")
public class PlayerDTO implements Serializable{
	
	private Integer playerId;
	private String playerName;
	private Integer teamId;
	private Role role;
	
	public PlayerDTO (){}
	
	public PlayerDTO(String playerName, Integer teamId, Role role) {
        super();
        this.playerName = playerName;
        this.teamId= teamId;
        this.role=role;
    }
	
	public Integer getPlayerId() {
		return playerId;
	}
	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public Integer getTeamId() {
		return teamId;
	}
	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	
}
