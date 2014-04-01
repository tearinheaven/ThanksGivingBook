package com.thanksgiving.access;

import com.thanksgiving.entity.User;
import com.thanksgiving.service.bo.UserBO;

public interface IUserDao {
	public int addUser(User user);
	
	public boolean isUserNameExists(String userName);
	
	public boolean isUserEmailExists(String userEmail);
	
	/**
	 * 用户登录
	 * @param userBO
	 * @return
	 */
	public UserBO userLogin(UserBO userBO);
}
