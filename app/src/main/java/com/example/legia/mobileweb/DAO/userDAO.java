package com.example.legia.mobileweb.DAO;

import com.example.legia.mobileweb.DTO.User;
import com.example.legia.mobileweb.Database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    public static int dangKy(User thanhVien) {
        int status=0;
        Connection db = Database.connect();
        try {

            PreparedStatement pst = db.prepareStatement("INSERT INTO user(username, password, ho_user, ten_user, email) VALUES(?,?,?,?,?)");

            pst.setString(1, thanhVien.getUsername());
            pst.setString(2, thanhVien.getPassword());
            pst.setString(3, thanhVien.getHo_user());
            pst.setString(4, thanhVien.getTen_user());
            pst.setString(5, thanhVien.getEmail());

            status = pst.executeUpdate();
            db.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return status;
    }

    public static User readUser(int iduser){
        User u = null;
        try {
            Connection db = Database.connect();
            String sql = "SELECT*FROM user WHERE iduser = ?";
            PreparedStatement pst = db.prepareStatement(sql);
            pst.setInt(1, iduser);

            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                u = new User();
                u.setIduser(rs.getInt("iduser"));
                u.setPassword(rs.getString("password"));
                u.setHo_user(rs.getString("ho_user"));
                u.setTen_user(rs.getString("ten_user"));
                u.setSdt(rs.getInt("sdt"));
                u.setEmail(rs.getString("email"));
                u.setDia_chi(rs.getString("dia_chi"));
                u.setQuan(rs.getString("quan"));
                u.setPhuong(rs.getString("phuong"));
                u.setThanh_pho(rs.getString("thanh_pho"));
                u.setNuoc(rs.getString("nuoc"));
                u.setZip_code(rs.getString("zip_code"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return u;
    }

    // reset password
    public static User ResetPassword(String username, String email) {
        User thanhVienQuenMatKhau = null;
        Connection db = Database.connect();
        PreparedStatement pst = null;

        try {
            String sql = "select*from hthong_muaban.user where username = ? and email = ? ";
            pst = db.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, email);
            ResultSet rs = pst.executeQuery();

            while(rs.next()) {
                thanhVienQuenMatKhau = new User();
                thanhVienQuenMatKhau.setIduser(rs.getInt("iduser"));
                thanhVienQuenMatKhau.setPassword(rs.getString("password"));
                thanhVienQuenMatKhau.setHo_user(rs.getString("ho_user"));
                thanhVienQuenMatKhau.setTen_user(rs.getString("ten_user"));
                thanhVienQuenMatKhau.setSdt(rs.getInt("sdt"));
                thanhVienQuenMatKhau.setEmail(rs.getString("email"));
                thanhVienQuenMatKhau.setDia_chi(rs.getString("dia_chi"));
                thanhVienQuenMatKhau.setThanh_pho(rs.getString("thanh_pho"));
                thanhVienQuenMatKhau.setNuoc(rs.getString("nuoc"));
                thanhVienQuenMatKhau.setZip_code(rs.getString("zip_code"));
            }
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return thanhVienQuenMatKhau;
    }

    //check user tồn tại
    public static User kiemTraUser(String username, String email) {
        User nd = null;
        Connection db = Database.connect();
        try {
            Statement stm = db.createStatement();
            String sql = "select*from user u where u.username = '"+username+"' and u.email = '"+email+"'";
            ResultSet rs = stm.executeQuery(sql);

            while(rs.next()) {
                nd = new User();
                nd.setIduser(rs.getInt("iduser"));
                nd.setUsername(rs.getString("username"));
                nd.setPassword(rs.getString("password"));
                nd.setHo_user(rs.getString("ho_user"));
                nd.setTen_user(rs.getString("ten_user"));
                nd.setSdt(rs.getInt("sdt"));
                nd.setEmail(rs.getString("email"));
                nd.setDia_chi(rs.getString("dia_chi"));
                nd.setQuan(rs.getString("quan"));
                nd.setPhuong(rs.getString("phuong"));
                nd.setThanh_pho(rs.getString("thanh_pho"));
                nd.setNuoc(rs.getString("nuoc"));
                nd.setZip_code(rs.getString("zip_code"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return nd;
    }
}
