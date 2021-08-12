package com.taxol.chapter4_1;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.List;

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
 * list 2-18 : 스프링 테스트 컨텍스트를 적용한 UserDaoTest
 * list 2-20 : 테스트를 위한 수동 DI 적용
*/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")	// 자동으로 만들어줄 ApplicationContext 위치 지정
//@DirtiesContext
public class UserDaoTest {
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private UserDao dao;
	
	private User user1;
	private User user2;
	private User user3;
	
	@Before
	public void setUp() {
		
		// 
		//DataSource dataSource = new SingleConnectionDataSource("jdbc:mysql://localhost:3306/tobyspring?useSSL=false", "ssafy", "ssafy", true);
		//dao.setDataSource(dataSource);
		
		user1 = new User("mylover", "park", "springno1");
		user2 = new User("taxol", "han", "springno2");
		user3 = new User("opink", "park", "springno3");
		
		System.out.println(this.context);
		System.out.println(this);
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
	
	// getAll() 테스트
	@Test
	public void getAll() throws Exception {
		dao.deleteAll();
		
		// 만약 현재 db에 아무것도 없을 때
		List<User> users0 = dao.getAll();
		assertThat(users0.size(), is(0));
		
		dao.add(user1); // Id: mylover
		List<User> users1 = dao.getAll();
		assertThat(users1.size(), is(1));
		checkSameUser(user1, users1.get(0));
		
		dao.add(user2); // Id: taxol
		List<User> users2 = dao.getAll();
		assertThat(users2.size(), is(2));	
		checkSameUser(user1, users1.get(0));
		checkSameUser(user2, users2.get(1));
		
		dao.add(user3); // id: opink;
		List<User> users3 = dao.getAll();
		assertThat(users3.size(), is(3));	
		checkSameUser(user1, users1.get(0));
		checkSameUser(user2, users2.get(1));
		checkSameUser(user2, users3.get(2));
		
		users3.forEach(System.out::println);
	}
	
	// User 오브젝트의 내용을 비교하는 검증코드 테스트에서 반복적으로 사용되므로 분리해놓았다.
		private void checkSameUser(User user1, User user2) {
			assertThat(user1.getId(), is(user2.getId()));
			assertThat(user1.getName(), is(user2.getName()));
			assertThat(user1.getPassword(), is(user2.getPassword()));
		}
}
