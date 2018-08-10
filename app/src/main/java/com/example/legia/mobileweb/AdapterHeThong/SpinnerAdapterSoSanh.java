package com.example.legia.mobileweb.AdapterHeThong;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import com.example.legia.mobileweb.DTO.giaSpinner;

public class SpinnerAdapterSoSanh extends ArrayAdapter<giaSpinner> {
    LayoutInflater flater;


    public SpinnerAdapterSoSanh(@NonNull Context context, int resource) {
        super(context, resource);
    }
}
