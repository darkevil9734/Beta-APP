package com.example.legia.mobileweb.AdapterSanPham;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.legia.mobileweb.DTO.sanPham;
import com.example.legia.mobileweb.Detail;
import com.example.legia.mobileweb.MainActivity;
import com.example.legia.mobileweb.R;

import java.sql.Blob;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.SanPhamViewHolder> {

    private List<sanPham> listSanPham;
    private Context context;

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
        /** Set Value*/
        //final sanPham sp = listSanPham.get(position);
        try {

            Blob b = listSanPham.get(position).getHinh_dai_dien();

            int blobLength = (int) b.length();
            byte[] blobAsBytes = b.getBytes(1, blobLength);
            Bitmap btm = BitmapFactory.decodeByteArray(blobAsBytes,0,blobAsBytes.length);
            //img.setImageBitmap(btm );
            DecimalFormat df = new DecimalFormat("###,###.##");
            holder.imgSanPham.setImageBitmap(btm);
            holder.name.setText(listSanPham.get(position).getTenSanPham());
            holder.price.setText(df.format(listSanPham.get(position).getGiaSanPham())+"đ");
            /*Sự kiện click vào item*/
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int maSanPham = listSanPham.get(position).getMa_san_pham();

                    Intent i = new Intent(context, Detail.class);
                    Bundle b = new Bundle();

                    b.putInt("MaSanPham", maSanPham);
                    i.putExtra("SanPhamChon", b);

                    context.startActivity(i);




                }

            });
        }
        catch (SQLException e){

        }

    }

    @Override
    public int getItemCount() {
        return listSanPham.size();
    }

    public class SanPhamViewHolder extends  RecyclerView.ViewHolder {
        ImageView imgSanPham;
        TextView name;
        TextView price;

        public SanPhamViewHolder(View itemView) {
            super(itemView);
            imgSanPham = itemView.findViewById(R.id.imgSanPham);
            name = itemView.findViewById(R.id.txtTenSanPham);
            price = itemView.findViewById(R.id.txtGiaSanPham);
        }
    }
}
