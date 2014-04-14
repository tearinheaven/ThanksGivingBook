package com.zte.thanksbook.activities;

public interface ThanksShadowLinstener {
	/**
	 * 主按钮事件
	 */
	public void mainAction();
	
	/**
	 * 次要按钮事件
	 */
	public void subAction();
	
	/**
	 * 取消按钮事件
	 */
	public void cancelAction();
}
