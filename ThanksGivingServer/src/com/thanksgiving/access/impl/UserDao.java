package com.thanksgiving.access.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.thanksgiving.access.IUserDao;
import com.thanksgiving.entity.User;

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
}

