package com.thanksgiving.access;

import com.thanksgiving.entity.User;

public interface IUserDao {
	public int addUser(User user);
	
	public boolean isUserNameExists(String userName);
	
	public boolean isUserEmailExists(String userEmail);
}
