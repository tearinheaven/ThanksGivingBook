package com.zte.thanksbook.service;

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
}
