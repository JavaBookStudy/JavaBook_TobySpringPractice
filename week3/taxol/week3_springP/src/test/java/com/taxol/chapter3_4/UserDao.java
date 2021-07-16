package com.taxol.chapter3_4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

/*
 * list 1.42 : DataSource를 사용하는 UserDao
 * list 2.7  : deleteAll & getCount가 추가되었다.
 * list 2-14 : get() 메소드에서 데이터를 찾지 못하면 예외를 발생 시키도록 수정하였다. 
 * list 3-3  : JDBC 예외처리가 발생해도 resource를 반환하도록 만들었다.
 * list 3-7  : 템플릿 메서드 패턴 적용 -> 추상 클래스로 변경하였다가 전략 패턴을 사용하면서 다시 일반 클래스로 변환함
 * list 3-10 : 전략 패턴에 따라 deleteAll() 메서드에서 변하는 부분을 다른 오브젝트로 분리하였다가, interface를 통해 연결한다.
 * list 3-11 : DI 적용을 위해 클라이언트/컨텍스트 분리하였다.
 * list 3-12 : add() 메서드도 PreparedStatement 생성 로직을 분리하였다.
 * list 3-17 : add() 메서드에서 로컬 변수를 직접 사용하도록 수정.
 * list 3-18 : deleteAll(), add() 메서드를 익명 클래스를 통해 사용하도록 수정.
*/
public class UserDao {
	
	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	private JdbcContext jdbcContext;
	
	public void setJdbcContext(JdbcContext jdbcContext) {
		this.jdbcContext = jdbcContext;
	}

	public void add(final User user) throws SQLException {
		/*
		class AddStatement implements StatementStrategy{
			@Override
			public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
				PreparedStatement ps = c.prepareStatement(
						"insert into users(id, name, password) values(?, ?, ?)");
				ps.setString(1, user.getId());
				ps.setString(2, user.getName());
				ps.setString(3, user.getPassword());
				return ps;
			}
		}
		*/
		jdbcContext.workWithStatementStrategy(new StatementStrategy() {
			@Override
			public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
				PreparedStatement ps = c.prepareStatement(
						"insert into users(id, name, password) values(?, ?, ?)");
				ps.setString(1, user.getId());
				ps.setString(2, user.getName());
				ps.setString(3, user.getPassword());
				return ps;
			}
		});
	}
	
	
	public User get(String id) throws SQLException {
		Connection c = dataSource.getConnection();
		
		PreparedStatement ps = c.prepareStatement(
				"select * from users where id = ?");
		ps.setString(1, id);
		
		// 4. SQL 실행 결과를 ResultSet으로 받아 정보를 저장할 Object에 옮긴다.
		ResultSet rs = ps.executeQuery();
		User user = null;
		if(rs.next()) {
			user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
		}
		rs.close();
		ps.close();
		c.close();
		// 결과가 없으면 예외를 던지게 한다.
		if(user == null)
			throw new EmptyResultDataAccessException(1);
		
		return user;
	}
	
	// 클라이언트 책임을 담당
	public void deleteAll() throws SQLException{
		jdbcContext.workWithStatementStrategy(new StatementStrategy() {
			@Override
			public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
				return c.prepareStatement("delete from users");
			}
		});
	}
	
	public int getCount() throws SQLException {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
		c = dataSource.getConnection();
		
		ps = c.prepareStatement("select count(*) from users");
		rs = ps.executeQuery();
		
		rs.next();
		return rs.getInt(1);
		}catch (SQLException e) {
			throw e;
		}finally {
			if(rs != null) {
				try {
					rs.close();
				}catch (SQLException e) {
				}
			}
			if(ps != null) {
				try {
					ps.close();
				}catch (SQLException e) {
				}
			}
			if(c != null) {
				try {
					c.close();
				}catch (SQLException e) {
				}
			}
		}
	}
}