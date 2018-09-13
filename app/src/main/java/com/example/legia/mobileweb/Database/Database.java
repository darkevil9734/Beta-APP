package com.example.legia.mobileweb.Database;
import java.sql.Connection; // import thư viện JDBC.
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    // khai báo phương thức kết nối csdl
    public static Connection connect(){
        Connection db = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://172.16.216.189:3306/hthong_muaban?useUnicode=true&characterEncoding=UTF-8";
            db = DriverManager.getConnection(url, "root", "");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return db;
    }
}
