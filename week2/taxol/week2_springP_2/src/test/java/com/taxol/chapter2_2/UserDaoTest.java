package com.taxol.chapter2_2;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import chapter1_7.user.domain.User;


/*
 * list 2.5
 * JUnit을 적용한 UserDaoTest 
*/
public class UserDaoTest {
	@Test
	public void addAndGet() throws SQLException{
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		UserDao dao = context.getBean("userDao", UserDao.class);
		
		User user = new User();
		user.setId("gyumee4");
		user.setName("Taxol");
		user.setPassword("loved");
		
		dao.add(user);
		
		System.out.println(user.getId() + " 등록 성공");
		
		User user2 = dao.get(user.getId());
		
		// 검증 코드
		assertThat(user2.getName(), is(user.getName()));
		assertThat(user2.getPassword(), is(user.getPassword()));
	}
}
