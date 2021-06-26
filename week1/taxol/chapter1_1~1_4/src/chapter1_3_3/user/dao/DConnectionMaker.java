package chapter1_3_3.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * list 1.9
 * ConnectorMaker 구현 클래스
*/
public class DConnectionMaker implements ConnectionMaker{

	@Override
	public Connection makeConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/tobyspring", "ssafy", "ssafy");
		return c;
	}
}
