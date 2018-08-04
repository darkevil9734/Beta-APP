package com.example.legia.mobileweb;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
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

import com.example.legia.mobileweb.AdapterSanPham.BottomNavigationViewHelper;
import com.example.legia.mobileweb.AdapterSanPham.ListViewSanPhamSoSanh;
import com.example.legia.mobileweb.AdapterSanPham.SanPhamAdapter;
import com.example.legia.mobileweb.DAO.sanPhamDAO;
import com.example.legia.mobileweb.DAO.themVaoGioHang;
import com.example.legia.mobileweb.DTO.sanPham;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.github.juanlabrador.badgecounter.BadgeCounter;


import java.sql.Blob;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class Detail extends AppCompatActivity {
    ImageView hinhSanPham;
    BottomNavigationView menuChiTiet;
    Button btnThemVaoGio, btnSoSanh;
    TextView txtTenSanPhamChiTiet, txtHangSanXuatChiTiet, txtGiaSanPhamChiTiet, txtCameraTruocChiTiet, txtCameraSauChiTiet, txtTinhTrangChiTiet;
    int maSanPham;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //FacebookSdk.setApplicationId("254499031809848");
        //FacebookSdk.sdkInitialize(Detail.this);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        this.setTitle("Xem chi tiết");
        hinhSanPham = findViewById(R.id.imgHinhSanPham);
        btnThemVaoGio = findViewById(R.id.btnMua);
        btnSoSanh = findViewById(R.id.btnSoSanh);
        menuChiTiet = findViewById(R.id.menuChiTiet);

        txtTenSanPhamChiTiet = findViewById(R.id.txtTenChiTietSanPham);
        txtHangSanXuatChiTiet = findViewById(R.id.txtHangChiTietSanPham);
        txtCameraSauChiTiet = findViewById(R.id.txtCameraSauChiTietSanPham);
        txtCameraTruocChiTiet = findViewById(R.id.txtCameraTruocChiTietSanPham);
        txtGiaSanPhamChiTiet = findViewById(R.id.txtGiaTienChiTiet);
        txtTinhTrangChiTiet = findViewById(R.id.txtTinhTrangChiTietSanPham);

        BottomNavigationViewHelper.disableShiftMode(menuChiTiet);
        menuChiTiet.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.general:
                        break;
                    case R.id.comment:
                        Intent intent = getIntent();
                        Bundle b = intent.getBundleExtra("SanPhamChon");
                        int id = b.getInt("MaSanPham");
                        String url = "http://localhost:8081/web-mobile/xemChiTietSanPhamServlet?id="+id;

                        Intent intent2 = new Intent(Detail.this, comment.class);
                        intent2.putExtra("url", url);
                        startActivity(intent2);

                        break;
                }
                return false;

            }
        });
        btnSoSanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<sanPham> dsSanPham = sanPhamDAO.danhSachSoSanh(maSanPham);
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

        btnThemVaoGio.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                themVaoGioHang gioHang = SanPhamAdapter.gioHang;
                gioHang.them(maSanPham, 1);

                startActivity(new Intent(Detail.this, cart.class));

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.share:
                ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                        .setQuote("Chia sẻ")
                        .build();
                ShareDialog.show(this,shareLinkContent);
                break;
        }
        return true;
    }
}
