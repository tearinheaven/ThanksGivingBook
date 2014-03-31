package com.zte.thanksbook.entity;

import java.sql.Timestamp;

public class User {
	private String userEmail;
	
	private String userId;
	
	private String userName;
	
	private String userSignature;
	
	private String lastUpdateDateString;

	public String getLastUpdateDateString() {
		return lastUpdateDateString;
	}

	public void setLastUpdateDateString(String lastUpdateDateString) {
		this.lastUpdateDateString = lastUpdateDateString;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserSignature() {
		return userSignature;
	}

	public void setUserSignature(String userSignature) {
		this.userSignature = userSignature;
	}
}
