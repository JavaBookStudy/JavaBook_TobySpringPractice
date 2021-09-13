package com.taxol.domain.sqlservice;

// 조회 실패시 예외를 설정해준다.
public class SqlRetrievalFailureException extends RuntimeException {
	public SqlRetrievalFailureException(String message) {
		super(message);
	}
	
	public SqlRetrievalFailureException(String message, Throwable cause) {
		super(message, cause);
	}
}
