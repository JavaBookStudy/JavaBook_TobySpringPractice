package chapter1_3_1.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import chapter1_1.user.domain.User;

/*
 * list 1.6
 * 독립적인 클래스로 만들어, 관심사를 분리시켜 보았다.
*/
public class UserDao {
	
	private SimpleConnectionMaker simpleConnectionMaker;
	
	public UserDao() {
		simpleConnectionMaker = new SimpleConnectionMaker();
	}

	public void add(User user) throws ClassNotFoundException, SQLException {
		// 1. DB Connection을 가져온다.
		Connection c = simpleConnectionMaker.makeNewConnection();
		
		// 2. SQL을 담은 Statement를 만든다
		PreparedStatement ps = c.prepareStatement(
				"insert into users(id, name, password) values(?, ?, ?)");
		ps.setString(1, user.getId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());
		
		// 3. Statement를 실행한다
		ps.executeUpdate();
		
		ps.close();
		c.close();
	}
	
	public User get(String id) throws ClassNotFoundException, SQLException {
		Connection c = simpleConnectionMaker.makeNewConnection();
		
		PreparedStatement ps = c.prepareStatement(
				"select * from users where id = ?");
		ps.setString(1, id);
		
		// 4. SQL 실행 결과를 ResultSet으로 받아 정보를 저장할 Object에 옮긴다.
		ResultSet rs = ps.executeQuery();
		rs.next();
		User user = new User();
		user.setId(rs.getString("id"));
		user.setName(rs.getString("name"));
		user.setPassword(rs.getString("password"));
		
		rs.close();
		ps.close();
		c.close();
		
		return user;
	}
	// 실제 테스트를 위한 main
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		UserDao dao = new UserDao();
		
		User user = new User();
		user.setId("whiteship4");
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