package com.netsurfingzone.config;



import java.sql.*;

public class ConnectingToDB {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        //Loading the required JDBC Driver class
        //Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");

        //Creating a connection to the database
        Connection conn = DriverManager.getConnection("jdbc:mysql://115.110.90.100:1433/prjml","prjaiml","Tataetr@1424");

        //Executing SQL query and fetching the result
        Statement st = conn.createStatement();
        String sqlStr = "select * from testTable";
        ResultSet rs = st.executeQuery(sqlStr);
        while (rs.next()) {
            System.out.println(rs.getString("name"));
        }
    }
}
