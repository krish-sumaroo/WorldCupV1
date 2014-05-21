package com.competition.worldcupv1.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserDTO implements Serializable{
	
	private String userName;
	private String uid;
	private String country;
	private String nickName;
	private String password;
	private long favTeam;
	private String regId;
		
	public UserDTO(){}



	public UserDTO(String userName, String uid, String country,
			String nickName, String password, long favTeam, String regId) {
		super();
		this.userName = userName;
		this.uid = uid;
		this.country = country;
		this.nickName = nickName;
		this.password = password;
		this.favTeam = favTeam;
		this.regId = regId;
	}



	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getFavTeam() {
		return favTeam;
	}

	public void setFavTeam(long favTeam) {
		this.favTeam = favTeam;
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

	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}
		
}
