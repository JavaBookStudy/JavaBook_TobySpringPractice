package com.taxol.chapter1_8;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * list 1.18
 * UserDao의 생성 책임을 맡은 팩토리 클래스
 * 생성 오브젝트 코드를 수정하여, 구현 클래스를 결정하고 오브젝트를 만드는 코드를 별도의 메소드로 분리하였다.
 * 스프링 빈 펙토리가 사용할 설정 정보를 담은 DaoFactory 클래스
 * 
 * list 1.34
 * 수정자 메서드 DI를 사용하여 펙터리 메서드를 구현하였다.
*/
@Configuration
public class DaoFactory {
	@Bean
	public UserDao userDao() {
		UserDao userDao = new UserDao();
		userDao.setConnectionMaker(connectionMaker());
		return userDao;
	}
	/*
	public AccountDao accountDao() {
		return new AccountDao(connectionMaker());
	}
	public MessageDao messageDao() {
		return new MessageDao(connectionMaker());
	}
	*/
	@Bean
	public ConnectionMaker connectionMaker() {
		return new DConnectionMaker();
	}
}
