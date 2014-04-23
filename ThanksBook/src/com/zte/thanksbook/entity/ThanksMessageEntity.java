package com.zte.thanksbook.entity;

import java.sql.Blob;
import java.util.List;

import android.net.Uri;

public class ThanksMessageEntity {

	/**
	 * 本地ID
	 */
	private int id;
	
	private String messageType;
	
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
	private Long createBy;
	
	/**
	 * 感恩对象
	 */
	private String thanksTo;
	
	/**
	 * 图片
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
