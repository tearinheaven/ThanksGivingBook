package com.zte.thanksbook.entity;

import java.sql.Blob;

public class ThanksMessageEntity {

	/**
	 * 本地ID
	 */
	private int id;
	
	/**
	 * 感恩文字
	 */
	private String messageText;
	
	/**
	 * 感恩图片
	 */
	private Blob messageImg;
	/**
	 * 创建日期
	 */
	private String createDate;
	
	/**
	 * 创建者
	 */
	private String createBy;
	
	/**
	 * 感恩对象
	 */
	private String thanksTo;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getThanksTo() {
		return thanksTo;
	}
	public void setThanksTo(String thanksTo) {
		this.thanksTo = thanksTo;
	}
	public Blob getMessageImg() {
		return messageImg;
	}
	public void setMessageImg(Blob messageImg) {
		this.messageImg = messageImg;
	}
	
}
