package chapter1_2.user.dao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import chapter1_1.user.domain.User;

/*
 * 세번째 UserDao 개선안
 * "분리와 확장"을 통하여 변경이 일어날 때 필요한 작업을 최소화 한다.
 * 리펙토링 : 상속을 통한 확장 기법 
*/
public abstract class UserDao_Extend {
	public void add(User user) throws ClassNotFoundException, SQLException {
		// 1. DB Connection을 가져온다.
		Connection c = getConnection();
		
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
		Connection c = getConnection();
		
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
	// 커넥션 부분만 분리 하였다.
	public abstract Connection getConnection() throws ClassNotFoundException, SQLException; 
}