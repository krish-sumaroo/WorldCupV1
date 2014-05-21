package com.competition.worldcupv1.dto;

public class Player {
	public int icon;
	public String name;
	public String position;
	public String number;
	public int playerId;
	public int teamId;
	public Player(){
		super();
	}
	public Player(int icon, String name, String position, String number,
			int playerId, int teamId) {
		super();
		this.icon = icon;
		this.name = name;
		this.position = position;
		this.number = number;
		this.playerId = playerId;
		this.teamId = teamId;
	}
	
}
