package com.project.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {
	private static final String DB_HOST = "localhost";
	private static final String DB_USER = "root";
	private static final String DB_PASS = "h3qb575fufgdb";
	private static final String DB_PORT = "3306";
	private static final String DB_SCHEMA = "904pxdb";
	private static final String DB_OPTIONS = "autoReconnect=true&useSSL=false";

	private final Connection connection;
	private static DBConnection instance;
	
	
	private DBConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		 
		connection = DriverManager
				.getConnection
				(String.format("jdbc:mysql://%s:%s/%s?%s", 
						DB_HOST, 
						DB_PORT, 
						DB_SCHEMA, 
						DB_OPTIONS), DB_USER, DB_PASS);
		
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public static synchronized DBConnection getInstance() throws ClassNotFoundException, SQLException {
		if (instance == null) {
			instance = new DBConnection();
		}
		return instance;
	}
}
