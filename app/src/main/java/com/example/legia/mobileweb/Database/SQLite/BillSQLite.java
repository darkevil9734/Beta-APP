package com.example.legia.mobileweb.Database.SQLite;

public class BillSQLite {
    private int maSanPham, soLuong;

    public  BillSQLite(){

    }

    public BillSQLite(int maSanPham, int soLuong) {
        this.maSanPham = maSanPham;
        this.soLuong = soLuong;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
