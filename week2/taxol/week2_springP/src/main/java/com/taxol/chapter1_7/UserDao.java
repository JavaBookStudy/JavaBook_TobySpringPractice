package com.taxol.chapter1_7;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import chapter1_7.user.domain.User;


/*
 * list 1.11
 * 생성자를 수정하여, 이제 어떠한 connectionMaker를  사용할 지 UserDao는 알 필요가 없다
 *
 * list 1.33
 * Setter Method를 사용하여 DI 방식을 사용하였다. 
*/
public class UserDao {
	
	private ConnectionMaker connectionMaker;
	
	// 이제 DB 접속 관심은 독립적이게 되었다.
	/*
	public UserDao(ConnectionMaker conn) { // 의존 관계 주입 : UserDao -> ConnectionMaker
		connectionMaker = conn;
	}*/
	
	// 수정자를 사용하여 DI를 수행한다.
	public void setConnectionMaker(ConnectionMaker connectionMaker) {
		this.connectionMaker = connectionMaker;
	}
	
	public void add(User user) throws ClassNotFoundException, SQLException {
		// 1. DB Connection을 가져온다.
		Connection c = connectionMaker.makeConnection();
		
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
		Connection c = connectionMaker.makeConnection();
		
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
	
}