package com.example.legia.mobileweb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.legia.mobileweb.DAO.sanPhamDAO;
import com.example.legia.mobileweb.DTO.sanPham;

import java.sql.Blob;
import java.sql.SQLException;

public class Detail extends AppCompatActivity {
    ImageView hinhSanPham;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        hinhSanPham = findViewById(R.id.imgHinhSanPham);

        GetBundle();
    }

    private void GetBundle(){
        Intent i = getIntent();

        if(i!=null){
            try{
                Bundle b = i.getBundleExtra("SanPhamChon");
                int maSanPham = b.getInt("MaSanPham");

                sanPham sanPham = DocChiTiet(maSanPham);

                Blob blob = sanPham.getHinh_dai_dien();

                int blobLength = (int) blob.length();
                byte[] blobAsBytes = blob.getBytes(1, blobLength);
                Bitmap btm = BitmapFactory.decodeByteArray(blobAsBytes,0,blobAsBytes.length);

                hinhSanPham.setImageBitmap(btm);
            }
            catch (SQLException e){

            }

        }
        else{

        }
    }

    private sanPham DocChiTiet(int ma_san_pham){
        sanPham sp = sanPhamDAO.docTheoID(ma_san_pham);
        return sp;
    }
}
