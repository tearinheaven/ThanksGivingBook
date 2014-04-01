package com.zte.thanksbook.service;

public class UserBO {
	//保存成功
	public static int SAVE_SUCCESS = 1;
	//缺少邮件地址
	public static int LACK_EMAIL = 2;
	//缺少密码
	public static int LACK_PASSWORD = 3;
	//用户名重复
	public static int USERNAME_DUPLICATE = 4;
	//邮件地址重复
	public static int USEREMAIL_DUPLICATE = 5;
}
