package chapter1_3_3.user.dao;

import java.sql.SQLException;

import chapter1_1.user.domain.User;

/*
 * list 1.13
 * 클라이언트 역할의 UserDaoTest
 * UserDao와 ConnectionMaker 구현 클래스의 관계를 결정해준다.
*/
public class UserDaoTest {
	// 실제 테스트를 위한 main
		public static void main(String[] args) throws ClassNotFoundException, SQLException{
			
			DConnectionMaker dConn = new DConnectionMaker();
			
			UserDao dao = new UserDao(dConn);
			
			User user = new User();
			user.setId("whiteship6");
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
