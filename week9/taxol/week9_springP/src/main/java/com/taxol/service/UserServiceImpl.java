package com.taxol.service;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;

import com.taxol.dao.UserDao;
import com.taxol.domain.Level;
import com.taxol.domain.User;

public class UserServiceImpl implements UserService{
	private static final int MIN_LOGCOUNT_FOR_SIVER = 50;
	private static final int MIN_RECOMMEND_FOR_GOLD = 30;
	private UserDao userDao;
	private MailSender mailSender;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
	// 6-2 비즈니스 로직과 트랜잭션 경계설정 분리
	public void upgradeLevels() {
		List<User> users = userDao.getAll();
		for (User user : users) {
			if (canUpgradeLevel(user)) {
				upgradeLevel(user);
			}
		}
	}

	// 5-24 업그레이드 기능 확인 메소드
	private boolean canUpgradeLevel(User user) {
		Level currentLevel = user.getLevel();
		// 레벨별로 구분해서 조건을 판단한다.
		switch(currentLevel) {
			case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SIVER);
			case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
			case GOLD: return false;
			// 현재 로직에서 다룰 수 없는 레벨이 주어지면 예외를 발생시킨다. 새로운 레벨이 추가되고 로직을 수정하지 않으면 에러가 나서 확인할 수 있다.
			default: throw new IllegalArgumentException("Unknown Level: " + currentLevel);
		}
	}
	
	// 5-25 레벨 업그레이드 작업 메소드
	// 5-28 더 간결해진 upgradeLevel
	protected void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);
		sendUpgradeEMail(user);
	}
	
	// 만약 user의 level이 null이면 default로 BASIC을 넣는다.
	// 서비스 단의 처리이므로, dao에서 처리
	public void add(User user) {
		if(user.getLevel() == null)
			user.setLevel(Level.BASIC);
		userDao.add(user);
	}
	
	private void sendUpgradeEMail(User user) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setFrom("useradmin@ksug.org");
		mailMessage.setSubject("Upgrade 안내");
		mailMessage.setText("사용자님의 등급이 " + user.getLevel().name());

		this.mailSender.send(mailMessage);
	}
}
