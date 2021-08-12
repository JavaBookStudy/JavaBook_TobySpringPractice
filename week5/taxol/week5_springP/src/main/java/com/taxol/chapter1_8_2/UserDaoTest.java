package com.taxol.chapter1_8_2;

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
		public static void main(String[] args) throws ClassNotFoundException, SQLException{
			
			ApplicationContext context = new GenericXmlApplicationContext("classpath:applicationContext.xml");
			
			UserDao dao = context.getBean("userDao", UserDao.class);
			
			User user = new User();
			user.setId("whitesh18");
			user.setName("baek");
			user.setPassword("married");
			
			dao.add(user);
			
			System.out.println(user.getId() + "등록 성공");
			
			User user2 = dao.get(user.getId());
			System.out.println(user2.getName());
			System.out.println(user2.getPassword());
			System.out.println(user2.getId() + " 조회 성공");
		}
}
