package com.taxol.chapter1_9;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import chapter1_7.user.domain.User;


/*
 * list 1.40
 * applicationContext.xml을 통하여 DI 수행 
 * 
*/
public class UserDaoTest {
		// 실제 테스트를 위한 main
		public static void main(String[] args) throws SQLException{
			
			ApplicationContext context = new GenericXmlApplicationContext("classpath:applicationContext.xml");
			
			UserDao dao = context.getBean("userDao", UserDao.class);
			
			User user = new User();
			user.setId("whitesh19");
			user.setName("baek");
			user.setPassword("married");
			
			dao.add(user);
			
			System.out.println(user.getId() + " 등록 성공");
			
			User user2 = dao.get(user.getId());
			if(!user.getName().contentEquals(user2.getName()))
				System.out.println("Test failed (name)");
			else if(!user.getPassword().contentEquals(user2.getPassword()))
				System.out.println("Test failed (password)");
			else
				System.out.println(user2.getId() + ": Test Success");
		}
}
