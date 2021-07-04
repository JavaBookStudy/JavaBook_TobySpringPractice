package com.taxol.chapter1_8;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import chapter1_7.user.domain.User;


/*
 * list 1.32
 * CountingConnectionMaker에 대한 테스트 클래스
*/
public class UserDaoConnectionCountingTest {
		// 실제 테스트를 위한 main
		public static void main(String[] args) throws ClassNotFoundException, SQLException{
			
			ApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
			
			UserDao dao = context.getBean("userDao", UserDao.class);
			
			User user = new User();
			user.setId("whitesh12");
			user.setName("baek");
			user.setPassword("married");
			
			dao.add(user);
			
			System.out.println(user.getId() + "등록 성공");
			
			User user2 = dao.get(user.getId());
			System.out.println(user2.getName());
			System.out.println(user2.getPassword());
			System.out.println(user2.getId() + " 조회 성공");
			
			
			CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);
			System.out.println("Connection counter : " + ccm.getCounter());
		}
}
