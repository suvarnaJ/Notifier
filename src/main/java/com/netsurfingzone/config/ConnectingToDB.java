package com.netsurfingzone.config;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectingToDB {

    public List<Map<String, Object>> Execute(String sqlStr) throws SQLException {
        Connection developmentConnection = DriverManager.getConnection("jdbc:sqlserver://115.110.90.100:1433;databaseName=prjml","prjaiml","Tataetr@1424");
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