package com.competition.worldcupv1.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GameInfoDTO implements Serializable{
	
	private String time;
	private String venue;
	private Integer team1Id;
	private Integer team2Id;
	private Integer playerInfoStatus;
	private String team1Name;
	private String team1Flag;
	private String team1Group;
	private String team2Name;
	private String team2Flag;
	private String team2Group;
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
	public Integer getTeam2Id() {
		return team2Id;
	}
	public void setTeam2Id(Integer team2Id) {
		this.team2Id = team2Id;
	}
	public Integer getPlayerInfoStatus() {
		return playerInfoStatus;
	}
	public void setPlayerInfoStatus(Integer playerInfoStatus) {
		this.playerInfoStatus = playerInfoStatus;
	}
	public String getTeam1Name() {
		return team1Name;
	}
	public void setTeam1Name(String team1Name) {
		this.team1Name = team1Name;
	}
	public String getTeam1Flag() {
		return team1Flag;
	}
	public void setTeam1Flag(String team1Flag) {
		this.team1Flag = team1Flag;
	}
	public String getTeam1Group() {
		return team1Group;
	}
	public void setTeam1Group(String team1Group) {
		this.team1Group = team1Group;
	}
	public String getTeam2Name() {
		return team2Name;
	}
	public void setTeam2Name(String team2Name) {
		this.team2Name = team2Name;
	}
	public String getTeam2Flag() {
		return team2Flag;
	}
	public void setTeam2Flag(String team2Flag) {
		this.team2Flag = team2Flag;
	}
	public String getTeam2Group() {
		return team2Group;
	}
	public void setTeam2Group(String team2Group) {
		this.team2Group = team2Group;
	}
	public GameInfoDTO() {

	}
	public GameInfoDTO(String time, String venue, Integer team1Id,
			Integer team2Id, Integer playerInfoStatus, String team1Name,
			String team1Flag, String team1Group, String team2Name,
			String team2Flag, String team2Group) {
		super();
		this.time = time;
		this.venue = venue;
		this.team1Id = team1Id;
		this.team2Id = team2Id;
		this.playerInfoStatus = playerInfoStatus;
		this.team1Name = team1Name;
		this.team1Flag = team1Flag;
		this.team1Group = team1Group;
		this.team2Name = team2Name;
		this.team2Flag = team2Flag;
		this.team2Group = team2Group;
	}

}
