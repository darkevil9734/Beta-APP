package com.example.legia.mobileweb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.legia.mobileweb.AdapterHeThong.AdapterOptionPayment;
import com.example.legia.mobileweb.AdapterSanPham.ListViewGioHangAdapter;
import com.example.legia.mobileweb.AdapterSanPham.SanPhamAdapter;
import com.example.legia.mobileweb.DAO.hoaDonDAO;
import com.example.legia.mobileweb.DAO.theTichDiemDAO;
import com.example.legia.mobileweb.DAO.themVaoGioHang;
import com.example.legia.mobileweb.DAO.userDAO;
import com.example.legia.mobileweb.DTO.User;
import com.example.legia.mobileweb.DTO.hoaDon;
import com.example.legia.mobileweb.DTO.payment;
import com.example.legia.mobileweb.DTO.sanPhamMua;
import com.example.legia.mobileweb.DTO.theKhachHang;
import com.example.legia.mobileweb.Database.Database;
import com.example.legia.mobileweb.Encryption.encrypt;
import com.example.legia.mobileweb.TyGia.DocTyGia;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import javax.activation.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class cart extends AppCompatActivity {
    ListView dsGioHang;
    ImageButton btnPay;
    Button btnOption;
    themVaoGioHang gioHang;
    TextView totalMoney;


    String paypalClientid = "AUto2kIFoFUBohXTAbmnQICEOAPxW3MZGCilm3LV9A6Yd9JUN-Gd2m_p0kWZTVlsKiE0b3N4N0wAt7Uw";
    int paypalCode = 999;
    PayPalConfiguration configure;
    Intent m_service;
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

    String diaChi, quan, phuong, soDienThoaiMaHoa = "";
    int soDienThoai = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        final  SharedPreferences sp = getSharedPreferences("userLogin", MODE_PRIVATE);

        DecimalFormat df = new DecimalFormat("###,###.##");

        Toast.makeText(this, "Giá usd : " + DocTyGia.giaBan(), Toast.LENGTH_SHORT).show();
        dsGioHang = findViewById(R.id.listCart);
        //btnPay = findViewById(R.id.btnPay);
        totalMoney = findViewById(R.id.lbTotal);
        btnOption = findViewById(R.id.btnOption);
        btnOption.setText(Html.fromHtml("<html>Thanh Toán \t&#x2794;</html>"));

        gioHang = SanPhamAdapter.gioHang;
        this.setTitle("Giỏ hàng ( " + gioHang.countSoLuongMua() + " sản phẩm )" );

        configure = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(paypalClientid);
        m_service = new Intent(this, PayPalService.class);
        m_service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configure);
        startService(m_service);

        final  SharedPreferences sharedPreferences = getSharedPreferences("userLogin", MODE_PRIVATE);

        int iduser = sharedPreferences.getInt("idUser", 0);
        int loaiThe =0;
        double tongTien = gioHang.tongTien();
        double tongTienGiamGia = 0;
        loaiThe = userDAO.layLoaiTheUser(iduser);
        switch (loaiThe){
            case 1: // Copper, normal
                Log.i("test", "thẻ đồng");
                tongTien = tongTien;
                break;
            case 2: // Silver, discount 2%
                Log.i("test", "thẻ bạc");
                tongTienGiamGia = tongTien - tongTien*0.02;
                break;
            case 3: // Gold, discount 5%
                Log.i("test", "thẻ vàng");
                tongTienGiamGia = tongTien - tongTien*0.05;
                /*totalMoney.setText(Html.fromHtml(
                        "<html>Tổng tiền: " + df.format(tongTien) +"</strike><br/>"+
                                "Tiền giảm giá: "+ df.format(tongTienGiamGia) +
                                "</html>"
                ));*/
                break;
            case 4: // Plantium, discount 7%
                Log.i("test", "thẻ bạch kim");
                tongTienGiamGia = tongTien - tongTien*0.07;
                break;
            case 5: // Diamond, discount 10%
                Log.i("test", "thẻ kim cương");
                tongTienGiamGia = tongTien - tongTien*0.1;
                break;

        }

        //
        totalMoney.setText("Tổng tiền : "+ df.format(tongTien)+" VNĐ");

        Log.i("test", DocTyGia.giaBan()+"");

        btnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // option
                List<payment> dsPayment = new ArrayList<>();
                dsPayment.add(new payment(1, R.drawable.paypallogo));
                dsPayment.add(new payment(2, R.drawable.onepaylogo));
                dsPayment.add(new payment(6, R.drawable.momo));
                dsPayment.add(new payment(4, R.drawable.cod));
                dsPayment.add(new payment(5, R.drawable.vtc));

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(cart.this);

                // Set title value.
                builder.setTitle("Chọn hình thức thanh toán: ");

                AdapterOptionPayment adapterOptionPayment = new AdapterOptionPayment(cart.this, dsPayment);
                ListView lv = new ListView(cart.this);

                lv.setAdapter(adapterOptionPayment);

                builder.setView(lv);
                android.support.v7.app.AlertDialog dialog=builder.create();

                dialog.show();



            }
        });

        /*btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sp.getBoolean("isLogin", false)){
                    //
                    AlertDialog.Builder builder = new AlertDialog.Builder(cart.this);
                    builder.setTitle("Bạn có muốn thanh toán ?");
                    builder.setMessage("Bạn có thanh toán bây giờ không?");
                    builder.setCancelable(false);
                    // if choose no
                    builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    // if choose yes, save into db
                    builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Xác thực lại thông tin cá nhân:
                            LayoutInflater li = LayoutInflater.from(cart.this);
                            final View formXacNhan = li.inflate(R.layout.layout_formxacnhan, null);

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    cart.this);

                            // set prompts.xml to alertdialog builder
                            alertDialogBuilder.setView(formXacNhan);


                            // set dialog message
                            alertDialogBuilder
                                    .setTitle("Nhập thông tin giao hàng")
                                    .setCancelable(false)
                                    .setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {

                                                public void onClick(DialogInterface dialog,int id) {
                                                    try{
                                                        final EditText txtDiaChi = formXacNhan.findViewById(R.id.txtDiaChiXacNhan);
                                                        final EditText txtPhuong = formXacNhan.findViewById(R.id.txtPhuongXacNhan);
                                                        final EditText txtQuan = formXacNhan.findViewById(R.id.txtQuanXacNhan);
                                                        final EditText txtSoDienThoai = formXacNhan.findViewById(R.id.txtSoDienThoaiXacNhan);

                                                        diaChi = txtDiaChi.getText().toString();
                                                        phuong = txtPhuong.getText().toString();
                                                        quan = txtQuan.getText().toString();
                                                        soDienThoai = Integer.parseInt(txtSoDienThoai.getText().toString());

                                                        if(diaChi.length()==0){
                                                            Toast.makeText(cart.this, "Bạn không được bỏ trống", Toast.LENGTH_SHORT).show();
                                                        }else if(phuong.length()==0){
                                                            Toast.makeText(cart.this, "Bạn không được bỏ trống", Toast.LENGTH_SHORT).show();
                                                        }else if(quan.length()==0){
                                                            Toast.makeText(cart.this, "Bạn không được bỏ trống", Toast.LENGTH_SHORT).show();
                                                        }else if(txtSoDienThoai.getText().toString().length() ==0){
                                                            Toast.makeText(cart.this, "Bạn không được bỏ trống", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else{
                                                            PayPalPayment cart = new PayPalPayment(new BigDecimal(ConvertToUSD(gioHang.tongTien())),"USD","Cart",
                                                                    PayPalPayment.PAYMENT_INTENT_ORDER);

                                                            Intent intent = new Intent(cart.this, PaymentActivity.class);
                                                            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configure);
                                                            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, cart);
                                                            startActivityForResult(intent, paypalCode);
                                                        }
                                                    }
                                                    catch (NumberFormatException e){
                                                        Toast.makeText(cart.this, "Bạn phải nhập số!", Toast.LENGTH_SHORT).show();

                                                    }

                                                }
                                            })
                                    .setNegativeButton("Cancel",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {
                                                    dialog.cancel();
                                                }
                                            });

                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();

                            // show it
                            alertDialog.show();



                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else{
                    // chưa login
                    AlertDialog.Builder builder = new AlertDialog.Builder(cart.this);
                    builder.setTitle("Bạn chưa đăng nhập?");
                    builder.setMessage("Bạn có đăng nhập bây giờ không?");
                    builder.setCancelable(false);
                    // if choose no go back.
                    builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    // if choose yes go to login.
                    builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(cart.this, login.class);
                            startActivity(intent);
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

               *//* *//*
            }
        });*/

        List<sanPhamMua> dsSanPhamMua = gioHang.danhSachSanPhamMua();
        ListViewGioHangAdapter gioHangAdapter = new ListViewGioHangAdapter(this, dsSanPhamMua);
        dsGioHang.setAdapter(gioHangAdapter);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                int countGioHang = gioHang.countSoLuongMua();
                Intent i = new Intent(this, MainActivity.class);
                i.putExtra("countGioHang", countGioHang);
                startActivity(i);
                overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
                return true;
        }
        return false;
    }


    private double ConvertToUSD(double vnd){
        return vnd/ DocTyGia.giaBan();
    }
    String diaChiGiaiMa, phuongGiaiMa, quanGiaiMa, soDienThoaiGiaiMa = "";
    // After pay with PayPal, get result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            PaymentConfirmation confirm = data.getParcelableExtra(
                    PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null){
                try {
                    Log.i("test", confirm.toJSONObject().toString(4));
                    final  SharedPreferences sp = getSharedPreferences("userLogin", MODE_PRIVATE);

                    int iduser = sp.getInt("idUser", 0);
                    String details = "", sp_mua = "";
                    DecimalFormat df = new DecimalFormat("###,###.##");

                    for (sanPhamMua spm : gioHang.danhSachSanPhamMua()){
                        details += "Tên sản phẩm: "+ spm.getTenSanPham() + "- Số lượng : " + spm.getSoLuongMua() + "\n" + "Giá 1 cái: " + df.format(spm.getGiaSanPham()) ;
                        sp_mua += "Tên sản phẩm: "+ spm.getTenSanPham() + "- Số lượng : " + spm.getSoLuongMua() +"<br/>" + "Giá 1 cái: " + df.format(spm.getGiaSanPham());
                    }

                    User u = userDAO.readUser(iduser);
                    int id_the_tich_diem = u.getId_the_tich_diem();

                    double tongTien = gioHang.tongTien();

                    theKhachHang theKhachHang = theTichDiemDAO.theKhachHang(id_the_tich_diem);
                    int diem = theKhachHang.getDiem();

                    diem+= tongTien/100000;
                    if(diem>=100 && diem <400){
                        // Silver
                        int updateThe = theTichDiemDAO.nangCapThe(id_the_tich_diem, 2);
                    }
                    else if(diem >=400 && diem<700){
                        // Gold
                        int updateThe = theTichDiemDAO.nangCapThe(id_the_tich_diem, 3);

                    }
                    else if(diem>=700 && diem <1000){
                        // Plantium
                        int updateThe = theTichDiemDAO.nangCapThe(id_the_tich_diem, 4);

                    }else if(diem>=1000){
                        // Diamond
                        int updateThe = theTichDiemDAO.nangCapThe(id_the_tich_diem, 5);

                    }

                    int tichDiem = theTichDiemDAO.tichDiem(id_the_tich_diem, diem);

                    String soDienThoaiMaHoa = "";
                    SharedPreferences sharedPreferencesInfo = getSharedPreferences("info_khach_hang", MODE_PRIVATE);
                    diaChi = sharedPreferencesInfo.getString("diaChi", "");
                    phuong = sharedPreferencesInfo.getString("phuong", "");
                    quan = sharedPreferencesInfo.getString("quan", "");
                    soDienThoaiMaHoa = sharedPreferencesInfo.getString("sdt", "");



                    if (diaChi != null) {
                        String[] split = diaChi.substring(1, diaChi.length()-1).split(", ");
                        byte[] array = new byte[split.length];
                        for (int i = 0; i < split.length; i++) {
                            array[i] = Byte.parseByte(split[i]);
                        }
                        diaChi = array.toString();
                        try {
                            diaChiGiaiMa = encrypt.decrypt(array);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (phuong != null) {
                        String[] split = phuong.substring(1, phuong.length()-1).split(", ");
                        byte[] array = new byte[split.length];
                        for (int i = 0; i < split.length; i++) {
                            array[i] = Byte.parseByte(split[i]);
                        }
                        phuong = array.toString();
                        try {
                            phuongGiaiMa = encrypt.decrypt(array);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (quan != null) {
                        String[] split = quan.substring(1, quan.length()-1).split(", ");
                        byte[] array = new byte[split.length];
                        for (int i = 0; i < split.length; i++) {
                            array[i] = Byte.parseByte(split[i]);
                        }
                        quan = array.toString();
                        try {
                            quanGiaiMa = encrypt.decrypt(array);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (soDienThoaiMaHoa != null) {
                        String[] split = soDienThoaiMaHoa.substring(1, soDienThoaiMaHoa.length()-1).split(", ");
                        byte[] array = new byte[split.length];
                        for (int i = 0; i < split.length; i++) {
                            array[i] = Byte.parseByte(split[i]);
                        }
                        try {
                            soDienThoaiGiaiMa = encrypt.decrypt(array);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    soDienThoai = Integer.parseInt(soDienThoaiGiaiMa);



                    // Save to db
                    hoaDon hd = new hoaDon();
                    hd.setId_user(iduser);
                    hd.setTen_user(u.getTen_user());
                    hd.setHo_user(u.getHo_user());
                    hd.setEmail(u.getEmail());
                    hd.setDiaChi(diaChi);
                    hd.setSdt(soDienThoai);
                    hd.setThanhPho(u.getThanh_pho());
                    hd.setPhuong(phuong);
                    hd.setQuan(quan);
                    hd.setChiTiet(details);
                    hd.setHinhThucThanhToan("Thanh toán PayPal");
                    int themHoaDon = hoaDonDAO.themHoaDon(hd);
                    if(themHoaDon != 0){
                        // Clear all of memories
                        startActivity(new Intent(this, MainActivity.class));
                        Toast.makeText(this, "Mua thành công!" , Toast.LENGTH_SHORT).show();

                        // Send bill via email to user
                        final String username = "testmailbaitap@gmail.com";
                        final String password = "Vuhoangnguyen";

                        Properties props = new Properties();
                        props.put("mail.smtp.auth", "true");
                        props.put("mail.smtp.starttls.enable", "true");
                        props.put("mail.smtp.host", "smtp.gmail.com");
                        props.put("mail.smtp.port", "587");

                        Session sessions = Session.getInstance(props,
                                new javax.mail.Authenticator() {
                                    protected PasswordAuthentication getPasswordAuthentication() {
                                        return new PasswordAuthentication(username, password);
                                    }
                                });

                        try {
                            Message message = new MimeMessage(sessions);


                            message.setFrom(new InternetAddress("testmailbaitap@gmail.com"));
                            message.setRecipients(Message.RecipientType.TO,
                                    InternetAddress.parse(hd.getEmail()));
                            message.setHeader("Content-Type", "text/plain; charset=UTF-8");
                            message.setSubject("Thông tin đơn hàng");
                            String noiDung = "Chào bạn, " + hd.getTen_user()
                                    + "<br/> Cảm ơn bạn đã mua hàng của chúng tôi"
                                    + "<br/> Sau đây là chi tiết đơn hàng bạn đã mua: "
                                    + "<br/> <br/>" + " <strong> " + sp_mua + "</strong>"
                                    + "<br/> <br/>" + " <strong> Tổng tiền: " + df.format(gioHang.tongTien()) + " VNĐ</strong>"
                                    + "<br/> <br/>" + "<strong> Địa chỉ giao hàng: " + diaChiGiaiMa + "Phường: " + phuongGiaiMa
                                    + "Quận: " + quanGiaiMa
                                    + "<br/> <br/> Số điện thoại liên hệ: " + soDienThoai
                                    + "<br/> <br/> Ban Quản Lý, VHN! </strong>";
                            //message.setText(noiDung);
                            message.setContent(noiDung, "text/html; charset=utf-8");
                            Transport.send(message);

                        } catch (MessagingException e) {

                            throw new RuntimeException(e);
                        }
                    }



                } catch (JSONException e) {
                    Log.e("test", "no confirmation data: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("test", "The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("test", "Invalid payment / config set");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, PayPalService.class));
    }




}
