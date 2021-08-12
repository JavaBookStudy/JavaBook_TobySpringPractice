package com.taxol.chapter4_2;

import java.util.List;

import com.taxol.chapter4_2.User;

public interface UserDao {
	void add(User user);
	User get(String id);
	void deleteAll();
	int getCount();
	List<User> getAll();
}
