package com.thanksgiving.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ts_user")
public class User {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
	private int userId;

    @Column(name = "user_name", nullable = false)
	private String userName;

    @Column(name = "user_email", nullable = false)
	private String userEmail;

	@Column(name = "user_password")
	private String userPassword;
	
    @Column(name = "user_signature")
	private String userSignature;
	
    @Column(name = "creation_date")
	private Timestamp creationDate;
	
    @Column(name = "last_update_date")
	private Timestamp lastUpdateDate;
    
    private String lastUpdateDateString;
	
    @Column(name = "enable_flag")
	private String enableFlag;

	public String getLastUpdateDateString() {
		return lastUpdateDateString;
	}

	public void setLastUpdateDateString(String lastUpdateDateString) {
		this.lastUpdateDateString = lastUpdateDateString;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
    public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserSignature() {
		return userSignature;
	}

	public void setUserSignature(String userSignature) {
		this.userSignature = userSignature;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public Timestamp getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Timestamp lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}	
}
