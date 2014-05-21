package com.competition.worldcupv1.dto;

import java.io.Serializable;

/**
 * @author roma
 *
 */
@SuppressWarnings("serial")
public class TeamDTO implements Serializable{
	
	private Integer teamId;
	private String name;
	private String flag;
	private String group;
	
	public TeamDTO (){}
	

	
	public TeamDTO(Integer teamId, String name, String flag, String group) {
		super();
		this.teamId = teamId;
		this.name = name;
		this.flag = flag;
		this.group = group;
	}



	public Integer getTeamId() {
		return teamId;
	}



	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getFlag() {
		return flag;
	}



	public void setFlag(String flag) {
		this.flag = flag;
	}



	public String getGroup() {
		return group;
	}



	public void setGroup(String group) {
		this.group = group;
	}



	@Override
	public String toString() {
	    return this.name;
	}
	
}
