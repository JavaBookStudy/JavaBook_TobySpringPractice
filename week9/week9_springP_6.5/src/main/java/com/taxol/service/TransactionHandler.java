package com.taxol.service;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author taxol1203@gmail.com
 */
public class TransactionHandler implements InvocationHandler {
	private Object target;	// 타깃 오브젝트
	private PlatformTransactionManager transactionManager; // 트랜잭션 부가기능 제공
	private String pattern;	// 트랜잭션 적용할 메서드들을 구별할 이름 패턴

	public void setTarget(Object target) {
		this.target = target;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (method.getName().startsWith(pattern)) {		// 특정 이름을 가진 메서드면, 트랜잭션 경계설정 기능 부여
			return invokeInTransaction(method, args);
		}
		return method.invoke(target, args);
	}

	private Object invokeInTransaction(Method method, Object[] args) throws Throwable {
		TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

		try {
			Object returnValue = method.invoke(target, args);	// 타겟 오브젝트의 메서드를 호출.
			this.transactionManager.commit(status);				// 예외 없으면, 커밋
			return returnValue;
		} catch (InvocationTargetException e) {					// 예외 발생 시, 롤백 -> RuntimeException이 아닌 InvocationTarget이다.(예외 포장)
			this.transactionManager.rollback(status);
			throw e.getTargetException();
		}
	}
}
