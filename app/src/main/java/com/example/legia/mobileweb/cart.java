package com.example.legia.mobileweb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.legia.mobileweb.AdapterSanPham.ListViewGioHangAdapter;
import com.example.legia.mobileweb.AdapterSanPham.SanPhamAdapter;
import com.example.legia.mobileweb.DAO.themVaoGioHang;
import com.example.legia.mobileweb.DTO.sanPham;
import com.example.legia.mobileweb.DTO.sanPhamMua;
import com.example.legia.mobileweb.TyGia.DocTyGia;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class cart extends AppCompatActivity {
    ListView dsGioHang;
    ImageButton btnPay;
    themVaoGioHang gioHang;
    TextView totalMoney;

    String paypalClientid = "AUto2kIFoFUBohXTAbmnQICEOAPxW3MZGCilm3LV9A6Yd9JUN-Gd2m_p0kWZTVlsKiE0b3N4N0wAt7Uw";
    int paypalCode = 999;
    PayPalConfiguration configure;
    Intent m_service;
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Toast.makeText(this, "Giá usd : " + DocTyGia.giaBan(), Toast.LENGTH_SHORT).show();
        dsGioHang = findViewById(R.id.listCart);
        btnPay = findViewById(R.id.btnPay);
        totalMoney = findViewById(R.id.lbTotal);

        gioHang = SanPhamAdapter.gioHang;
        this.setTitle("Giỏ hàng ( " + gioHang.countSoLuongMua() + " sản phẩm )" );

        configure = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(paypalClientid);
        m_service = new Intent(this, PayPalService.class);
        m_service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configure);
        startService(m_service);

        DecimalFormat df = new DecimalFormat("###,###.##");
        totalMoney.setText("Tổng tiền : "+ df.format(gioHang.tongTien())+" VNĐ");

        Log.i("test", DocTyGia.giaBan()+"");
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayPalPayment cart = new PayPalPayment(new BigDecimal(ConvertToUSD(gioHang.tongTien())),"USD","Cart",
                        PayPalPayment.PAYMENT_INTENT_SALE);

                Intent i = new Intent(cart.this, PaymentActivity.class);
                i.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configure);
                i.putExtra(PaymentActivity.EXTRA_PAYMENT, cart);
                startActivityForResult(i, paypalCode);
            }
        });

        List<sanPhamMua> dsSanPhamMua = gioHang.danhSachSanPhamMua();
        ListViewGioHangAdapter gioHangAdapter = new ListViewGioHangAdapter(this, dsSanPhamMua);
        dsGioHang.setAdapter(gioHangAdapter);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return false;
    }

    private double ConvertToUSD(double vnd){
        return vnd/ DocTyGia.giaBan();
    }




}
