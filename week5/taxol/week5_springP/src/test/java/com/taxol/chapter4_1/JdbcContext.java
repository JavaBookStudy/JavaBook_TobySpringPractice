package com.taxol.chapter4_1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

/*
 * list 3-21 : JDBC 작업 흐름을 분리하여 만든 JdbcContext 클래스
 * list 3-28 : executeSql() 메서드를 JdbcContext로 옮겨, 모든 DAO에서 사용 가능하도록 만들었다.
*/
public class JdbcContext {
	
	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException{
		Connection c = null;
		PreparedStatement ps = null;
		
		try {
			c = dataSource.getConnection();
			//ps = makeStatement(c);
			ps = stmt.makePreparedStatement(c);		// 이와 같이 DI로 넣어준다.
			
			ps.executeUpdate();
		}catch (SQLException e) {
			throw e;
		}finally {
			if(ps != null) {
				try {
					ps.close();
				}catch (SQLException e) {
				}
			}
			if(c != null) {
				try {
					c.close();
				}catch (SQLException e) {
				}
			}
		}
	}
	
	public void executeSql(String query) throws SQLException {
		workWithStatementStrategy(new StatementStrategy() {
			@Override
			public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
				return c.prepareStatement(query);
			}
		});
	}
}
