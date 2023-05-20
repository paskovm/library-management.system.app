package org.example;

import java.sql.*;

/**
 * Hello world!
 */
public class App {

    private static final String HOST = "jdbc:mysql://127.0.0.1:3306/lms";
    private static final String USERNAME = "devuser";
    private static final String PASSWORD = "12345";

    public static void main(String[] args) throws SQLException {
        System.out.println("Hello World!");

        Connection connection = DriverManager.getConnection(HOST, USERNAME, PASSWORD);
        Statement statement = connection.createStatement();

        String sql = "select * from lms.person";
        ResultSet result = statement.executeQuery(sql);

        while (result.next()) {
            System.out.println(result.getString("FirstName") + " " + result.getString("LastName"));
        }
    }
}
