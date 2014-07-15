package com.normal.ordering.dao;

import com.normal.ordering.entities.DiscountFood;
import com.normal.ordering.entities.User;

/**
 * 用户本地数据处理接口
 * 
 * @author VBaboon
 * @date 2014-6-22
 */
public interface UserDao {
	/**
	 * 把用户数据写入本地数据库
	 * 
	 * @param user
	 * @throws Exception
	 */
	public void userUpdate(User user) throws Exception;

	/**
	 * 把打折信息写入数据库
	 * 
	 * @param discountFood
	 * @throws Exception
	 */
	public void disUpdate(DiscountFood discountFood) throws Exception;

	/**
	 * 本地获取用户信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public User getUser() throws Exception;
}
