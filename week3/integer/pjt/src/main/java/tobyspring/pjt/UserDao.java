package tobyspring.pjt;/*
 * 1.1.2
 * 사용자 정보를 DB에 넣고 관리할 수 있는 DAO 클래스
 */

import tobyspring.pjt.dao.ConnectionMaker;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
	
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}
	

	public void add(User user) throws ClassNotFoundException, SQLException {
		Connection c = dataSource.getConnection();

		PreparedStatement ps = c.prepareStatement(
				"insert into users(id, name, password) values(?, ?, ?)");
		ps.setString(1, user.getId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());
		
		ps.executeUpdate();
		
		ps.close();
		c.close();
	}
	
	public User get(String id) throws ClassNotFoundException, SQLException {

		Connection c = dataSource.getConnection();
		PreparedStatement ps = c.prepareStatement(
				"select * from users where id = ?");
		ps.setString(1, id);
		
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
	
	// 메서드 추출 기법 (extract method)
	// 구현 코드는 제거되고 추상 메서드로 바뀌었다. 메서드의 구현은 서브클래스가 담당.
	// 템플릿 메서드 패턴: super class에 기본적인 로직의 흐름을 만들고, 그 기능의 일부를 sub class에서 필요에 맞게 구현해서 사용하는 방법
	// 팩터리 메서드 패턴: 서브클래스에서 구체적인 오브젝트 생성 방법을 결정하게 함
	// public abstract Connection getConnection() throws ClassNotFoundException, SQLException;
	

}
