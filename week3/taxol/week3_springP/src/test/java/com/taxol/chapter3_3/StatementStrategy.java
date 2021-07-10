package com.taxol.chapter3_3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementStrategy {
	PreparedStatement makePreparedStatement(Connection c) throws SQLException;
}
