package com.taxol;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
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
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import com.taxol.DuplicateUserIdException;
import com.taxol.dao.UserDao;
import com.taxol.domain.Level;
import com.taxol.domain.User;
import com.taxol.service.UserService;

/*
 * list 2-18 : 스프링 테스트 컨텍스트를 적용한 UserDaoTest
 * list 2-20 : 테스트를 위한 수동 DI 적용
 * list 5-5  : 수정 된 User 정보에 따라 변경 
*/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")	// 자동으로 만들어줄 ApplicationContext 위치 지정
//@DirtiesContext
public class UserDaoTest {
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private UserDao dao;
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	private User user1;
	private User user2;
	private User user3;
	
	@Before
	public void setUp() {
		
		// 
		//DataSource dataSource = new SingleConnectionDataSource("jdbc:mysql://localhost:3306/tobyspring?useSSL=false", "ssafy", "ssafy", true);
		//dao.setDataSource(dataSource);
		
		user1 = new User("mylover", "park", "springno1", Level.BASIC, 1, 0);
		user2 = new User("taxol", "han", "springno2", Level.SILVER, 55, 10);
		user3 = new User("opink", "park", "springno3", Level.GOLD, 100, 40);
		
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
		checkSameUser(userget1, user1);
		
		// 두 번째 User에 대해서도 같은 방법으로 검증한다.
		User userget2 = dao.get(user2.getId());
		checkSameUser(userget2, user2);
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
		System.out.println(users1.get(0).getLevel());
		System.out.println(user1.getLevel());
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
		assertThat(user1.getLevel(), is(user2.getLevel()));
		assertThat(user1.getRecommend(), is(user2.getRecommend()));
		assertThat(user1.getLogin(), is(user2.getLogin()));
	}
	
	@Test(expected = DuplicateUserIdException.class)
	public void duplicatedKey() {
		dao.deleteAll();
		
		dao.add(user1);
		dao.add(user1);
	}
	
	@Test
	public void sqlExceptionTranslate() {
//		dao.deleteAll();
//		
//		try {
//			dao.add(user1);
//			dao.add(user1);
//		}catch (DuplicateUserIdException ex) {
//			SQLException sqlEx = (SQLException) ex.getCause();
//			SQLExceptionTranslator set =  // 코드를 이용한 SQLException 전환
//					new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
//			assertEquals(set.translate(null, null, sqlEx), DuplicateUserIdException.class);
//		}
	}
	
	@Test
	public void update() {
		dao.deleteAll();
		
		dao.add(user1);			// 수정 되는 사용자
		dao.add(user2);			// 수정 되면 안되는 사용자
		
		user1.setName("오민규");
		user1.setPassword("spring32");
		user1.setLevel(Level.GOLD);
		user1.setLogin(100);
		user1.setRecommend(999);
		dao.update(user1);
		
		User user1update = dao.get(user1.getId());
		checkSameUser(user1, user1update);
		
		User user2update = dao.get(user2.getId());
		checkSameUser(user2, user2update);	// 만약 여기서 틀렸다면, 원하지 않은 row도 수정 된 것이다.
	}


}
