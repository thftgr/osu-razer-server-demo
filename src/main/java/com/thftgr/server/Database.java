package com.thftgr.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.*;

public class Database {
    private Connection connection;
    private Statement statement;

    public boolean connectDatabase(JsonObject connectionData){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(
                    connectionData.get("Url").getAsString(),
                    connectionData.get("username").getAsString(),
                    connectionData.get("password").getAsString()
            );

            statement = connection.createStatement();
        } catch (ClassNotFoundException e) {
            System.out.println("ConnectDatabase error : Mysql class load error : \n"+e.getMessage());
            return false;
        } catch (SQLException e) {
            System.out.println("ConnectDatabase error : SQL Exception : \n" + e.getMessage());
            return false;
        }catch (Exception e){
            System.out.println("ConnectDatabase error : \n" + e.getMessage());
            return false;
        }
        return true;

    }

    public boolean closeDatabaseConnection(){
        try {
            statement.cancel();
            statement.close();
            connection.close();
        } catch (Exception e) {
            System.out.println("closeDatabaseConnection error : \n"+e.getMessage());
            return false;
        }
        return true;


    }

    public JsonArray query(String query) {


        JsonArray beatMapJsonArray = new JsonArray();

        try {
            ResultSet rs = statement.executeQuery(query);

            int columnCount = rs.getMetaData().getColumnCount();

            String ColumnName;
            JsonObject beatMapJsonObject;

            while (rs.next()) {
                beatMapJsonObject = new JsonObject();


                for (int j = 1; j < columnCount + 1; j++) {
                    ColumnName = rs.getMetaData().getColumnName(j);
                    System.out.println(ColumnName);
                    beatMapJsonObject.addProperty(ColumnName, rs.getString(ColumnName));
                }
                beatMapJsonArray.add(beatMapJsonObject);
            }
            rs.close();
            rs.close();


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return beatMapJsonArray;
    }


}
