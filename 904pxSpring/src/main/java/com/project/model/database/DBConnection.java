package com.project.model.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public final class DBConnection {
	// private static final String DB_HOST = "localhost";
	// private static final String DB_USER = "root";
	// private static final String DB_PASS = "MySqlRoot123";
	// private static final String DB_PORT = "3306";
	// private static final String DB_SCHEMA = "904pxdb";
	// private static final String DB_OPTIONS =
	// "autoReconnect=true&useSSL=false";

	private static String DB_HOST;
	private static String DB_USER;
	private static String DB_PASS;
	private static String DB_PORT;
	private static String DB_SCHEMA;
	private static String DB_OPTIONS;

	private static final String[] dbProperties = { DB_HOST, DB_USER, DB_PASS, DB_PORT, DB_SCHEMA, DB_OPTIONS };

	private final Connection connection;
	private static DBConnection instance;

	private DBConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");

		loadConnectionProperties();

		DB_HOST = dbProperties[0];
		DB_USER = dbProperties[1];
		DB_PASS = dbProperties[2];
		DB_PORT = dbProperties[3];
		DB_SCHEMA = dbProperties[4];
		DB_OPTIONS = dbProperties[5];

		connection = DriverManager.getConnection(
				String.format("jdbc:mysql://%s:%s/%s?%s", DB_HOST, DB_PORT, DB_SCHEMA, DB_OPTIONS), DB_USER, DB_PASS);

	}

	private void loadConnectionProperties() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("dbConnectionProperties").getFile());
		if (!file.exists()) {
			try {
				file.createNewFile();
				PrintWriter wt = new PrintWriter(file);
				wt.println("DB_HOST:localhost");
				wt.println("DB_USER:root");
				wt.println("DB_PASS:h3qb575fufgdb");
				wt.println("DB_PORT:3306");
				wt.println("DB_SCHEMA:904pxdb");
				wt.println("DB_OPTIONS:autoReconnect=true&useSSL=false");
			} catch (IOException e) {
				System.out.println("Something went wrong");
				return;
			}
		}
		System.out.println("The file exists: " + file.exists());
		try {
			Scanner sc = new Scanner(file);
			int index = 0;
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				dbProperties[index++] = line.split(":")[1];
			}
		} catch (FileNotFoundException e) {
			System.out.println("File does not exist");
			return;
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public static DBConnection getInstance() throws ClassNotFoundException, SQLException {
		synchronized (DBConnection.class) {
			if (instance == null) {
				instance = new DBConnection();
			}
		}
		return instance;
	}

}
