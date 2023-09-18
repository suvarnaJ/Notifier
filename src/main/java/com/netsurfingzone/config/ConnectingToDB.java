package com.netsurfingzone.config;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectingToDB {

    public List<Map<String, Object>> Execute(String sqlStr) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        String serverName = "115.110.90.100";
        String dbName = "prjml";
        String  url = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true";
        Connection developmentConnection = DriverManager.getConnection(url,"prjaiml","Tataetr@1424");
        Statement statement = developmentConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlStr);
        List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
        while (resultSet.next()) {
            Map<String,Object> map = new HashMap<String,Object>();
            for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                map.put(resultSet.getMetaData().getColumnName(i+1),resultSet.getObject(i+1));
            }
            rows.add(map);
        }
        return rows;
    }


}
