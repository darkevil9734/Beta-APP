package com.example.legia.mobileweb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.legia.mobileweb.AdapterSanPham.BottomNavigationViewHelper;
import com.example.legia.mobileweb.AdapterSanPham.SanPhamAdapter;
import com.example.legia.mobileweb.AdapterSanPham.SanPhamAdapterNew;
import com.example.legia.mobileweb.AdapterSanPham.SlidingAdapter;
import com.example.legia.mobileweb.DAO.sanPhamDAO;
import com.example.legia.mobileweb.DAO.themVaoGioHang;
import com.example.legia.mobileweb.DTO.sanPham;
import com.github.arturogutierrez.Badges;
import com.github.arturogutierrez.BadgesNotSupportedException;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import q.rorbin.badgeview.QBadgeView;

import static android.support.constraint.Constraints.TAG;

public class test extends AppCompatActivity {
    RecyclerView viewTest;
    ViewPager myPager;
    Timer timer;
    BottomNavigationView menuBar;
    boolean isLoading=false;

    int currentPage = 0;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.
    int hinh[] = {R.drawable.hinh1, R.drawable.hinh2, R.drawable.hinh3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_test);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        myPager =  findViewById(R.id.bannerNew);
        viewTest = findViewById(R.id.viewTest);
        menuBar = findViewById(R.id.menuBarNew);

        // Cách 2: lấy từ SharedPrefence lên:
        SharedPreferences sharedPreferences = getSharedPreferences("shareGioHang", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        final int soLuongMua = sharedPreferences.getInt("soLuongMua", 0);


        themVaoGioHang gioHang = SanPhamAdapter.gioHang;

        if(soLuongMua>0){
            editor.clear();
            editor.commit();
        }
        try {
            Intent i = getIntent();
            int count = i.getIntExtra("countGioHang", 0);
            if(count>0){
                Badges.setBadge(this, count);
            }
            else{
                Badges.setBadge(this, gioHang.countSoLuongMua());
            }
        } catch (BadgesNotSupportedException badgesNotSupportedException) {
            Log.d(TAG, badgesNotSupportedException.getMessage());
        }
        BottomNavigationViewHelper.disableShiftMode(menuBar);
        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) menuBar.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(3); // number of menu from left
        if(soLuongMua > 0){
            new QBadgeView(this).bindTarget(v).setBadgeNumber(soLuongMua);
        }

        menuBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homePage:
                        break;
                    case R.id.app_bar_search:
                        startActivity(new Intent(test.this, search.class));
                        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                        break;
                    case R.id.news:
                        Intent news = new Intent(test.this, menu_news.class);
                        startActivity(news);
                        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                        break;
                    case R.id.cart:
                        if (soLuongMua > 0){
                            Intent cart = new Intent(test.this, cart.class);
                            startActivity(cart);
                            overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                        }
                        else{
                            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(test.this);
                            dlgAlert.setMessage("Bạn chưa mua sản phẩm nào, vui lòng chọn sản phẩm !");
                            dlgAlert.setTitle("Giỏ hàng của bạn trống!");
                            dlgAlert.setPositiveButton("Quay lại", null);
                            dlgAlert.setCancelable(true);
                            dlgAlert.create().show();
                        }

                        break;
                    case R.id.menu:
                        Intent menu_bar = new Intent(test.this, menu.class);
                        startActivity(menu_bar);
                        break;
                }
                return false;
            }
        });



        final SlidingAdapter adapter = new SlidingAdapter(this, hinh);
        myPager.setAdapter(adapter);
        final Handler handler = new Handler(getMainLooper());
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


        int numberOfColumns = 3;
        viewTest.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        List<sanPham> dssp = sanPhamDAO.DocTatCa();
        SanPhamAdapterNew adapterNew = new SanPhamAdapterNew(this, dssp, viewTest);

        viewTest.setAdapter(adapterNew);

        /*int total = dssp.size();
        int spPerPage = 8;

        int pageSize = total/spPerPage + (total%spPerPage != 0 ? 1 : 0);

        int index = total / pageSize;*/


    }
}
