package com.taxol.proxy;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import com.taxol.dao.UserDao;
import com.taxol.domain.Level;
import com.taxol.domain.User;
import com.taxol.service.DummyMailSender;
import com.taxol.service.MockMailSender;
import com.taxol.service.TransactionHandler;
import com.taxol.service.TxProxyFactoryBean;
import com.taxol.service.UserService;
import com.taxol.service.UserServiceImpl;
import com.taxol.service.UserServiceTx;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationcontext.xml")
public class UserServiceTest_ProxyFactory {
	private static final int MIN_LOGCOUNT_FOR_SIVER = 50;
	private static final int MIN_RECOMMEND_FOR_GOLD = 30;

	@Autowired
	UserService userService;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	UserServiceImpl userServiceImpl;
	
	@Autowired
	ApplicationContext context;
	
	List<User> users; // 테스트 픽스처
	
	@Autowired
	private DataSource dataSource;
	@Autowired
	private PlatformTransactionManager transactionManager;
	@Autowired
	MailSender mailSender;
	@Before
	public void setUp() throws Exception {
		users = Arrays.asList( // 배열을 리스트로 만들어주는 편리한 메소드, 배열을 가변인자로 넣어주면 더욱 편리하다.
			new User("bumjin",   "박범진", "p1", Level.BASIC,  MIN_LOGCOUNT_FOR_SIVER-1, 0, "taxol1203@naver.com"),
			new User("joytouch", "강명성", "p2", Level.BASIC,  MIN_LOGCOUNT_FOR_SIVER, 0, "taxol1203@gmail.com"),
			new User("erwins",   "신승한", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1, "taxol1203@daum.com"),
			new User("madnitel", "이상호", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD, "taxol1203@kakao.com"),
			new User("green",    "오민규", "p5", Level.GOLD,  100, Integer.MAX_VALUE, "taxol1203@hanmail.net")
		);
	}

	// 5-17 userService 빈의 주입을 확인하는 테스트
	@Test
	public void bean() {
		assertNotNull(this.userService);
	}

	// 5-30 개선한 레벨 업그레이드 테스트
	@Test
	@DirtiesContext
	public void upgradeLevels() {
		userDao.deleteAll();
		for(User user : users) userDao.add(user);
		
		// 메일 발송 결과를 테스트할 수 있도록 목 오브젝트를 만들어 userService의 의존 오브젝트로 주입한다.
        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender(mockMailSender);
        
		userService.upgradeLevels();
		
		// 각 사용자별로 업그레이드 후의 예상 레벨을 검증한다.
		checkLevelUpgraded(users.get(0), false);
		checkLevelUpgraded(users.get(1), true);
		checkLevelUpgraded(users.get(2), false);
		checkLevelUpgraded(users.get(3), true);
		checkLevelUpgraded(users.get(4), false);
		
		// 목 오브젝트에 저장된 메일 수신자 목록을 가져와 업그레이드 대상과 일치하는지 확인 한다.
        List<String> request = mockMailSender.getRequests();
        assertThat(request.size(), is(2));
        assertThat(request.get(0), is(users.get(1).getEmail()));
        assertThat(request.get(1), is(users.get(3).getEmail()));
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
	
	// 다이내믹 프록시 팩토리 빈을 직접 만들어 사용할 때는 없앴다가 다시 등장한 컨텍스트 무효화 어노테이션
		// 해당 부분은 setTarget을 통해 원래 빈에 등록된 타겟을 재조정해주고 있고 이에 따라 컨텍스트가 변경되었다는걸
		// 알려주지 않는다면 빈이 싱글톤이기에 다른 테스트에 영향을 미칠수 있기에 @DirtiesContext를 적용해야 한다.
	@Test
	@DirtiesContext
	public void upgradeAllOrNothing() throws Exception {
		// 타겟 오브젝트
		TestUserService testUserService = new TestUserService(users.get(3).getId()); // 예외 발생용 오브젝트 생성
		testUserService.setUserDao(userDao); // TestUserService 객체에 userDao 수동 DI
		testUserService.setMailSender(mailSender); // TestUserService 객체에 mailSender 수동 DI

		// 팩토리 빈 자체를 가져와야 하므로 빈 이름에 & 를 반드시 넣어야 한다.
		// getBean()의 처음 패러미터는 우리가 @Bean으로 설정한 메소드의 이름이며 getBean()의 리턴타입은 Object인데
		// 이를 두번째 패러미터를 이용하여서 캐스팅을 하지 않고 바로 리턴 타입을 설정할 수 있다.
		ProxyFactoryBean txProxyFactoryBean = 
			context.getBean("&userService", ProxyFactoryBean.class); // 테스트용 타깃 주입
		txProxyFactoryBean.setTarget(testUserService);
		// 변경된 타깃 설정을 이용해서 트랜잭션 다이내믹 프록시 오브젝트를 다시 생성한다.
		UserService txUserService = (UserService) txProxyFactoryBean.getObject();

		/*
        기존 트랜잭션을 넣기 위해서 만들었던 데코레이터 패턴을 이용한 프록시이나 다이내믹 프록시에 의해 대체되었다.
        UserServiceTx txUserService = new UserServiceTx();
        txUserService.setTransactionManager(transactionManager);
        txUserService.setUserService(testUserService);
        */
				 
		userDao.deleteAll();			  
		for(User user : users) userDao.add(user);
		
		try {
			txUserService.upgradeLevels();   
			fail("TestUserServiceException expected"); 
		}
		catch(TestUserServiceException e) { 
		}
		
		checkLevelUpgraded(users.get(1), false);
	}
	
	
	static class TestUserService extends UserServiceImpl {
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
