package com.example.legia.mobileweb.Database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static Connection connect(){
        Connection db = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://192.168.1.67:3306/hthong_muaban?useUnicode=true&characterEncoding=UTF-8";
            db = DriverManager.getConnection(url, "root", "");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return db;
    }
}
