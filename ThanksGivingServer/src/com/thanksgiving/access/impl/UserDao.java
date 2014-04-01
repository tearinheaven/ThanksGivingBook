package com.thanksgiving.access.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.thanksgiving.access.IUserDao;
import com.thanksgiving.entity.User;
import com.thanksgiving.service.bo.UserBO;
import com.thanksgiving.util.TGUtil;

@Repository("userDao")
public class UserDao extends BaseDao implements IUserDao {

	@Override
	public int addUser(User user) {
		return (Integer)this.getSession().save(user);
	}

	@Override
	public boolean isUserNameExists(String userName) {
		String hql = "select count(*) from User where userName=:userName";
		
		Query q = this.getSession().createQuery(hql);
		q.setParameter("userName", userName);
		int count = Integer.valueOf(q.iterate().next().toString());
		if (count > 0)
		{
			return true;
		}
		
		return false;
	}

	@Override
	public boolean isUserEmailExists(String userEmail) {
		String hql = "select count(*) from User where userEmail=:userEmail";
		
		Query q = this.getSession().createQuery(hql);
		q.setParameter("userEmail", userEmail);
		int count = Integer.valueOf(q.iterate().next().toString());
		if (count > 0)
		{
			return true;
		}
		
		return false;
	}
	/**
	 * 用户登录
	 * @param userBO
	 * @return
	 */
	@Override
	public UserBO userLogin(UserBO userBO)
	{
		UserBO rs = new UserBO();
		
		StringBuffer hql = new StringBuffer();
		hql.append(" from User where userPassword=:userPassword ");
		if (!TGUtil.isEmpty(userBO.getUser().getUserName()))
		{
			hql.append(" and userName=:userName ");
		}
		else
		{
			hql.append(" and userEmail=:userEmail ");
		}
		
		Query q = this.getSession().createQuery(hql.toString());
		q.setParameter("userPassword", userBO.getUser().getUserPassword());
		if (!TGUtil.isEmpty(userBO.getUser().getUserName()))
		{
			q.setParameter("userName", userBO.getUser().getUserName());
		}
		else
		{
			q.setParameter("userEmail", userBO.getUser().getUserEmail());
		}
		
		List<User> users = q.list();
		if (users==null || users.size() < 1)
		{
			rs.setResult(UserBO.OPERACTION_FAILURE);
		}
		else
		{
			User user = users.get(0);
			user.setLastUpdateDateString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(user.getLastUpdateDate().getTime())));
			rs.setResult(UserBO.OPERACTION_SUCCESS);
			rs.setUser(user);
		}

		return rs;
	}
}

