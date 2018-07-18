package com.example.legia.mobileweb.AdapterSanPham;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.legia.mobileweb.DAO.themVaoGioHang;
import com.example.legia.mobileweb.DTO.sanPham;
import com.example.legia.mobileweb.DTO.sanPhamMua;
import com.example.legia.mobileweb.R;
import com.example.legia.mobileweb.cart;

import java.sql.Blob;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

public class ListViewGioHangAdapter extends BaseAdapter {
    private Context context;
    private List<sanPhamMua> sp;

    public ListViewGioHangAdapter(Context context, List<sanPhamMua> sp){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.layout_cart,parent,false);
        }
        final sanPhamMua sanPhamMua = (sanPhamMua) this.getItem(position);

        ImageView img = convertView.findViewById(R.id.imgSanPhamMua);
        TextView name =  convertView.findViewById(R.id.txtTenSanPhamMua);
        TextView count =  convertView.findViewById(R.id.txtSoLuongMua);
        TextView price =  convertView.findViewById(R.id.txtGiaMotCai);
        Button delete = convertView.findViewById(R.id.btnXoa);
        DecimalFormat df = new DecimalFormat("###,###.##");

        //BIND
        try {
            name.setText(sanPhamMua.getTenSanPham());
            count.setText("Số lượng mua : " + sanPhamMua.getSoLuongMua());
            price.setText("Giá 1 cái : " + df.format(sanPhamMua.getGiaSanPham()) + " VNĐ");
            Blob b = sanPhamMua.getHinh_dai_dien();

            int blobLength = (int) b.length();
            byte[] blobAsBytes = b.getBytes(1, blobLength);
            Bitmap btm = BitmapFactory.decodeByteArray(blobAsBytes,0,blobAsBytes.length);
            img.setImageBitmap(btm );

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    themVaoGioHang gioHang = SanPhamAdapter.gioHang;
                    gioHang.xoa(sanPhamMua.getMa_san_pham());
                    Intent i = new Intent(context, cart.class);
                    context.startActivity(i);
                }
            });


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, sanPhamMua.getTenSanPham(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch (SQLException e){

        }
        return convertView;
    }
}
