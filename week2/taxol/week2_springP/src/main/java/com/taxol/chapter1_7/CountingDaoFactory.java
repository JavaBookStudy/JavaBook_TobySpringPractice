package com.taxol.chapter1_7;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * list 1.31
 * CountingConnectionMaker 의존관계가 추가된 DI 설정용 클래스
 * 이로써 UserDao -> CountingConnectionMaker -> DConnectionMaker의 관계가 성립되었다.
*/
@Configuration
public class CountingDaoFactory {
	@Bean
	public UserDao userDao() {
		return new UserDao(connectionMaker());
	}
	
	@Bean
	public ConnectionMaker connectionMaker() {
		return new CountingConnectionMaker(realConnectionMaker());
	}
	
	@Bean
	public ConnectionMaker realConnectionMaker() {
		return new DConnectionMaker();
	}
}
