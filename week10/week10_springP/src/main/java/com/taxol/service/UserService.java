package com.taxol.service;

import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.taxol.dao.UserDao;
import com.taxol.domain.Level;
import com.taxol.domain.User;

public interface UserService {
	void add(User user);
	
	// 새로 추가된 메서드
	void deleteAll();
    void update(User user);
    
    @Transactional(readOnly=true) // 메소드 단위로 부여된 트랜잭션 속성이 타입 레벨에 부여된 것보다 우선한다.
    User get(String id);

    @Transactional(readOnly=true)
    List<User> getAll();
    // 새로 추가된 메서드 끝
    
    void upgradeLevels();
}
