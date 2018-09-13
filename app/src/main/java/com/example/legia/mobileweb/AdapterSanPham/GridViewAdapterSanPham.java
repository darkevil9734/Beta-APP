package com.example.legia.mobileweb.AdapterSanPham;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.legia.mobileweb.DAO.sanPhamDAO;
import com.example.legia.mobileweb.DTO.sanPham;
import com.example.legia.mobileweb.R;

import java.sql.Blob;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GridViewAdapterSanPham extends BaseAdapter {

    private Context context;
    private List<sanPham> sp;


    public GridViewAdapterSanPham(Context context, List<sanPham> sp){
        this.context = context;
        this.sp = sp;
    }

    @Override
    public int getCount() {
        return sp.size();
    }

    @Override
    public Object getItem(int position) {
        return sp.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.layout_san_pham,parent,false);
        }

        final sanPham SanPham = (sanPham) this.getItem(position);

        ImageView img = (ImageView) convertView.findViewById(R.id.imgSanPham);
        TextView price = (TextView) convertView.findViewById(R.id.txtGiaSanPham);
        TextView name = (TextView) convertView.findViewById(R.id.txtTenSanPham);


        //BIND
        try {
            DecimalFormat df = new DecimalFormat("###,###.##");
            // a
            price.setText(df.format(SanPham.getGiaSanPham())+"đ");
            name.setText(SanPham.getTenSanPham());

            Blob b = SanPham.getHinh_dai_dien();

            int blobLength = (int) b.length();
            byte[] blobAsBytes = b.getBytes(1, blobLength);
            Bitmap btm = BitmapFactory.decodeByteArray(blobAsBytes,0,blobAsBytes.length);
            img.setImageBitmap(btm );



            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, SanPham.getTenSanPham(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch (SQLException e){

        }
        return convertView;
    }
}
