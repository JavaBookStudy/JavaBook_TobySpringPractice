package com.taxol.chapter4_2;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class UserDaoJdbc implements UserDao{
private JdbcTemplate jdbcTemplate;	// 스프링이 제공하는 JDBC 코드용 기본 템플릿
	
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public void add(final User user) throws DuplicateUserIdException {
		try {
			jdbcTemplate.update("insert into users(id, name, password) values(?,?,?)", 
					user.getId(), user.getName(), user.getPassword());
		} catch (DuplicateKeyException  e) {
			throw new DuplicateUserIdException(e);
		}
	}
	
	
	public User get(String id) {
		
		return jdbcTemplate.queryForObject("select * from users where id = ?", new Object[] {id} , userMapper);
	}
	
	// jdbcTemplate의 내장 콜백 메서드
	public void deleteAll(){
//		this.jdbcTemplate.update(new PreparedStatementCreator() {
//			@Override
//			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//				return con.prepareStatement("delete from users");
//			}
//		});
		
		this.jdbcTemplate.update("delete from users");
	}

	public int getCount(){
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
