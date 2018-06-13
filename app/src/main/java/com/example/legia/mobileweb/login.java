package com.example.legia.mobileweb;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.legia.mobileweb.DAO.userDAO;
import com.example.legia.mobileweb.DTO.User;

public class login extends AppCompatActivity {
    EditText txtUsername, txtPassword;
    CheckBox cbRemember;
    Button btnLogin;
    Button btnRegister;

    SharedPreferences sp = getSharedPreferences("userLogin", MODE_PRIVATE);
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String username, password;

        txtUsername = findViewById(R.id.txtUsernameLogin);
        txtPassword = findViewById(R.id.txtPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        cbRemember = findViewById(R.id.cbRemember);

        username = txtUsername.getText().toString();
        password = txtPassword.getText().toString();

        if(cbRemember.isChecked()){
            rememberLogin(username, password);
        }
        else {
           // user
        }


    }

    private User login(String username, String password){
        User u = userDAO.Login(username,password);
        return u;
    }

    private void rememberLogin(String username, String password){
            editor = sp.edit();
            editor.putString("username", username);
            editor.putString("password", password);

            editor.commit();
    }
}
