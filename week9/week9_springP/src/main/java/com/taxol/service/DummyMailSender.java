package com.taxol.service;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

// 실제 simpleMessage로 부터 메일을 받아 전송하는 것이 아닌 dummy 전송 객체
public class DummyMailSender implements MailSender {
	@Override
	public void send(SimpleMailMessage simpleMessage) throws MailException {
	}

	@Override
	public void send(SimpleMailMessage[] simpleMessages) throws MailException {
	}
}