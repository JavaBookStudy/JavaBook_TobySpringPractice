package com.taxol.chapter2_4;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/*
 * list 2-23
 * 애플리케이션 컨텍스트가 없는 DI 테스트
*/

public class UserDaoTest_notAppContext {
	
	private UserDao dao;
	
	private User user1;
	private User user2;
	private User user3;
	
	@Before
	public void setUp() {
		
		dao = new UserDao();
		
		DataSource dataSource = new SingleConnectionDataSource("jdbc:mysql://localhost:3306/tobyspring?useSSL=false", "ssafy", "ssafy", true);
		dao.setDataSource(dataSource);

		user1 = new User("mylover", "park", "springno1");
		user2 = new User("taxol", "han", "springno2");
		user3 = new User("opink", "park", "springno3");
	}
	
	@Test
	public void addAndGet() throws SQLException{
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		
		// 첫 번째 User의 id로 get()을 실행하면 첫 번째 User의 값을 가진 오브젝트를 돌려주는지 확인한다.
		User userget1 = dao.get(user1.getId());
		assertThat(userget1.getName(), is(user1.getName()));
		assertThat(userget1.getPassword(), is(user1.getPassword()));
		
		// 두 번째 User에 대해서도 같은 방법으로 검증한다.
		User userget2 = dao.get(user2.getId());
		assertThat(userget2.getName(), is(user2.getName()));
		assertThat(userget2.getPassword(), is(user2.getPassword()));
	}
	
	@Test
	public void count() throws SQLException {
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.add(user1);
		assertThat(dao.getCount(), is(1));
		
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		
		dao.add(user3);
		assertThat(dao.getCount(), is(3));
	}
	
	// 주어진 id가 없으면 EmptyResultDataAccessException 와 같은 예외가 떨어지게 하는 것
	@Test(expected = EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException{
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.get("unknomn_id");
	}
}
