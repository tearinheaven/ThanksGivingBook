package com.thanksgiving.service.bo;

import com.thanksgiving.entity.User;

public class UserBO {
	//操作成功
	public static int OPERACTION_SUCCESS = 1;
	//操作失败
	public static int OPERACTION_FAILURE = 2;
	//缺少邮件地址
	public static int LACK_EMAIL = 3;
	//缺少密码
	public static int LACK_PASSWORD = 4;
	//用户名重复
	public static int USERNAME_DUPLICATE = 5;
	//邮件地址重复
	public static int USEREMAIL_DUPLICATE = 6;
	
	private User user;
	
	private int result;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
