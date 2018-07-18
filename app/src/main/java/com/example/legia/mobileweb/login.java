package com.example.legia.mobileweb;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.legia.mobileweb.DAO.userDAO;
import com.example.legia.mobileweb.DTO.User;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class login extends AppCompatActivity {
    EditText txtUsername, txtPassword;
    Button btnLogin;
    Button btnRegister, btnForgetPass;
    ProgressDialog pd;

    //Handles the thread result of the Backup being executed.
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(android.os.Message msg) {
            pd.dismiss();
            Intent intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(login.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsername = findViewById(R.id.txtUsernameLogin);
        txtPassword = findViewById(R.id.txtPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegisterLogin);
        btnForgetPass = findViewById(R.id.btnForgotPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                if(username.length()==0){
                    txtUsername.setError("Bạn không được để trống");
                }
                if(password.length()==0){
                    txtPassword.setError("Bạn không được để trống");
                }
                User u = login(username, password);
                if(u != null){
                    SharedPreferences sp = getSharedPreferences("userLogin", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.putBoolean("isLogin", true);
                    editor.commit();


                    pd = ProgressDialog.show(login.this,"Vui lòng đợi...", "Đang đăng nhập...", false, true);
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
                else {
                    Toast.makeText(login.this, "ERROR", Toast.LENGTH_SHORT).show();

                }
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dky = new Intent(login.this, register.class);
                startActivity(dky);
            }
        });

        btnForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(login.this);
                dialog.setContentView(R.layout.layout_forget);
                dialog.setTitle("Bạn quên mật khẩu ?");
                dialog.show();

                Button btnRecovery =  dialog.findViewById(R.id.btnRecoveryPassword);
                final EditText txtUserForgot = dialog.findViewById(R.id.txtUsernameForget);
                final EditText txtEmailForgot = dialog.findViewById(R.id.txtEmailForget);
                btnRecovery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String usernameForgot = txtUserForgot.getText().toString();
                        String emailForgot = txtEmailForgot.getText().toString();
                        if(usernameForgot.length()==0){
                            txtUserForgot.setError("Không được bỏ trống");
                            return;
                        }
                        else if(emailForgot.length()==0){
                            txtEmailForgot.setError("Không được bỏ trống");
                            return;
                        }
                        else{
                            User thanhVienQuenMatKhau = userDAO.ResetPassword(usernameForgot, emailForgot);
                            if(thanhVienQuenMatKhau != null){
                                // tìm thấy tên và email thành viên này trong csdl
                                final String admin = "testmailbaitap@gmail.com";
                                final String password_admin = "Vuhoangnguyen";

                                Properties props = new Properties();
                                props.put("mail.smtp.auth", "true");
                                props.put("mail.smtp.starttls.enable", "true");
                                props.put("mail.smtp.host", "smtp.gmail.com");
                                props.put("mail.smtp.port", "587");

                                Session session = Session.getInstance(props,
                                    new javax.mail.Authenticator() {
                                        protected PasswordAuthentication getPasswordAuthentication() {
                                            return new PasswordAuthentication(admin, password_admin);
                                        }
                                    }
                                );

                                try {
                                    Message message = new MimeMessage(session);
                                    message.setHeader("Content-Type", "text/plain; charset=UTF-8");
                                    message.setFrom(new InternetAddress("testmailbaitap@gmail.com"));
                                    message.setRecipients(Message.RecipientType.TO,
                                            InternetAddress.parse(emailForgot));
                                    message.setSubject("Khôi phục mật khẩu");
                                    message.setText("Chào bạn ," + thanhVienQuenMatKhau.getTen_user()
                                            + "Mật khẩu của bạn đã đặt là: " + thanhVienQuenMatKhau.getPassword()
                                            + "\n\n Email của bạn: " + emailForgot
                                            + "\n\n Bạn vui lòng kiểm tra lại, và thay đổi nếu cần thiết"
                                            + "\n\n Thân! "
                                            + "\n\n Admin" );

                                    Transport.send(message);

                                    System.out.println("Done");
                                    // xử lý thread trong quá trình gửi mail
                                    // sau khi gởi xong thì in ra thông báo
                                    AlertDialog.Builder alertDialog=new AlertDialog.Builder(login.this);
                                    alertDialog.setTitle("Đã gửi mã xác nhận về email");
                                    alertDialog.setMessage("Bạn có muốn kiểm tra ngay ?");
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
                                            //startActivity(new Intent(Intent.ACTION_SEND));
                                        }
                                    });

                                    alertDialog.show();

                                } catch (MessagingException e) {

                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                });

            }
        });
    }

    private User login(String username, String password){
        User u = userDAO.Login(username,password);
        return u;
    }

    private void rememberLogin(String username, String password){

    }
}
