package week2.integer;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionMaker {

	/**
	 * 커넥션 반환 코드
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public Connection makeConnection() throws ClassNotFoundException, SQLException;
}
