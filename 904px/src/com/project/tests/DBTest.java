package com.project.tests;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.project.database.DBConnection;

public class DBTest {

	@Test
	public void testDBConnection() {
		try {
			Connection connection = DBConnection.getInstance().getConnection();
		} catch (ClassNotFoundException e) {
			System.out.println("class problem");
			return;
		} catch (SQLException e) {
			System.out.println("sql problem");
		}
	}

}
