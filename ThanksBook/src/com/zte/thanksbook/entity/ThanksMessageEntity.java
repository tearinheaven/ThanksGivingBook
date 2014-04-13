package com.zte.thanksbook.entity;

import java.sql.Blob;

public class ThanksMessageEntity {

	/**
	 * ����ID
	 */
	private int id;
	
	/**
	 * �ж�����
	 */
	private String messageText;
	
	/**
	 * �ж�ͼƬ
	 */
	private Blob messageImg;
	/**
	 * ��������
	 */
	private String createDate;
	
	/**
	 * ������
	 */
	private String createBy;
	
	/**
	 * �ж�����
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
