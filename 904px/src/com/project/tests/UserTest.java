package com.project.tests;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

import com.project.database.DBConnection;
import com.project.exceptions.UserException;
import com.project.user.User;
import com.project.user.UserDAO;

public class UserTest {

//	@Test(expected=UserException.class)
//	public void testRegisterUser() {
//		String username = "Robert";
//		String password = "123";
//		String email = "robert@gmail.com";
//		
//		UserDAO dao = UserDAO.getInstance();
//		dao.register(username, password, email);
//		
//		Connection con = null;
//		try {
//			con = DBConnection.getInstance().getConnection();
//			Statement st = con.createStatement();
//			ResultSet set = st.executeQuery("select username, password, email from users;");
//			
//			while (set.next()) {
//				System.out.printf("%s, %s, %s%n", set.getString(1), set.getString(2), set.getString(3));
//			}
//
//			for (User user : dao.getUsers()) {
//				System.out.println(user);
//			}
//			
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			try {
//				con.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//	}
	
	@Test(expected=UserException.class)
	public void testLoginUser() {
		UserDAO dao = UserDAO.getInstance();
		
		try {
			System.out.println(dao.login("paula", "1234"));
			
		} catch (UserException e) {
			return;
		}
	}

}
