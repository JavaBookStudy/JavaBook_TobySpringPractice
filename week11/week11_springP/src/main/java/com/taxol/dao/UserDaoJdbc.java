package com.taxol.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.taxol.DuplicateUserIdException;
import com.taxol.domain.Level;
import com.taxol.domain.User;
import com.taxol.domain.sqlservice.SqlService;

public class UserDaoJdbc implements UserDao{
private JdbcTemplate jdbcTemplate;	// 스프링이 제공하는 JDBC 코드용 기본 템플릿
	// xml 방식의 sql 값 DI
//	private String sqlAdd;
//	
//	public void setSqlAdd(String sqlAdd) {
//		this.sqlAdd = sqlAdd;
//	}
//	// Map타입의 SQL 인젝션
//	private Map<String, String> sqlMap;
//	
//	public void setSqlMap(Map<String, String> sqlMap) {
//		this.sqlMap = sqlMap;
//	}
	
	// SqlService 프로퍼티 사용
	private SqlService sqlService;
	
	public void setSqlService(SqlService sqlService) {
		this.sqlService = sqlService;
	}
	
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public void add(final User user) throws DuplicateUserIdException {
		try { 
			jdbcTemplate.update(this.sqlService.getSql("userAdd"), 
					user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail());
		} catch (DuplicateKeyException  e) {
			throw new DuplicateUserIdException(e);
		}
	}
	
	
	public User get(String id) {
		// xml의 Map을 통해 sql을 가져왔다.
		return jdbcTemplate.queryForObject(this.sqlService.getSql("userGet"), new Object[] {id} , userMapper);
	}
	
	// jdbcTemplate의 내장 콜백 메서드
	public void deleteAll(){
//		this.jdbcTemplate.update(new PreparedStatementCreator() {
//			@Override
//			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//				return con.prepareStatement("delete from users");
//			}
//		});
		
		this.jdbcTemplate.update(sqlService.getSql("userDeleteAll"));
	}

	public int getCount(){
		// 쿼리문 만 매개변수로 넣으면, jdbc, preparedstatement, resultSet 다 해결해 준다.
		return this.jdbcTemplate.queryForInt(sqlService.getSql("userGetCount"));
	}
	
	public List<User> getAll() {
		return this.jdbcTemplate.query(sqlService.getSql("userGetAll"), userMapper);
	}
	
	private RowMapper<User> userMapper = new RowMapper<User>() {

		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			user.setLevel(Level.valueOf(rs.getInt("Level")));
			user.setLogin(rs.getInt("Login"));
			user.setRecommend(rs.getInt("Recommend"));
			user.setEmail(rs.getString("email"));
			return user;
		}
	};

	@Override
	public void update(User user) {
		this.jdbcTemplate.update(this.sqlService.getSql("userUpdate") , 
				user.getName(), user.getPassword(),	user.getLevel().intValue(), user.getLogin(), user.getRecommend(),
				user.getId());
	}

}
