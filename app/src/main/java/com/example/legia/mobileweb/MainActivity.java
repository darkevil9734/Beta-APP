package com.example.legia.mobileweb;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.multidex.MultiDex;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.legia.mobileweb.AdapterSanPham.BottomNavigationViewHelper;
import com.example.legia.mobileweb.AdapterSanPham.SanPhamAdapter;
import com.example.legia.mobileweb.AdapterSanPham.SlidingAdapter;
import com.example.legia.mobileweb.CheckInternet.CheckInternet;
import com.example.legia.mobileweb.DAO.sanPhamDAO;
import com.example.legia.mobileweb.DAO.themVaoGioHang;
import com.example.legia.mobileweb.DTO.sanPham;
import com.example.legia.mobileweb.Database.Database;
import com.example.legia.mobileweb.Database.Firebase.DAO.countOrder;
import com.example.legia.mobileweb.Database.Firebase.FirebaseConnect;
import com.facebook.share.Share;
import com.github.arturogutierrez.Badges;
import com.github.arturogutierrez.BadgesNotSupportedException;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import q.rorbin.badgeview.QBadgeView;
import shortbread.Shortbread;
import shortbread.Shortcut;

import static android.support.constraint.Constraints.TAG;


public class MainActivity extends AppCompatActivity {
    int hinh[] = {R.drawable.hinh1, R.drawable.hinh2, R.drawable.hinh3};
    ViewPager myPager;
    RecyclerView listApple, listSamsung, listHTC, listAsus, listDuoi1Trieu, list1Den3Trieu, list3Den7Trieu, listCaoCap;
    ScrollView scrollView;
    Button btnApple, btnSamsung, btnHTC, btnASus, btnDuoi1Trieu, btn1Den3Trieu, btn3Den7Trieu, btnCaoCap;
    BottomNavigationView menuBar;

    Timer timer;
    int currentPage = 0;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.

    Intent i = null;
    String brand, type = null;

    private SharedPreferences spCountApp;
    private SharedPreferences.Editor editorCount;
    private int count = 0;

    @Shortcut(id = "cart", icon = R.drawable.carticonbread, shortLabel = "Giỏ hàng")
    public void openCart(){
        themVaoGioHang gioHang = SanPhamAdapter.gioHang;
        int count = gioHang.countSoLuongMua();
        if(count>0){
            startActivity(new Intent(this, cart.class));
        }
        else{
            TastyToast.makeText(this, "Chưa có sản phẩm trong giỏ hàng", TastyToast.LENGTH_SHORT, TastyToast.WARNING).show();
        }
    }

    @Shortcut(id = "login", icon = R.drawable.loginbread, shortLabel = "Đăng nhập")
    public void openLogin(){
        SharedPreferences loginFacebook = getSharedPreferences("loginWithFb", MODE_PRIVATE);
        SharedPreferences loginGoogle = getSharedPreferences("loginWithGoogle", MODE_PRIVATE);
        SharedPreferences loginNormal = getSharedPreferences("userLogin", MODE_PRIVATE);
        if(loginFacebook.getBoolean("isLoginWithFB", false)){
            TastyToast.makeText(this, "Bạn đã đăng nhập trước đó", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
        }
        else if(loginGoogle.getBoolean("isLoginWithGoogle",false)){
            TastyToast.makeText(this, "Bạn đã đăng nhập trước đó", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
        }
        else if(loginNormal.getBoolean("isLogin",false)){
            TastyToast.makeText(this, "Bạn đã đăng nhập trước đó", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
        }
        else{
            startActivity(new Intent(this, login.class));
        }
    }

    @Shortcut(id = "scan", icon = R.drawable.scancode, shortLabel = "Quét mã QR/Barcode")
    public void openScan(){
        startActivity(new Intent(this, scancode.class));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(this);
        Shortbread.create(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Optional
        spCountApp = getSharedPreferences("countApp", MODE_PRIVATE);
        // Get number of count:
        count = spCountApp.getInt("countApp", 0);
        if(count==10){
            Toast.makeText(this, "RATE" , Toast.LENGTH_SHORT).show();
            // Rating popup
        }

        // Count app open:
        editorCount = spCountApp.edit();
        editorCount.putInt("countApp", count+=1);
        editorCount.commit();



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

        /*View viewEvent = getLayoutInflater().inflate( R.layout.layout_event, null);


        AlertDialog.Builder dialog = new AlertDialog.Builder( this );
        dialog.setView(viewEvent);

        dialog.show();*/


        // menu bar
        menuBar = findViewById(R.id.menuBar);

        // Cách 1: intent truyền số lượng mua qua
        Intent cart = getIntent();
        final Bundle b = cart.getBundleExtra("GioHang");

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
                        startActivity(new Intent(MainActivity.this, search.class));
                        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                        break;
                    case R.id.news:
                        Intent news = new Intent(MainActivity.this, menu_news.class);
                        startActivity(news);
                        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                        break;
                    case R.id.cart:
                        if (soLuongMua > 0){
                            Intent cart = new Intent(MainActivity.this, cart.class);
                            startActivity(cart);
                            overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                        }
                        else{
                            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(MainActivity.this);
                            dlgAlert.setMessage("Bạn chưa mua sản phẩm nào, vui lòng chọn sản phẩm !");
                            dlgAlert.setTitle("Giỏ hàng của bạn trống!");
                            dlgAlert.setPositiveButton("Quay lại", null);
                            dlgAlert.setCancelable(true);
                            dlgAlert.create().show();
                        }

                        break;
                    case R.id.menu:
                        Intent menu_bar = new Intent(MainActivity.this, menu.class);
                        startActivity(menu_bar);
                        break;
                }
                return false;
            }
        });

        if(CheckInternet.checkConnection(this)){
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

                Handler hdl = new Handler(Looper.getMainLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        handleDanhSach();
                    }
                };
                hdl.post(runnable);

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
        else {
            LinearLayout layoutAll = findViewById(R.id.layoutAll);
            layoutAll.removeAllViews();
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
            alertDialog.setTitle("Turn on WI-Fi..");
            alertDialog.setMessage("Do you want to go to wifi settings?");
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.cancel();
                }
            });
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
                }
            });

            alertDialog.show();


        }



    }

    protected void handleDanhSach(){
        Handler ds = new Handler(getMainLooper());
        Runnable run = new Runnable() {
            @Override
            public void run() {
                threadDanhSach();
            }
        };
        ds.post(run);
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
