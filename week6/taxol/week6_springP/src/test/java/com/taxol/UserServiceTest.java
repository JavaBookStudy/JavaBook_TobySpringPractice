package com.taxol;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import com.taxol.dao.UserDao;
import com.taxol.domain.Level;
import com.taxol.domain.User;
import com.taxol.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationcontext.xml")
public class UserServiceTest {
	private static final int MIN_LOGCOUNT_FOR_SIVER = 50;
	private static final int MIN_RECOMMEND_FOR_GOLD = 30;

	@Autowired
	UserService userService;
	
	@Autowired
	UserDao userDao;
	
	List<User> users; // 테스트 픽스처
	
	@Autowired
	private DataSource dataSource;
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Before
	public void setUp() throws Exception {
		users = Arrays.asList( // 배열을 리스트로 만들어주는 편리한 메소드, 배열을 가변인자로 넣어주면 더욱 편리하다.
			new User("bumjin",   "박범진", "p1", Level.BASIC,  MIN_LOGCOUNT_FOR_SIVER-1, 0),
			new User("joytouch", "강명성", "p2", Level.BASIC,  MIN_LOGCOUNT_FOR_SIVER, 0),
			new User("erwins",   "신승한", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1),
			new User("madnitel", "이상호", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
			new User("green",    "오민규", "p5", Level.GOLD,  100, Integer.MAX_VALUE)
		);
	}

	// 5-17 userService 빈의 주입을 확인하는 테스트
	@Test
	public void bean() {
		assertNotNull(this.userService);
	}

	// 5-30 개선한 레벨 업그레이드 테스트
	@Test
	public void upgradeLevels() {
		userDao.deleteAll();
		for(User user : users) userDao.add(user);
		
		userService.upgradeLevels();
		
		// 각 사용자별로 업그레이드 후의 예상 레벨을 검증한다.
		checkLevelUpgraded(users.get(0), false);
		checkLevelUpgraded(users.get(1), true);
		checkLevelUpgraded(users.get(2), false);
		checkLevelUpgraded(users.get(3), true);
		checkLevelUpgraded(users.get(4), false);
		
	}
	
	// DB에서 사용자 정보를 가져와 레벨을 확인하는 코드가 중복되므로 헬퍼 메소드로 분리했다.
	private void checkLevelUpgraded(User user, boolean upgraded) {
		User userUpdate = userDao.get(user.getId());
		if(upgraded) {
			assertEquals(userUpdate.getLevel(), user.getLevel().nextLevel()); // 업그레이드가 일어났는지 확인
		}
		else { 
			assertEquals(userUpdate.getLevel(), user.getLevel()); // 업그레이드가 일어나지 않았는지 확인
		}
	}
	
	// 5-21 add() 메소드의 테스트
	@Test
	public void add() {
		userDao.deleteAll();
		
		User userWithLevel = users.get(4); // GOLD 레벨
		User userWithoutLevel = users.get(0);
		userWithoutLevel.setLevel(null); // 레벨이 비어 있는 사용자. 로직에 따라 등록 중에 BASIC 레벨도 설정돼야 한다.
		
		userService.add(userWithLevel);
		userService.add(userWithoutLevel);
		
		// DB에 저장된 결과를 가져와 확인한다.
		User userWithLevelRead = userDao.get(userWithLevel.getId());
		User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());
		
		assertEquals(userWithLevelRead.getLevel(), userWithLevel.getLevel());
		assertEquals(userWithoutLevelRead.getLevel(), Level.BASIC);
	}
	
	@Test
	public void upgradeAllOrNothing() throws Exception {
		UserService testUserService = new TestUserService(users.get(3).getId());
		testUserService.setUserDao(userDao);
		testUserService.setDataSource(dataSource);
		testUserService.setTransactionManager(transactionManager);

		userDao.deleteAll();

		for (User user : users) {
			userDao.add(user);
		}

		try {
			testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		} catch (TestUserServiceException e) {
		}

//		checkLevel(users.get(1), false);
	}
	
	
	static class TestUserService extends UserService {
		private String id;

		private TestUserService(String id) {
			this.id = id;
		}

		protected void upgradeLevel(User user) { 
			if (user.getId().equals(this.id)) {
				throw new TestUserServiceException();
			}
			super.upgradeLevel(user);
		}
	}
	static class TestUserServiceException extends RuntimeException {
	}
}
