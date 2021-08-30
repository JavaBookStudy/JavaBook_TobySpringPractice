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
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.taxol.dao.UserDao;
import com.taxol.domain.Level;
import com.taxol.domain.User;

public interface UserService {
	void add(User user);
	void upgradeLevels();
}
