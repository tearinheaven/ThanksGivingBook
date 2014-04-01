package com.thanksgiving.service;

import com.thanksgiving.service.bo.UserBO;

public interface IUserDs {
	/**
	 * 新增用户
	 * @param userBO
	 * @return
	 */
	public UserBO addUser(UserBO userBO);
	
	/**
	 * 用户登录
	 * @param userBO
	 * @return
	 */
	public UserBO userLogin(UserBO userBO);
}
