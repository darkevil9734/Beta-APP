package com.example.legia.mobileweb;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.legia.mobileweb.AdapterHeThong.AdapterMenuMain;
import com.example.legia.mobileweb.AdapterSanPham.BottomNavigationViewHelper;
import com.example.legia.mobileweb.DAO.userDAO;
import com.example.legia.mobileweb.DTO.User;
import com.example.legia.mobileweb.DTO.menuMain;
import com.facebook.Profile;

import java.util.ArrayList;
import java.util.List;

import q.rorbin.badgeview.QBadgeView;

public class menu extends AppCompatActivity {
    BottomNavigationView menuBar;
    ListView listMenuMain;
    private String facebookID = "100006754115144";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        menuBar = findViewById(R.id.menuBar);

        listMenuMain = findViewById(R.id.listMenuMain);

        final SharedPreferences sp = getSharedPreferences("userLogin", MODE_PRIVATE);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Menu bar
        BottomNavigationViewHelper.disableShiftMode(menuBar);
        Menu menus = menuBar.getMenu();
        MenuItem menuItem = menus.getItem(4);
        menuItem.setChecked(true);

        SharedPreferences sharedPreferences = getSharedPreferences("shareGioHang", MODE_PRIVATE);
        final int soLuongMua = sharedPreferences.getInt("soLuongMua", 0);

        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) menuBar.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(3); // number of menu from left
        if(soLuongMua > 0){
            new QBadgeView(this).bindTarget(v).setBadgeNumber(soLuongMua);
        }

        int id_user = sp.getInt("idUser", 0);
        User u = userDAO.readInfo(id_user);


        List<menuMain> listMenu = new ArrayList<>();
        SharedPreferences isLoginFacebook = getSharedPreferences("loginWithFb", MODE_PRIVATE);
        SharedPreferences isLoginGoogle = getSharedPreferences("loginWithGoogle", MODE_PRIVATE);

        if(isLoginFacebook.getBoolean("isLoginWithFB", false)){
            Profile profile = Profile.getCurrentProfile();
            listMenu.add(new menuMain(13,"Chào bạn, " + profile.getName()));
        }else if(isLoginGoogle.getBoolean("isLoginWithGoogle", false)){
            listMenu.add(new menuMain(14,"Chào bạn, " + isLoginGoogle.getString("name", null)));

        }
        else if(sp.getBoolean("isLogin", false)){
            listMenu.add(new menuMain(8,"Chào bạn, " + sp.getString("username", null)));
        }
        listMenu.add(new menuMain(1,"Liên hệ với chúng tôi"));
        listMenu.add(new menuMain(2,"Góp ý"));
        listMenu.add(new menuMain(9, "Quét mã QR Code, Barcode"));
        listMenu.add(new menuMain(3,"Hệ thống cửa hàng"));
        listMenu.add(new menuMain(4,"Thẻ tích điểm"));
        listMenu.add(new menuMain(5,"Chính sách hậu mãi"));
        listMenu.add(new menuMain(10, "Ủng hộ chúng tôi"));
        listMenu.add(new menuMain(15, "Đặt hàng sản phẩm mới"));
        // If login with facebook
        SharedPreferences loginWithFB = getSharedPreferences("loginWithFb", MODE_PRIVATE);
        SharedPreferences loginWithGoogle = getSharedPreferences("loginWithGoogle", MODE_PRIVATE);
        if(loginWithGoogle.getBoolean("isLoginWithGoogle", false)){
            listMenu.add(new menuMain(12, "Đăng xuất khỏi Google"));
        }
        else if(loginWithFB.getBoolean("isLoginWithFB", false)){
            // Is login with facebook
            listMenu.add(new menuMain(11, "Đăng xuất khỏi Facebook"));
        }
        else{
            if(sp.getBoolean("isLogin", false)){
                listMenu.add(new menuMain(7,"Đăng xuất"));
            }
            else{
                listMenu.add(new menuMain(6,"Đăng nhập"));
            }
        }
        listMenu.add(new menuMain(16,"Layout Mới."));


        AdapterMenuMain adapterMenuMain = new AdapterMenuMain(menu.this, listMenu);
        listMenuMain.setAdapter(adapterMenuMain);

        menuBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homePage:
                        startActivity(new    Intent(menu.this, MainActivity.class));
                        break;
                    case R.id.app_bar_search:
                        startActivity(new Intent(menu.this, search.class));
                        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                        break;
                    case R.id.news:
                        startActivity(new Intent(menu.this, menu_news.class));
                        break;
                    case R.id.cart:
                        if (soLuongMua > 0){
                            startActivity(new Intent(menu.this, cart.class));
                        }
                        else{
                            android.support.v7.app.AlertDialog.Builder dlgAlert  = new android.support.v7.app.AlertDialog.Builder(menu.this);
                            dlgAlert.setMessage("Bạn chưa mua sản phẩm nào, vui lòng chọn sản phẩm !");
                            dlgAlert.setTitle("Giỏ hàng của bạn trống!");
                            dlgAlert.setPositiveButton("Quay lại", null);
                            dlgAlert.setCancelable(true);
                            dlgAlert.create().show();
                        }

                        break;
                    case R.id.menu:
                        break;
                }
                return false;
            }
        });

        /*
        // Login/Logout
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sp.getBoolean("isLogin", false)){

                    SharedPreferences user = getSharedPreferences("userLogin",
                            MODE_PRIVATE);
                    SharedPreferences.Editor editor = user.edit();
                    editor.clear();
                    editor.commit();

                    SharedPreferences gioHang = getSharedPreferences("shareGioHang", MODE_PRIVATE);
                    SharedPreferences.Editor gioHangEdit = gioHang.edit();
                    gioHangEdit.clear();
                    gioHangEdit.commit();

                    Intent intent = new Intent(menu.this,
                            MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent loginIntent = new Intent(menu.this, login.class);
                    startActivity(loginIntent);
                }

            }
        });

        // Tương tác
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Popup list of report type

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        menu.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                List<gopY> danhSachGopY = new ArrayList<>();
                                danhSachGopY.add(new gopY(1, "Facebook Messenger", R.drawable.fb_chat));
                                danhSachGopY.add(new gopY(2, "Gọi cho chúng tôi", R.drawable.call));
                                danhSachGopY.add(new gopY(3, "Nhắn tin cho chúng tôi", R.drawable.sms));
                                danhSachGopY.add(new gopY(4, "Viber", R.drawable.viber));

                                AlertDialog.Builder builder = new AlertDialog.Builder(menu.this);

                                // Set title
                                builder.setTitle("Chọn hình thức tương tác");

                                ListGopY adapterGopY = new ListGopY(menu.this, danhSachGopY);
                                ListView lv = new ListView(menu.this);

                                lv.setAdapter(adapterGopY);

                                builder.setView(lv);
                                AlertDialog dialog=builder.create();

                                dialog.show();
                            }
                        });

                    }
                });
                t.start();


            }
        });

        // Hệ thống cửa hàng
        btnStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(menu.this, hethong.class));
            }
        });

        // Góp ý
        btnGopY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setType("text/plain");
                sendIntent.setData(Uri.parse("mailto:testmailbaitap@gmail.com"));
                //sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "[Góp ý phần mềm]");
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        " Phiên bản android hiện tại: " + Build.VERSION.RELEASE
                            +"\n Tên điện thoại: " + Build.MODEL
                            +"\n API hiện tại: " + Build.VERSION.SDK_INT
                            +"\n --------------------------------------" +
                            "\n Nội dung góp ý: ");
                startActivity(sendIntent);
            }
        });
        */
    }
}
