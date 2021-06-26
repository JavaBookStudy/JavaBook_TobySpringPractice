package chapter1_2.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import chapter1_1.user.domain.User;

/*
 * UserDao_Extend를 상속받아, DB Connection 부분만 원하는 대로 설정하였다.
 * 템플릿 메소드 패턴
 * -> 슈퍼클래스 기능의 일부를 추상 메소드나 오버라이딩이 가능한 protected 메소드 등으로 만든 뒤,
 * -> 서브클래스에서 이런 메소드를 필요에 맞게 구현해서 사용하도록 하는 디자인 패턴
*/
public class NUserDao extends UserDao_Extend{
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/tobyspring", "ssafy", "ssafy");
		return c;
	}
	
	// 실제 테스트를 위한 main
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		UserDao_Extend dao = new NUserDao();
		
		User user = new User();
		user.setId("whiteship3");
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
