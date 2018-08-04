package com.example.legia.mobileweb.DAO;

import com.example.legia.mobileweb.DTO.hoaDon;
import com.example.legia.mobileweb.Database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class hoaDonDAO {
    public static int themHoaDon(hoaDon hd) {
        int status =0;
        String sql = "insert into gio_hang(iduser, email, ho_user, ten_user, sdt, diaChi, quan, phuong, chi_tiet) values(?,?,?,?,?,?,?,?,?)";
        try {
            Connection db = Database.connect();
            PreparedStatement pst = db.prepareStatement(sql);
            pst.setInt(1, hd.getId_user());
            pst.setString(2, hd.getEmail());
            pst.setString(3, hd.getHo_user());
            pst.setString(4, hd.getTen_user());
            pst.setInt(5, hd.getSdt());
            pst.setString(6, hd.getDiaChi());
            pst.setString(7, hd.getQuan());
            pst.setString(8, hd.getPhuong());
            pst.setString(9, hd.getChiTiet());

            status = pst.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return status;
    }
}
