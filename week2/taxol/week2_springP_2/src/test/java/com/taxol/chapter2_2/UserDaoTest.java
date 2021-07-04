package com.taxol.chapter2_2;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

/*
 * list 2.5
 * JUnit을 적용한 UserDaoTest
 * 
 * list 2.9
 * deleteAll()과 getCount()를 추가하여, 매번 데이터를 수정할 필요 없이 삭제하였다가 테스트한다.
 * 
 * list 2-11
 * 여러개의 User를 등록해보면서 getCount를 테스트한다.
 * 
 * list 2-13
 * get() 메소드의 id가 없는 경우 대비한 예외상황 테스트
*/
public class UserDaoTest {
	@Test
	public void addAndGet() throws SQLException{
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		UserDao dao = context.getBean("userDao", UserDao.class);
		
		User user1 = new User("gyumee", "park", "spring01");
		User user2 = new User("taxol", "han", "spring02");
		
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
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		UserDao dao = context.getBean("userDao", UserDao.class);
		
		User user1 = new User("mylover", "park", "springno1");
		User user2 = new User("taxol", "han", "springno2");
		User user3 = new User("opink", "park", "springno3");
		
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
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		UserDao dao = context.getBean("userDao" , UserDao.class);
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.get("unknomn_id");
	}
}
