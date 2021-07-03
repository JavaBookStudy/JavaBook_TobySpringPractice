package com.taxol.chapter1_7;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import chapter1_7.user.domain.User;


/*
 * list 1.19
 * 클라이언트 역할의 UserDaoTest
 * 애플리케이션 컨텍스트를 적용하였다.
*/
public class UserDaoConnectionCountingTest {
		// 실제 테스트를 위한 main
		public static void main(String[] args) throws ClassNotFoundException, SQLException{
			
			ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
			
			UserDao dao = context.getBean("userDao", UserDao.class);
			
			User user = new User();
			user.setId("whitesh11");
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
