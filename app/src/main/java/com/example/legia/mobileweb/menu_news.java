package com.example.legia.mobileweb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.legia.mobileweb.AdapterSanPham.ListViewNewsProvider;
import com.example.legia.mobileweb.DTO.newsProvider;

import java.util.ArrayList;
import java.util.List;

public class menu_news extends AppCompatActivity {
    ListView listNewsProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_news);

        this.setTitle("Thông tin online");

        listNewsProvider = findViewById(R.id.listNewsProvider);
        final List<newsProvider> listProvider = new ArrayList<>();
        listProvider.add(new newsProvider(R.drawable.vietcombank, "Xem tỉ giá", "https://www.vietcombank.com.vn/ExchangeRates/ExrateXML.aspx"));
        listProvider.add(new newsProvider(R.drawable.vnexpress, "VnExpress", "https://vnexpress.net/rss/so-hoa.rss"));
        listProvider.add(new newsProvider(R.drawable.thanhnien, "ThanhNien", "https://thanhnien.vn/rss/cong-nghe-thong-tin.rss"));
        listProvider.add(new newsProvider(R.drawable.vietnamnet, "VietNamNet", "http://vietnamnet.vn/rss/cong-nghe.rss"));
        listProvider.add(new newsProvider(R.drawable.ict, "ICT News", "http://ictnews.vn/rss/the-gioi-so/di-dong"));
        listProvider.add(new newsProvider(R.drawable.tinhte, "Tinh Tế", "https://tinhte.vn/rss"));

        ListViewNewsProvider adapterNews = new ListViewNewsProvider(menu_news.this, listProvider);
        listNewsProvider.setAdapter(adapterNews);

        listNewsProvider.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    Intent i1 = new Intent(menu_news.this, tygia.class);
                    Bundle b1 = new Bundle();

                    b1.putString("urlProvider", listProvider.get(position).getUrlProvider());

                    i1.putExtra("news", b1);

                    startActivity(i1);
                    Toast.makeText(menu_news.this, "Vietcombank", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent i = new Intent(menu_news.this, News.class);
                    Bundle b = new Bundle();

                    b.putString("urlProvider", listProvider.get(position).getUrlProvider());

                    i.putExtra("news", b);

                    startActivity(i);
                }
            }
        });

    }
}
