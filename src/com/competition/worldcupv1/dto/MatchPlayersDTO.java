package com.competition.worldcupv1.dto;

import java.util.List;

public class MatchPlayersDTO {
	
	private List<PlayerDTO> homeTeamPlayers;
	private List<PlayerDTO> awayTeamPlayers;
	
	public List<PlayerDTO> getHomeTeamPlayers() {
		return homeTeamPlayers;
	}
	public void setHomeTeamPlayers(List<PlayerDTO> homeTeamPlayers) {
		this.homeTeamPlayers = homeTeamPlayers;
	}
	public List<PlayerDTO> getAwayTeamPlayers() {
		return awayTeamPlayers;
	}
	public void setAwayTeamPlayers(List<PlayerDTO> awayTeamPlayers) {
		this.awayTeamPlayers = awayTeamPlayers;
	}
	
}
