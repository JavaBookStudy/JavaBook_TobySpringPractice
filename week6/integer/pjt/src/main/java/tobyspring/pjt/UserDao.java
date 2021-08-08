package tobyspring.pjt;/*
 * 1.1.2
 * 사용자 정보를 DB에 넣고 관리할 수 있는 DAO 클래스
 */

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import tobyspring.pjt.domain.Level;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class UserDao {

	private DataSource dataSource;

	private final JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private RowMapper<User> userMapper =
			new RowMapper<User>() {
				public User mapRow(ResultSet rs, int rowNum) throws SQLException {
					User user = new User();
					user.setId(rs.getString("id"));
					user.setName(rs.getString("name"));
					user.setLogin(rs.getInt("login"));
					user.setLevel(Level.valueOf(rs.getInt("level")));
					user.setPassword(rs.getString("password"));
					user.setRecommend(rs.getInt("recommend"));

					return user;
				}
		};

	public void add(User user){
		this.jdbcTemplate.update(
				"insert into users(id, name, password, level, login, recommend " +
						"values(?,?,?,?,?,?)",
				user.getId(), user.getName(), user.getPassword(),
				user.getLevel(), user.getLogin(), user.getRecommend());
	}

	
	public User get(String id) throws ClassNotFoundException, SQLException {

		List<User> result = this.jdbcTemplate.query("select from users where id = ?", userMapper, id);

		if(result.isEmpty())
			return null;

		return result.get(0);
	}

	public  void update(User user){
		this.jdbcTemplate.update(
				"update users set name=?, password-?, level=?, login=?, recommend=?  where id=? ",
				user.getName(), user.getPassword(),
				user.getLevel(), user.getLogin(), user.getRecommend(),  user.getId());
	}

	public void deleteAll() {

		this.jdbcTemplate.update("delete from users");
	}

	// 메서드 추출 기법 (extract method)
	// 구현 코드는 제거되고 추상 메서드로 바뀌었다. 메서드의 구현은 서브클래스가 담당.
	// 템플릿 메서드 패턴: super class에 기본적인 로직의 흐름을 만들고, 그 기능의 일부를 sub class에서 필요에 맞게 구현해서 사용하는 방법
	// 팩터리 메서드 패턴: 서브클래스에서 구체적인 오브젝트 생성 방법을 결정하게 함
	// public abstract Connection getConnection() throws ClassNotFoundException, SQLException;

}
