package com.taxol.chapter1_7;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * list 1.8
 * ConnectionMaker 인터페이스
 * DB 연결 방법에 관한 관심사를 별도의 새로운 클래스로 관심사로 분리함
*/
public interface ConnectionMaker {
	public Connection makeConnection() throws ClassNotFoundException, SQLException;
}
