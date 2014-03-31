package com.thanksgiving.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thanksgiving.access.IUserDao;
import com.thanksgiving.entity.User;
import com.thanksgiving.service.IUserDs;
import com.thanksgiving.service.bo.UserBO;
import com.thanksgiving.util.TGUtil;

@Service("userDs")
public class UserDs implements IUserDs {
	@Autowired
	private IUserDao userDao;

	@Override
	public UserBO addUser(UserBO userBO) {
		User user = userBO.getUser();
		String userEmail = user.getUserEmail();
		String userPassword = user.getUserPassword();
		if (TGUtil.isEmpty(userEmail))
		{
			userBO.setResult(UserBO.LACK_EMAIL);		
			return userBO;
		}
		if (TGUtil.isEmpty(userPassword))
		{
			userBO.setResult(UserBO.LACK_PASSWORD);		
			return userBO;
		}
		
		//判断邮件地址是否重复
		if (this.userDao.isUserEmailExists(userEmail))
		{
			userBO.setResult(UserBO.USEREMAIL_DUPLICATE);		
			return userBO;
		}		
		
		//用户名取邮箱名 @之前的内容
		String userName = userEmail.substring(0, userEmail.indexOf("@"));
		//校验用户名是否重复
		if (this.userDao.isUserNameExists(userName))
		{
			//用户名重复时用邮件地址去掉@代替
			userName = userEmail.replace("@", "");
		}
		user.setUserName(userName);
		Timestamp time = new Timestamp(new Date().getTime());
		user.setCreationDate(time);
		user.setEnableFlag("Y");
		user.setLastUpdateDate(time);
		
		user.setUserId(this.userDao.addUser(user));		
		user.setLastUpdateDateString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time.getTime())));
		userBO.setResult(UserBO.SAVE_SUCCESS);		
		return userBO;
	}

	public IUserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

}
