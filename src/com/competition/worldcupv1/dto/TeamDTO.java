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
	private String group;
	
	public TeamDTO (){}
	
	public TeamDTO(String teamName, String countryCode) {
        super();
        this.teamName = teamName;
        this.countryCode = countryCode;
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

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
	
}
