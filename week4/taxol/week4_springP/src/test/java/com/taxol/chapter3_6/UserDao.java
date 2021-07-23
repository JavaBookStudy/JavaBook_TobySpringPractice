package com.taxol.chapter3_6;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

/*
 * list 1.42 : DataSource를 사용하는 UserDao
 * list 2.7  : deleteAll & getCount가 추가되었다.
 * list 2-14 : get() 메소드에서 데이터를 찾지 못하면 예외를 발생 시키도록 수정하였다. 
 * list 3-3  : JDBC 예외처리가 발생해도 resource를 반환하도록 만들었다.
 * list 3-7  : 템플릿 메서드 패턴 적용 -> 추상 클래스로 변경하였다가 전략 패턴을 사용하면서 다시 일반 클래스로 변환함
 * list 3-10 : 전략 패턴에 따라 deleteAll() 메서드에서 변하는 부분을 다른 오브젝트로 분리하였다가, interface를 통해 연결한다.
 * list 3-11 : DI 적용을 위해 클라이언트/컨텍스트 분리하였다.
 * list 3-12 : add() 메서드도 PreparedStatement 생성 로직을 분리하였다.
 * list 3-17 : add() 메서드에서 로컬 변수를 직접 사용하도록 수정
 * list 3-18 : deleteAll(), add() 메서드를 익명 클래스를 통해 사용하도록 수정
 * list 3-25 : 수동 DI를 적용하여, JdbcContext를 DAO의 인스턴스로 생성하여 사용한다.  
 * list 3-27 : deleteAll() 메서드에서 변하지 않는 부분을 따로 executeSql로 분리시켰다.
 * chapter 3-6 : jdbcTemplate 사용
 * list 3-53 : getAll() 메서드 추가
 * list 3-56 : rowMapper 메서드(컨텍스트) 분리
*/
public class UserDao {
	
	private JdbcTemplate jdbcTemplate;	// 스프링이 제공하는 JDBC 코드용 기본 템플릿
	
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public void add(final User user) throws SQLException {
		jdbcTemplate.update("insert into users(id, name, password) values(?,?,?)", user.getId(), user.getName(), user.getPassword());
	}
	
	
	public User get(String id) throws SQLException {
		
		return jdbcTemplate.queryForObject("select * from users where id = ?", new Object[] {id} , userMapper);
	}
	
	// jdbcTemplate의 내장 콜백 메서드
	public void deleteAll() throws SQLException{
//		this.jdbcTemplate.update(new PreparedStatementCreator() {
//			@Override
//			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//				return con.prepareStatement("delete from users");
//			}
//		});
		
		this.jdbcTemplate.update("delete from users");
	}

	public int getCount() throws SQLException {
//		return jdbcTemplate.query(new PreparedStatementCreator() {
//			@Override
//			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//				return con.prepareStatement("select count(*) from users");
//			}
//		}, new ResultSetExtractor<Integer>() {@Override
//			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
//				rs.next();
//				return rs.getInt(1);
//			}
//		});
		// 쿼리문 만 매개변수로 넣으면, jdbc, preparedstatement, resultSet 다 해결해 준다.
		return this.jdbcTemplate.queryForInt("select count(*) from users");
	}
	
	public List<User> getAll() {
		return this.jdbcTemplate.query("select * from users order by id", userMapper);
	}
	
	private RowMapper<User> userMapper = new RowMapper<User>() {

		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			return user;
		}
	};
}