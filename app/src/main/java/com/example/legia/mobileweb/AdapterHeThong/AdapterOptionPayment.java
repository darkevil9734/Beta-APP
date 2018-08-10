package com.example.legia.mobileweb.AdapterHeThong;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.legia.mobileweb.AdapterSanPham.SanPhamAdapter;
import com.example.legia.mobileweb.CheckInternet.Utils;
import com.example.legia.mobileweb.DAO.hoaDonDAO;
import com.example.legia.mobileweb.DAO.themVaoGioHang;
import com.example.legia.mobileweb.DAO.userDAO;
import com.example.legia.mobileweb.DTO.User;
import com.example.legia.mobileweb.DTO.hoaDon;
import com.example.legia.mobileweb.DTO.payment;
import com.example.legia.mobileweb.DTO.sanPhamMua;
import com.example.legia.mobileweb.MainActivity;
import com.example.legia.mobileweb.PaymentAPI.ConfigVNPay;
import com.example.legia.mobileweb.PaymentAPI.onePayPayment;
import com.example.legia.mobileweb.R;
import com.example.legia.mobileweb.TyGia.DocTyGia;
import com.example.legia.mobileweb.cart;
import com.example.legia.mobileweb.login;
import com.facebook.share.Share;
import com.lib.vtcpay.sdk.ICallBackPayment;
import com.lib.vtcpay.sdk.InitModel;
import com.lib.vtcpay.sdk.PaymentModel;
import com.lib.vtcpay.sdk.VTCPaySDK;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class AdapterOptionPayment extends BaseAdapter implements OnActivityResult {
    private Context context;
    private List<payment> listPayment;

    String paypalClientid = "AUto2kIFoFUBohXTAbmnQICEOAPxW3MZGCilm3LV9A6Yd9JUN-Gd2m_p0kWZTVlsKiE0b3N4N0wAt7Uw";
    int paypalCode = 999;
    PayPalConfiguration configure;
    Intent m_service;
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    String diaChi, quan, phuong = "";
    int soDienThoai = 0;

    themVaoGioHang gioHang = SanPhamAdapter.gioHang;

    public AdapterOptionPayment(Context context, List<payment> listPayment) {
        this.context = context;
        this.listPayment = listPayment;
    }

    @Override
    public int getCount() {
        return listPayment.size();
    }

    @Override
    public Object getItem(int position) {
        return listPayment.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.layout_payment_option,parent,false);
        }
        ImageView imgPayment = convertView.findViewById(R.id.imgPaymentOption);
        imgPayment.setImageResource(listPayment.get(position).getHinhPayment());

        final int maHinhThuc = listPayment.get(position).getMaHinhThuc();
        final SharedPreferences sp = context.getSharedPreferences("userLogin", Context.MODE_PRIVATE);


        convertView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                switch(maHinhThuc){
                    case 1:
                        // Paypal
                        configure = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(paypalClientid);
                        m_service = new Intent(context, PayPalService.class);
                        m_service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configure);
                        context.startService(m_service);

                        //check user login
                        if(sp.getBoolean("isLogin", false)){
                            //
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                                    LayoutInflater li = LayoutInflater.from(context);
                                    final View formXacNhan = li.inflate(R.layout.layout_formxacnhan, null);

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                            context);

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
                                                                    Toast.makeText(context, "Bạn không được bỏ trống", Toast.LENGTH_SHORT).show();
                                                                }else if(phuong.length()==0){
                                                                    Toast.makeText(context, "Bạn không được bỏ trống", Toast.LENGTH_SHORT).show();
                                                                }else if(quan.length()==0){
                                                                    Toast.makeText(context, "Bạn không được bỏ trống", Toast.LENGTH_SHORT).show();
                                                                }else if(txtSoDienThoai.getText().toString().length() ==0){
                                                                    Toast.makeText(context, "Bạn không được bỏ trống", Toast.LENGTH_SHORT).show();
                                                                }
                                                                else{
                                                                    SharedPreferences sharedPreferencesInfo = context.getSharedPreferences("info_khach_hang", Context.MODE_PRIVATE);
                                                                    SharedPreferences.Editor editor = sharedPreferencesInfo.edit();
                                                                    editor.putString("diaChi", diaChi);
                                                                    editor.putString("phuong", phuong);
                                                                    editor.putString("quan", quan);
                                                                    editor.putInt("sdt", soDienThoai);
                                                                    editor.commit();

                                                                    PayPalPayment cart = new PayPalPayment(new BigDecimal(ConvertToUSD(gioHang.tongTien())),"USD","Cart",
                                                                            PayPalPayment.PAYMENT_INTENT_ORDER);

                                                                    Intent intent = new Intent(context, PaymentActivity.class);
                                                                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configure);
                                                                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, cart);
                                                                    ((Activity) context).startActivityForResult(intent, paypalCode);
                                                                }
                                                            }
                                                            catch (NumberFormatException e){
                                                                Toast.makeText(context, "Bạn phải nhập số!", Toast.LENGTH_SHORT).show();

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
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                                    Intent intent = new Intent(context, login.class);
                                    context.startActivity(intent);
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }


                        break;
                    case 2:
                        // Onepay
                        themVaoGioHang gioHang = SanPhamAdapter.gioHang;
                        double tongtien = gioHang.tongTien();
                        DecimalFormat df = new DecimalFormat("###,###.##");
                        Toast.makeText(context, "Tổng tiền: " + df.format(tongtien), Toast.LENGTH_SHORT).show();

                        // Info card owner.
                        String vpc_Version = "2";
                        String vpc_Currency = "VND";
                        String vpc_Command = "pay";
                        String vpc_AccessCode = "D67342C2";
                        String vpc_Merchant = "ONEPAY";
                        String vpc_Locale = "vn";
                        String vpc_ReturnURL = "http://192.168.1.67:8081/test-onepay/dr.jsp";

                        double amout = tongtien*100;
                        // Info bill
                        // Get IP android:
                        String vpc_TicketNo = Utils.getIPAddress(true);
                        String AgainLink = "test.htm";
                        String vpc_OrderInfo   = "HÓA ĐƠN THANH TOÁN:";
                        String vpc_Amount = String.valueOf(amout);

                        Random random = new Random();
                        String vpc_MerchTxnRef = String.valueOf(random.nextInt(1000000));

                        Map<String, String> fields = new HashMap<>();
                        fields.put("vpc_Version", vpc_Version);
                        fields.put("vpc_Currency", vpc_Currency);
                        fields.put("vpc_Command", vpc_Command);
                        fields.put("vpc_OrderInfo", vpc_OrderInfo);
                        fields.put("vpc_AccessCode", vpc_AccessCode);
                        fields.put("vpc_Merchant", vpc_Merchant);
                        fields.put("vpc_Locale", vpc_Locale);
                        fields.put("vpc_MerchTxnRef", vpc_MerchTxnRef);
                        fields.put("vpc_TicketNo", vpc_TicketNo);
                        fields.put("AgainLink", AgainLink);
                        fields.put("vpc_ReturnURL", vpc_ReturnURL);

                        fields.put("vpc_Amount", vpc_Amount);

                        String vpcURL = "https://mtf.onepay.vn/onecomm-pay/vpc.op?";
                        String secureHash = onePayPayment.hashAllFields(fields);
                        fields.put("vpc_SecureHash", secureHash);

                        //StringBuffer buf = new StringBuffer();
                        //buf.append(vpcURL).append('?');

                        //appendQueryFields(buf, fields);
                        String parameter = "vpc_Amount="+vpc_Amount+"&vpc_Version=2&vpc_OrderInfo="+vpc_OrderInfo+"&vpc_Command=pay&vpc_Currency=VND&vpc_Merchant=ONEPAY&Title="+vpc_OrderInfo+"&vpc_ReturnURL="+vpc_ReturnURL+"&AgainLink=http%3A%2F%2Flocalhost%3A8080%2Ftest-onepay%2F&vpc_SecureHash="+fields.get("vpc_SecureHash")+"&vpc_AccessCode=D67342C2&vpc_MerchTxnRef="+vpc_MerchTxnRef+"&vpc_TicketNo="+vpc_TicketNo+"&vpc_Locale=vn";

                        for(Map.Entry<String, String> param : fields.entrySet()){
                            Log.i("Test ", param.getKey() + " : " + param.getValue());
                            //parameter += param.getKey()+"="+param.getValue()+"&&";
                        }


                        String url = vpcURL+parameter;
                        Log.i("testurl", "url: " + url);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        context.startActivity(i);

                        break;
                    /*case 3: //VNPAY
                        String vnp_Version = "2.0.0";
                        String vnp_Command = "pay";
                        String vnp_OrderInfo = "hoadon";
                        //String orderType = req.getParameter("ordertype");
                        String vnp_Merchant = "DEMO";
                        String vnp_TxnRef = ConfigVNPay.getRandomNumber(8);
                        String vnp_IpAddr = Utils.getIPAddress(true);
                        String vnp_ReturnUrl = "http%3a%2f%2fsandbox.vnpayment.vn%2ftryitnow%2fHome%2fVnPayReturn";
                        String vnp_TmnCode = ConfigVNPay.vnp_TmnCode;

                        String vnp_TransactionNo = vnp_TxnRef;
                        String vnp_hashSecret = ConfigVNPay.vnp_HashSecret;

                        themVaoGioHang gioHang1 = SanPhamAdapter.gioHang;
                        double amount = gioHang1.tongTien()*100;

                        Map<String, String> vnp_Params = new HashMap<>();
                        vnp_Params.put("vnp_Version", vnp_Version);
                        //vnp_Params.put("vnp_hashSecret", vnp_hashSecret);
                        vnp_Params.put("vnp_Merchant", vnp_Merchant);
                        vnp_Params.put("vnp_Command", vnp_Command);
                        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
                        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
                        vnp_Params.put("vnp_TransactionNo", vnp_TransactionNo);
                        vnp_Params.put("vnp_Amount", String.valueOf(amount));
                        vnp_Params.put("vnp_CurrCode", "VND");
                        vnp_Params.put("vnp_BankCode", "NCB");
                        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
                        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
                        vnp_Params.put("vnp_OrderType", "topup");
                        vnp_Params.put("vnp_Locale", "vn");
                        vnp_Params.put("vnp_ReturnUrl", ConfigVNPay.vnp_Returnurl);
                        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

                        Date dt = new Date();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                        String dateString = formatter.format(dt);
                        String vnp_CreateDate = dateString;
                        String vnp_TransDate = vnp_CreateDate;
                        vnp_Params.put("vnp_CreateDate", vnp_TransDate);

                        //HASHING
                        //Build data to hash and querystring
                        List fieldNames = new ArrayList(vnp_Params.keySet());
                        Collections.sort(fieldNames);
                        Charset.forName("UTF-8");
                        StringBuilder hashData = new StringBuilder();
                        StringBuilder query = new StringBuilder();
                        Iterator itr = fieldNames.iterator();
                        while (itr.hasNext()) {
                            String fieldName = (String) itr.next();
                            String fieldValue = (String) vnp_Params.get(fieldName);
                            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                                //Build hash data
                                hashData.append(fieldName);
                                hashData.append('=');
                                hashData.append(fieldValue);
                                //Build query
                                query.append(fieldName);
                                query.append('=');
                                query.append(fieldValue);

                                if (itr.hasNext()) {
                                    query.append('&');
                                    hashData.append('&');
                                }

                            }
                        }
                        String queryUrl = query.toString();
                        String vnp_SecureHash = ConfigVNPay.md5(ConfigVNPay.vnp_HashSecret + hashData.toString());
                        //System.out.println("HashData=" + hashData.toString());
                        queryUrl += "&vnp_SecureHashType=MD5&vnp_SecureHash=" + vnp_SecureHash;
                        String paymentUrl = ConfigVNPay.vnp_PayUrl + "?" + queryUrl;
                        *//*String vnp_SecureHash = ConfigVNPay.hashAllFields(vnp_Params);
                        String param = "vnp_Amount="+vnp_Params.get("vnp_Amount")+"&vnp_BankCode=NCB&" +
                                "vnp_Command=pay&vnp_CreateDate="+vnp_Params.get("vnp_CreateDate")+"&vnp_CurrCode=VND&" +
                                "vnp_IpAddr="+vnp_Params.get("vnp_IpAddr")+"&vnp_Locale=vn&vnp_Merchant=DEMO&" +
                                "vnp_OrderInfo="+vnp_Params.get("vnp_OrderInfo")+"&" +
                                "vnp_OrderType=topup&vnp_ReturnUrl=http%3a%2f%2fsandbox.vnpayment.vn%2ftryitnow%2fHome%2fVnPayReturn&" +
                                "vnp_TmnCode=2QXUI4J4&vnp_TxnRef="+vnp_Params.get("vnp_TxnRef")+"&vnp_Version=2&" +
                                "vnp_SecureHashType=MD5&vnp_SecureHash="+vnp_SecureHash;
                        String paymentUrl = "http://sandbox.vnpayment.vn/paymentv2/vpcpay.html?" + param;*//*

                        Log.i("testurl", "url vnpay: " + paymentUrl);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(paymentUrl));
                        context.startActivity(intent);

                        break;*/
                    case 4: //COD

                        break;
                    case 5: // VTC
                        themVaoGioHang gioHang2 = SanPhamAdapter.gioHang;
                        InitModel initModel = new InitModel();
                        initModel.setSandbox(true);//[Required] set enviroment test, default is true
                        initModel.setAmount(gioHang2.tongTien()); //[Required] your amount
                        initModel.setOrderCode("HOA-DON-THANH-TOAN");//[Required] your order code
                        initModel.setAppID(500002825); //[Required] your AppID that registered with VTC
                        initModel.setSecretKey("Vuhoangnguyen1997"); //[Required] your secret key that registered with VTC
                        initModel.setReceiverAccount("0971844698");//[Required] your account
                        initModel.setDescription("Thanh toán hóa đơn"); //[Option] description
                        initModel.setCurrency(VTCPaySDK.VND);//[Option] set currency, default is VND
                        initModel.setDrawableLogoMerchant(R.mipmap.ic_launcher); //[Option] Your logo
                        //initModel.setHiddenForeignBank(cbIsHiddenPaymentForeignBank.isChecked());//[Option] hidden foreign bank
                        //initModel.setHiddenPayVTC(cbIsHiddenPaymentVTCPay.isChecked());//[Option] hidden pay vtc
                        //initModel.setHiddenDomesticBank(cbIsHiddenPaymentDomesticBank.isChecked());//[Option] hidden domestic bank
                        VTCPaySDK.getInstance().setInitModel(initModel); //init model

                        VTCPaySDK.getInstance().payment(context,
                                new ICallBackPayment() {
                                    @Override
                                    public void onPaymentSuccess(PaymentModel paymentModel) {
                                        Toast.makeText(
                                                context,
                                                "payment success transctionID "
                                                        + paymentModel.getTransactionID(),
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onPaymentError(int errorCode, String errorMessage, String bankName) {
                                        Toast.makeText(context,
                                                "Payment error " + errorMessage, Toast.LENGTH_SHORT)
                                                .show();
                                    }

                                    @Override
                                    public void onPaymentCancel() {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(context, "Payment cancel ",
                                                Toast.LENGTH_SHORT).show();
                                    }

                //					@Override
                //					public void onPaymentError(String error) {
                //						// TODO Auto-generated method stub
                //						Toast.makeText(MainActivity.this,
                //								"Payment error " + error, Toast.LENGTH_SHORT)
                //								.show();
                //					}
                                });

                        break;
                }
            }
        });

        return convertView;
    }

    private double ConvertToUSD(double vnd){
        return vnd/ DocTyGia.giaBan();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            PaymentConfirmation confirm = data.getParcelableExtra(
                    PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null){
                try {
                    Log.i("test", confirm.toJSONObject().toString(4));
                    final  SharedPreferences sp = context.getSharedPreferences("userLogin", Context.MODE_PRIVATE);

                    int iduser = sp.getInt("idUser", 0);
                    String details = "", sp_mua = "";
                    DecimalFormat df = new DecimalFormat("###,###.##");

                    for (sanPhamMua spm : gioHang.danhSachSanPhamMua()){
                        details += "Tên sản phẩm: "+ spm.getTenSanPham() + "- Số lượng : " + spm.getSoLuongMua() + "\n" + "Giá 1 cái: " + df.format(spm.getGiaSanPham()) ;
                        sp_mua += "Tên sản phẩm: "+ spm.getTenSanPham() + "- Số lượng : " + spm.getSoLuongMua() +"<br/>" + "Giá 1 cái: " + df.format(spm.getGiaSanPham());
                    }

                    User u = userDAO.readUser(iduser);

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
                    int themHoaDon = hoaDonDAO.themHoaDon(hd);
                    if(themHoaDon != 0){
                        // Clear all of memories
                        context.startActivity(new Intent(context, MainActivity.class));
                        Toast.makeText(context, "Mua thành công!" , Toast.LENGTH_SHORT).show();

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
                                    + "<br/> <br/> Ban Quản Lý, VHN!";
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
}
