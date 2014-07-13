package com.normal.ordering.dao;

import com.normal.ordering.entities.DiscountFood;
import com.normal.ordering.entities.User;

/**
 * 用户本地数据处理具体实现代码
 * 
 * @author VBaboon
 * @date 2014-6-22
 */
public class UserDaoImpl implements UserDao {
	@Override
	public void userUpdate(User user) {

	}

	@Override
	public void disUpdate(DiscountFood discountFood) {

	}

	@Override
	public User getUser() {
		User user = new User();
		return user;
	}

}
