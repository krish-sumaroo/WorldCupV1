package com.competition.worldcupv1.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserDTO implements Serializable{
	
	private String name;
	private String uid;
	private String country;
	
	public UserDTO(){}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
		
}
