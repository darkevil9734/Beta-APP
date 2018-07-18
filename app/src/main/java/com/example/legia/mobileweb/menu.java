package com.example.legia.mobileweb;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.legia.mobileweb.AdapterSanPham.BottomNavigationViewHelper;

public class menu extends AppCompatActivity {
    BottomNavigationView menuBar;
    Button btnLogin;
    TextView txtWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        menuBar = findViewById(R.id.menuBar);
        btnLogin = findViewById(R.id.login);
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



        BottomNavigationViewHelper.disableShiftMode(menuBar);
        Menu menus = menuBar.getMenu();
        MenuItem menuItem = menus.getItem(3);
        menuItem.setChecked(true);
        menuBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return false;
            }
        });

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
    }
}
