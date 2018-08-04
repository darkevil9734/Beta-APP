package com.example.legia.mobileweb;


import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.legia.mobileweb.AdapterSanPham.BottomNavigationViewHelper;
import com.example.legia.mobileweb.AdapterSanPham.ListGopY;
import com.example.legia.mobileweb.AdapterSanPham.ListViewSanPhamSoSanh;
import com.example.legia.mobileweb.DTO.gopY;
import com.example.legia.mobileweb.DeviceInfo.deviceInfo;
import com.jaredrummler.android.device.DeviceName;

import java.util.ArrayList;
import java.util.List;

public class menu extends AppCompatActivity {
    BottomNavigationView menuBar;
    Button btnLogin, btnGopY, btnContact, btnStore;
    TextView txtWelcome;
    private String facebookID = "100006754115144";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        menuBar = findViewById(R.id.menuBar);
        btnLogin = findViewById(R.id.login);
        btnContact = findViewById(R.id.btnContact);
        btnGopY = findViewById(R.id.btnGopY);
        btnStore = findViewById(R.id.btnStore);
        txtWelcome = findViewById(R.id.welcome);

        final SharedPreferences sp = getSharedPreferences("userLogin", MODE_PRIVATE);
        if(sp.getBoolean("isLogin", false)){
            txtWelcome.setText("WELCOME " + sp.getString("username", null));
            btnLogin.setText("Logout");
        }
        else{
            txtWelcome.setText("");
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        // Menu bar
        BottomNavigationViewHelper.disableShiftMode(menuBar);
        Menu menus = menuBar.getMenu();
        MenuItem menuItem = menus.getItem(3);
        menuItem.setChecked(true);
        menuBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homePage:
                        startActivity(new Intent(menu.this, MainActivity.class));
                        break;
                    case R.id.news:
                        startActivity(new Intent(menu.this, menu_news.class));
                        break;
                    case R.id.cart:
                        startActivity(new Intent(menu.this, cart.class));
                        break;
                    case R.id.menu:
                        break;
                }
                return false;
            }
        });


        // Login/Logout
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sp.getBoolean("isLogin", false)){
                    SharedPreferences myPrefs = getSharedPreferences("userLogin",
                            MODE_PRIVATE);
                    SharedPreferences.Editor editor = myPrefs.edit();
                    editor.clear();
                    editor.commit();
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
    }
}
