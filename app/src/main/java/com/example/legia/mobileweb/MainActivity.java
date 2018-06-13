package com.example.legia.mobileweb;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.legia.mobileweb.AdapterSanPham.SanPhamAdapter;
import com.example.legia.mobileweb.AdapterSanPham.SlidingAdapter;
import com.example.legia.mobileweb.DAO.sanPhamDAO;
import com.example.legia.mobileweb.DTO.sanPham;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    int hinh[] = {R.drawable.hinh1, R.drawable.hinh2, R.drawable.hinh3};
    ViewPager myPager;
    RecyclerView listApple, listSamsung, listHTC, listAsus, listDuoi1Trieu, list1Den3Trieu, list3Den7Trieu, listCaoCap;
    ScrollView scrollView;
    Button btnApple, btnSamsung, btnHTC, btnASus, btnDuoi1Trieu, btn1Den3Trieu, btn3Den7Trieu, btnCaoCap;
    TextView lbApple, lbSamsumg, lbAsus, lbHTC, lbDuoi1Trieu, lb1Den3Trieu, lb3Den7Trieu, lbCaoCap;

    Timer timer;
    int currentPage = 0;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.

    Intent i = null;
    String brand, type = null;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // findViewByID
        scrollView = findViewById(R.id.scroll);
        myPager =  findViewById(R.id.banner);
        // list
        listApple = findViewById(R.id.listApple);
        listSamsung = findViewById(R.id.listSamsung);
        listAsus = findViewById(R.id.listAsus);
        listHTC = findViewById(R.id.listHTC);
        listDuoi1Trieu = findViewById(R.id.listDuoi1Trieu);
        list1Den3Trieu = findViewById(R.id.list1Den3Trieu);
        list3Den7Trieu = findViewById(R.id.list3Den7Trieu);
        listCaoCap = findViewById(R.id.listCaoCap);
        //button
        btnApple = findViewById(R.id.btnChiTietApple);
        btnSamsung = findViewById(R.id.btnChiTietSamsung);
        btnHTC = findViewById(R.id.btnChiTietHTC);
        btnASus = findViewById(R.id.btbChiTietAsus);
        btnDuoi1Trieu = findViewById(R.id.btbChiTietDuoi1Trieu);
        btn1Den3Trieu = findViewById(R.id.btbChiTiet1Den3Trieu);
        btn3Den7Trieu = findViewById(R.id.btbChiTiet3Den7Trieu);
        btnCaoCap = findViewById(R.id.btbChiTietCaoCap);
        //label
        lbApple = findViewById(R.id.lbApple);


        final SlidingAdapter adapter = new SlidingAdapter(this, hinh);
        myPager.setAdapter(adapter);

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == adapter.getCount()) {
                    currentPage = 0;
                }
                myPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer();
        timer .schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        threadDanhSach();


        btnApple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brand = "Apple";
                type = "brandName";
                i = new Intent(MainActivity.this, gridProduct.class);
                Bundle b = new Bundle();
                b.putString("type", type);
                b.putString("brand", brand);
                i.putExtra("chiTiet", b);

                startActivity(i);
            }
        });

        btnSamsung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brand = "Samsung";
                type = "brandName";
                i = new Intent(MainActivity.this, gridProduct.class);
                Bundle b = new Bundle();
                b.putString("type", type);
                b.putString("brand", brand);
                i.putExtra("chiTiet", b);

                startActivity(i);
            }
        });

        btnASus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brand = "Asus";
                type = "brandName";
                i = new Intent(MainActivity.this, gridProduct.class);
                Bundle b = new Bundle();
                b.putString("type", type);
                b.putString("brand", brand);
                i.putExtra("chiTiet", b);

                startActivity(i);
            }
        });

        btnHTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brand = "HTC";
                type = "brandName";
                i = new Intent(MainActivity.this, gridProduct.class);
                Bundle b = new Bundle();
                b.putString("type", type);
                b.putString("brand", brand);
                i.putExtra("chiTiet", b);

                startActivity(i);
            }
        });

        btnDuoi1Trieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "sorter";
                brand = "Dưới 1 Triệu";
                List<sanPham> dsSorter = danhSachDuoi1Trieu();
                i = new Intent(MainActivity.this, gridProduct.class);
                Bundle b = new Bundle();
                b.putString("type", type);
                b.putString("brand", brand);
                i.putExtra("chiTiet", b);

                startActivity(i);
            }
        });

        btn1Den3Trieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "sorter";
                brand = "1 Đến 3 Triệu";
                List<sanPham> dsSorter = danhSachDuoi1Trieu();
                i = new Intent(MainActivity.this, gridProduct.class);
                Bundle b = new Bundle();
                b.putString("type", type);
                b.putString("brand", brand);
                i.putExtra("chiTiet", b);

                startActivity(i);
            }
        });

        btn3Den7Trieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "sorter";
                brand = "3 Đến 7 Triệu";
                List<sanPham> dsSorter = danhSachDuoi1Trieu();
                i = new Intent(MainActivity.this, gridProduct.class);
                Bundle b = new Bundle();
                b.putString("type", type);
                b.putString("brand", brand);
                i.putExtra("chiTiet", b);

                startActivity(i);
            }
        });

        btnCaoCap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "sorter";
                brand = "Cao cấp";
                List<sanPham> dsSorter = danhSachDuoi1Trieu();
                i = new Intent(MainActivity.this, gridProduct.class);
                Bundle b = new Bundle();
                b.putString("type", type);
                b.putString("brand", brand);
                i.putExtra("chiTiet", b);

                startActivity(i);
            }
        });

    }



    protected void threadDanhSach(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dsApple();
                        dsSamsung();
                        dsHTC();
                        dsAsus();
                        dsDuoi1Trieu();
                        ds1Den3Trieu();
                        ds3Den7Trieu();
                        dsCaoCap();
                    }
                });
            }
        });
        t.start();
    }


    private List<sanPham> danhSachApple(){
        List<sanPham> ds = sanPhamDAO.timKiemTheoHang("Apple");
        return  ds;
    }

    private List<sanPham> danhSachSamsung(){
        List<sanPham> ds = sanPhamDAO.timKiemTheoHang("Samsung");
        return  ds;
    }

    private List<sanPham> danhSachHTC(){
        List<sanPham> ds = sanPhamDAO.timKiemTheoHang("HTC");
        return  ds;
    }

    private List<sanPham> danhSachAsus(){
        List<sanPham> ds = sanPhamDAO.timKiemTheoHang("Asus");
        return  ds;
    }

    private List<sanPham> danhSachDuoi1Trieu(){
        List<sanPham> ds = sanPhamDAO.timTheoGiaDuoi1Trieu();
        return  ds;
    }

    private List<sanPham> danhSach1Den3Trieu(){
        List<sanPham> ds = sanPhamDAO.timTheoGia1TrieuDen3Trieu();
        return  ds;
    }

    private List<sanPham> danhSach3Den7Trieu(){
        List<sanPham> ds = sanPhamDAO.timTheoGia3TrieuDen7Trieu();
        return  ds;
    }

    private List<sanPham> danhSachCaoCap(){
        List<sanPham> ds = sanPhamDAO.timTheoGiaTren10Trieu();
        return  ds;
    }


    private SanPhamAdapter adapter;
    private List<sanPham> ds;

    private void dsApple() {
        listApple = findViewById(R.id.listApple);
        adapter = new SanPhamAdapter(MainActivity.this,danhSachApple());

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        listApple.setLayoutManager(layoutManager);
        listApple.setAdapter(adapter);

        //ds = danhSachApple();
        //int count = ds.size();

        //String size = lbApple.getText().toString();
        //lbApple.setText(Html.fromHtml("<p><strong>"+size+ "</strong> ( Có " + count + " sản phẩm )</p>"));
    }

    private void dsSamsung() {
        listSamsung = findViewById(R.id.listSamsung);
        adapter = new SanPhamAdapter(MainActivity.this,danhSachSamsung());

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        listSamsung.setLayoutManager(layoutManager);
        listSamsung.setAdapter(adapter);
    }

    private void dsAsus() {
        listAsus = findViewById(R.id.listAsus);
        adapter = new SanPhamAdapter(MainActivity.this,danhSachAsus());

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        listAsus.setLayoutManager(layoutManager);
        listAsus.setAdapter(adapter);
    }

    private void dsHTC() {
        listHTC = findViewById(R.id.listHTC);
        adapter = new SanPhamAdapter(MainActivity.this,danhSachHTC());

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        listHTC.setLayoutManager(layoutManager);
        listHTC.setAdapter(adapter);
    }

    private void dsDuoi1Trieu() {
        listDuoi1Trieu = findViewById(R.id.listDuoi1Trieu);
        adapter = new SanPhamAdapter(MainActivity.this,danhSachDuoi1Trieu());

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        listDuoi1Trieu.setLayoutManager(layoutManager);
        listDuoi1Trieu.setAdapter(adapter);
    }

    private void ds1Den3Trieu() {
        list1Den3Trieu = findViewById(R.id.list1Den3Trieu);
        adapter = new SanPhamAdapter(MainActivity.this,danhSach1Den3Trieu());

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        list1Den3Trieu.setLayoutManager(layoutManager);
        list1Den3Trieu.setAdapter(adapter);
    }

    private void ds3Den7Trieu() {
        list3Den7Trieu = findViewById(R.id.list3Den7Trieu);
        adapter = new SanPhamAdapter(MainActivity.this,danhSach3Den7Trieu());

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        list3Den7Trieu.setLayoutManager(layoutManager);
        list3Den7Trieu.setAdapter(adapter);
    }

    private void dsCaoCap() {
        listCaoCap = findViewById(R.id.listCaoCap);
        adapter = new SanPhamAdapter(MainActivity.this,danhSachCaoCap());

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        listCaoCap.setLayoutManager(layoutManager);
        listCaoCap.setAdapter(adapter);
    }



}
