package com.example.legia.mobileweb;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.legia.mobileweb.DAO.userDAO;
import com.example.legia.mobileweb.DTO.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class register extends AppCompatActivity {
    EditText txtHoTen, txtUsername, txtPassword, txtEmail;
    Button btnLogin, btnRegister;
    ProgressDialog pd;

    //Handles the thread result of the Backup being executed.
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(android.os.Message msg) {
            pd.dismiss();
            Intent intent = new Intent(register.this, login.class);
            startActivity(intent);
            Toast.makeText(register.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //findView
        txtUsername = findViewById(R.id.username);
        txtHoTen = findViewById(R.id.name);
        txtPassword = findViewById(R.id.password);
        txtEmail = findViewById(R.id.email);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLinkToLoginScreen);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hoten, username, password, email;
                hoten = txtHoTen.getText().toString();
                username = txtUsername.getText().toString();
                password = txtPassword.getText().toString();
                email = txtEmail.getText().toString();

                if(hoten.length()==0){
                    txtHoTen.setError("Bạn không được bỏ trống");
                }else if(username.length()==0){
                    txtUsername.setError("Bạn không được bỏ trống");
                }else if(password.length()==0){
                    txtPassword.setError("Bạn không được bỏ trống");
                }else if(email.length()==0){
                    txtEmail.setError("Bạn không được bỏ trống");
                }
                else if(validateEmailAddress(email)==false){
                    txtEmail.setError("Bạn nhập không đúng định dạng Email");
                }
                else{
                    // thỏa
                    int dk = dangKy(hoten, username, password, email);
                    if(dk>0){
                        pd = ProgressDialog.show(register.this,"Vui lòng đợi...", "Đang đăng ký...", false, true);
                        pd.setCanceledOnTouchOutside(false);
                        Thread t = new Thread()
                        {
                            @Override
                            public void run()
                            {
                                try
                                {
                                    sleep(3000);  //Delay of 3 seconds
                                }
                                catch (Exception e) {}
                                handler.sendEmptyMessage(0);
                            }
                        } ;
                        t.start();
                    }
                    else{
                        Toast.makeText(register.this, "ERROR!!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(register.this, login.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private static Pattern emailNamePtrn = Pattern.compile(
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    public static boolean validateEmailAddress(String email){

        Matcher match = emailNamePtrn.matcher(email);
        if(match.matches()){
            return true;
        }
        return false;
    }

    private int dangKy(String hoTen, String username, String password, String email){
        User u = null;
        String[] hoVaTen = hoTen.split(" ");
        String ho = hoVaTen[0];
        String ten = hoVaTen[1];

        u = userDAO.kiemTraUser(username,email);
        if(u==null){
            u = new User();
            u.setHo_user(ho);
            u.setTen_user(ten);
            u.setUsername(username);
            u.setPassword(password);
            u.setEmail(email);
            int dk = userDAO.dangKy(u);

            return dk;
        }
        else{
            Toast.makeText(this, "TRÙNG EMAIL HOẶC USERNAME", Toast.LENGTH_SHORT).show();
        }

        return 0;
    }
}
