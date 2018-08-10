package com.example.legia.mobileweb.AdapterSanPham;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.legia.mobileweb.DAO.themVaoGioHang;
import com.example.legia.mobileweb.DTO.sanPham;
import com.example.legia.mobileweb.DTO.sanPhamMua;
import com.example.legia.mobileweb.Detail;
import com.example.legia.mobileweb.MainActivity;
import com.example.legia.mobileweb.R;
import com.example.legia.mobileweb.cart;
import com.github.arturogutierrez.Badges;
import com.github.arturogutierrez.BadgesNotSupportedException;

import java.sql.Blob;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.SanPhamViewHolder> {
    private List<sanPham> listSanPham;
    private Context context;
    public static themVaoGioHang gioHang = new themVaoGioHang();

    public SanPhamAdapter(Context context,List<sanPham> listSanPham) {
        this.context = context;
        this.listSanPham = listSanPham;
    }


    @Override
    public SanPhamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_san_pham, parent, false);
        SanPhamViewHolder holder = new SanPhamViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SanPhamViewHolder holder, final int position) {
        try {

            Blob b = listSanPham.get(position).getHinh_dai_dien();

            int blobLength = (int) b.length();
            byte[] blobAsBytes = b.getBytes(1, blobLength);

            Bitmap btm = BitmapFactory.decodeByteArray(blobAsBytes,0,blobAsBytes.length);



            DecimalFormat df = new DecimalFormat("###,###.##");
            holder.imgSanPham.setImageBitmap(btm);
            holder.name.setText(listSanPham.get(position).getTenSanPham());
            holder.price.setText(df.format(listSanPham.get(position).getGiaSanPham())+"đ");


            /*Sự kiện click vào item*/
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int maSanPham = listSanPham.get(position).getMa_san_pham();
                    int soLuongMua = gioHang.countSoLuongMua();

                    Intent i = new Intent(context, Detail.class);
                    Bundle b = new Bundle();

                    b.putInt("MaSanPham", maSanPham);
                    b.putInt("soLuongMua", soLuongMua);
                    i.putExtra("SanPhamChon", b);

                    context.startActivity(i);
                }

            });



            holder.btnMua.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    int maSanPhamMua, soLuongMua = 1;
                    maSanPhamMua = listSanPham.get(position).getMa_san_pham();

                    gioHang.them(maSanPhamMua, soLuongMua);

                    SharedPreferences sp = context.getSharedPreferences("shareGioHang", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("soLuongMua", gioHang.countSoLuongMua());
                    editor.commit();

                    mainNoti();
                    Log.i("count", "Tổng tiền: " + gioHang.tongTien());
                }
            });
        }
        catch (SQLException e){

        }

    }

    public void viewCart(){
        Intent i = new Intent(context, cart.class);
        this.gioHang = gioHang;
        context.startActivity(i);
    }

    public void mainNoti(){
        Intent i = new Intent(context, MainActivity.class);
        int soLuongMua = gioHang.countSoLuongMua();

        Bundle b = new Bundle();
        b.putInt("soLuongMua", soLuongMua);
        i.putExtra("GioHang", b);

        context.startActivity(i);
    }
    @Override
    public int getItemCount() {
        return listSanPham.size();
    }

    public class SanPhamViewHolder extends  RecyclerView.ViewHolder {
        ImageView imgSanPham;
        TextView name;
        TextView price;
        Button btnMua;

        public SanPhamViewHolder(View itemView) {
            super(itemView);
            imgSanPham = itemView.findViewById(R.id.imgSanPham);
            name = itemView.findViewById(R.id.txtTenSanPham);
            price = itemView.findViewById(R.id.txtGiaSanPham);
            btnMua = itemView.findViewById(R.id.btnMua);
        }
    }



}
