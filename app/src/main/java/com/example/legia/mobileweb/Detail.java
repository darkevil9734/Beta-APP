package com.example.legia.mobileweb;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.legia.mobileweb.AdapterSanPham.ListViewSanPhamSoSanh;
import com.example.legia.mobileweb.AdapterSanPham.SanPhamAdapter;
import com.example.legia.mobileweb.DAO.sanPhamDAO;
import com.example.legia.mobileweb.DAO.themVaoGioHang;
import com.example.legia.mobileweb.DTO.sanPham;

import com.github.juanlabrador.badgecounter.BadgeCounter;


import java.sql.Blob;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class Detail extends AppCompatActivity {
    ImageView hinhSanPham;
    Button btnThemVaoGio, btnSoSanh;
    TextView txtTenSanPhamChiTiet, txtHangSanXuatChiTiet, txtGiaSanPhamChiTiet, txtCameraTruocChiTiet, txtCameraSauChiTiet, txtTinhTrangChiTiet;
    int maSanPham;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        this.setTitle("Xem chi tiết");
        hinhSanPham = findViewById(R.id.imgHinhSanPham);
        btnThemVaoGio = findViewById(R.id.btnMua);
        btnSoSanh = findViewById(R.id.btnSoSanh);

        txtTenSanPhamChiTiet = findViewById(R.id.txtTenChiTietSanPham);
        txtHangSanXuatChiTiet = findViewById(R.id.txtHangChiTietSanPham);
        txtCameraSauChiTiet = findViewById(R.id.txtCameraSauChiTietSanPham);
        txtCameraTruocChiTiet = findViewById(R.id.txtCameraTruocChiTietSanPham);
        txtGiaSanPhamChiTiet = findViewById(R.id.txtGiaTienChiTiet);
        txtTinhTrangChiTiet = findViewById(R.id.txtTinhTrangChiTietSanPham);



        btnSoSanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<sanPham> dsSanPham = sanPhamDAO.DocTatCa();
                AlertDialog.Builder builder = new AlertDialog.Builder(Detail.this);

                // Set title value.
                builder.setTitle("Chọn sản phẩm cần so sánh");
                int listItemLen = dsSanPham.size();

                ListViewSanPhamSoSanh adapterSoSanh = new ListViewSanPhamSoSanh(Detail.this, dsSanPham, maSanPham);
                ListView lv = new ListView(Detail.this);

                lv.setAdapter(adapterSoSanh);

                builder.setView(lv);
                AlertDialog dialog=builder.create();

                dialog.show();

            }
        });

        GetBundle();
    }

    private void GetBundle(){
        Intent i = getIntent();

        if(i!=null){
            try{
                Bundle b = i.getBundleExtra("SanPhamChon");
                maSanPham = b.getInt("MaSanPham");

                sanPham sanPham = DocChiTiet(maSanPham);

                Blob blob = sanPham.getHinh_dai_dien();

                int blobLength = (int) blob.length();
                byte[] blobAsBytes = blob.getBytes(1, blobLength);
                Bitmap btm = BitmapFactory.decodeByteArray(blobAsBytes,0,blobAsBytes.length);

                hinhSanPham.setImageBitmap(btm);
                DecimalFormat df = new DecimalFormat("###,###.##");
                txtGiaSanPhamChiTiet.setText(df.format(sanPham.getGiaSanPham())+"đ");
                txtHangSanXuatChiTiet.setText(sanPham.getHangSanXuat());
                txtTinhTrangChiTiet.setText(sanPham.getTinhTrang());
                txtCameraTruocChiTiet.setText(sanPham.getCamera_truoc());
                txtCameraSauChiTiet.setText(sanPham.getCamera_sau());
                txtTenSanPhamChiTiet.setText(sanPham.getTenSanPham());
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cart, menu);
        Intent i = getIntent();

        if(i!=null) {
            Bundle b = i.getBundleExtra("SanPhamChon");
            if (b != null) {

            }
        }

        return true;
    }
}
