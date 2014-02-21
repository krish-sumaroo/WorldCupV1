package com.competition.worldcupv1.dto;

public class ScoreDTO {
	
	private Integer homeGoals;
	private Integer awayGoals;
	
	public ScoreDTO() { }
	
	public ScoreDTO( Integer homeGoals, Integer awayGoals) {
		this.homeGoals = homeGoals;
		this.awayGoals = awayGoals;
	}
	
	
	/**
	 * @return the awayGoals
	 */
	public Integer getAwayGoals() {
		return awayGoals;
	}
	
	/**
	 * @param awayGoals the awayGoals to set
	 */
	public void setAwayGoals(Integer awayGoals) {
		this.awayGoals = awayGoals;
	}

	/**
	 * @return the homeGoals
	 */
	public Integer getHomeGoals() {
		return homeGoals;
	}

	/**
	 * @param homeGoals the homeGoals to set
	 */
	public void setHomeGoals(Integer homeGoals) {
		this.homeGoals = homeGoals;
	}	
}
