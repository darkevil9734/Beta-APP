package com.example.legia.mobileweb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.legia.mobileweb.DAO.sanPhamDAO;
import com.example.legia.mobileweb.DTO.sanPham;

import java.sql.Blob;
import java.sql.SQLException;

public class sosanh extends AppCompatActivity {
    ImageView hinhSanPhamChinh, hinhSanPhamSoSanh;
    TextView txtTenSPChinh, txtHangSPChinh, txtTinhTrangSPChinh, txtTinhNangSPChinh, txtCameraTruocSPChinh, txtCameraSauSPChinh, txtDungLuongPinSPChinh;
    TextView txtTenSPSoSanh, txtHangSPSoSanh, txtTinhTrangSPSoSanh, txtTinhNangSPSoSanh, txtCameraTruocSPSoSanh, txtCameraSauSPSoSanh, txtDungLuongPinSPSoSanh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sosanh);

        // Sản phẩm chính
        hinhSanPhamChinh = findViewById(R.id.imgHinhA);
        txtTenSPChinh = findViewById(R.id.txtTenSPChinh);


        // Sản phẩm so sánh
        txtTenSPSoSanh = findViewById(R.id.txtTenSPSoSanh);
        hinhSanPhamSoSanh = findViewById(R.id.imgHinhB);


        final int maSanPham, maSanPhamSoSanh;

        Intent i = getIntent();

        if(i!=null){
            Bundle b = i.getBundleExtra("soSanh");
            maSanPham = b.getInt("MaSanPhamChinh");
            maSanPhamSoSanh = b.getInt("MaSanPhamSoSanh");

            sanPhamChinh(maSanPham);
            sanPhamSoSanh(maSanPhamSoSanh);

        }
        else{
            // LỖI
        }
    }

    private void sanPhamChinh(int maSanPham){
        try {
            sanPham sanPhamChinh = sanPhamDAO.docTheoID(maSanPham);
            Blob blobSanPhamChinh = sanPhamChinh.getHinh_dai_dien();

            int blobLength = (int) blobSanPhamChinh.length();
            byte[] blobAsBytes = blobSanPhamChinh.getBytes(1, blobLength);
            Bitmap btm = BitmapFactory.decodeByteArray(blobAsBytes,0,blobAsBytes.length);

            hinhSanPhamChinh.setImageBitmap(btm);
            txtTenSPChinh.setText(sanPhamChinh.getTenSanPham());
        }
        catch (SQLException e){

        }

    }

    private void sanPhamSoSanh(int maSanPhamSoSanh){
        try {
            sanPham sanPhamSoSanh = sanPhamDAO.docTheoID(maSanPhamSoSanh);
            Blob blobSanPhamSoSanh = sanPhamSoSanh.getHinh_dai_dien();

            int blobLength = (int) blobSanPhamSoSanh.length();
            byte[] blobAsBytes = blobSanPhamSoSanh.getBytes(1, blobLength);
            Bitmap btm = BitmapFactory.decodeByteArray(blobAsBytes,0,blobAsBytes.length);

            hinhSanPhamSoSanh.setImageBitmap(btm);
            txtTenSPSoSanh.setText(sanPhamSoSanh.getTenSanPham());
        }
        catch (SQLException e){

        }

    }
}
