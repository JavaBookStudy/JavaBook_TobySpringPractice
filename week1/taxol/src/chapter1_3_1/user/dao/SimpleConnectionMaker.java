package chapter1_3_1.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * list 1.7
 * 독립적인 클래스로 만들어, 관심사를 분리시켜 보았다.
 * DB 연결 방법에 관한 관심사를 별도의 새로운 클래스로 관심사로 분리함
*/
public class SimpleConnectionMaker {
	public Connection makeNewConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/tobyspring", "ssafy", "ssafy");
		return c;
	}
}
