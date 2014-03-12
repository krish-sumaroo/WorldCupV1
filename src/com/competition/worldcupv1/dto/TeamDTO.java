package com.competition.worldcupv1.dto;

import java.io.Serializable;

/**
 * @author roma
 *
 */
@SuppressWarnings("serial")
public class TeamDTO implements Serializable{
	
	private Integer teamId;
	private String teamName;
	private String countryCode;
	private String coach;
	
	public TeamDTO (){}
	
	public TeamDTO(String teamName, String countryCode, String coach) {
        super();
        this.teamName = teamName;
        this.countryCode = countryCode;
        this.coach = coach;
    }
	
	public Integer getTeamId() {
		return teamId;
	}
	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getCoach() {
		return coach;
	}

	public void setCoach(String coach) {
		this.coach = coach;
	}
	
}
