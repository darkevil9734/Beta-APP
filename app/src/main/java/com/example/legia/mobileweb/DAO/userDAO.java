package com.example.legia.mobileweb.DAO;

import com.example.legia.mobileweb.DTO.User;
import com.example.legia.mobileweb.Database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class userDAO {
    public static User Login(String username, String password) {
        User thanhVienDangNhap = null;
        Connection db = Database.connect();
        PreparedStatement pst = null;

        try {
            String sql = "SELECT * FROM user where username = ?  and password = ?";
            pst = db.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();


            if (rs.next()) {
                thanhVienDangNhap = new User();
                thanhVienDangNhap.setIduser(rs.getInt("iduser"));
                thanhVienDangNhap.setPassword(rs.getString("password"));
                thanhVienDangNhap.setHo_user(rs.getString("ho_user"));
                thanhVienDangNhap.setTen_user(rs.getString("ten_user"));
                thanhVienDangNhap.setSdt(rs.getInt("sdt"));
                thanhVienDangNhap.setEmail(rs.getString("email"));
                thanhVienDangNhap.setDia_chi(rs.getString("dia_chi"));
                thanhVienDangNhap.setQuan(rs.getString("quan"));
                thanhVienDangNhap.setPhuong(rs.getString("phuong"));
                thanhVienDangNhap.setThanh_pho(rs.getString("thanh_pho"));
                thanhVienDangNhap.setNuoc(rs.getString("nuoc"));
                thanhVienDangNhap.setZip_code(rs.getString("zip_code"));

            }
            db.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return thanhVienDangNhap;
    }
}
