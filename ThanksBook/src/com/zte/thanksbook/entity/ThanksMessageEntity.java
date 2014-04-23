package com.zte.thanksbook.entity;

import java.sql.Blob;
import java.util.List;

import android.net.Uri;

public class ThanksMessageEntity {

	/**
	 * ����ID
	 */
	private int id;
	
	private String messageType;
	
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
	private Long createBy;
	
	/**
	 * �ж�����
	 */
	private String thanksTo;
	
	/**
	 * ͼƬ
	 */
	private List<Uri> imgs;
	
	
	public List<Uri> getImgs() {
		return imgs;
	}
	public void setImgs(List<Uri> imgs) {
		this.imgs = imgs;
	}
	public int getId() {
		return id;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
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
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
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
