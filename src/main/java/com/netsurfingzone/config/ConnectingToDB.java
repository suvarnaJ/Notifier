package com.netsurfingzone.config;
import java.sql.*;

public class ConnectingToDB {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        //Loading the required JDBC Driver class
        //Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");

        //Creating a connection to the database
        Connection connection = DriverManager.getConnection("jdbc:mysql://115.110.90.100:1433/prjml","prjaiml","Tataetr@1424");

        //Executing SQL query and fetching the result
        Statement statement = connection.createStatement();
        String sqlStr = "select * from testTable";
        ResultSet resultSet = statement.executeQuery(sqlStr);
        while (resultSet.next()) {
            System.out.println(resultSet.getString("name"));
        }
    }
}
