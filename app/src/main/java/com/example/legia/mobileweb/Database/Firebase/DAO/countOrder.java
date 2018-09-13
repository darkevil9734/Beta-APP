package com.example.legia.mobileweb.Database.Firebase.DAO;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.legia.mobileweb.DTO.coupon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class countOrder {
    static DatabaseReference db = FirebaseDatabase.getInstance().getReference("countOrder");

    public static void add(){
        String countID = "count";

        int count = 0;


        // pushing user to 'users' node using the userId
        db.child(countID).setValue(count+1);

    }

    public static void updateValue(){
        final int[] count = {0};

        final DatabaseReference getValue = db.child("count");
        getValue.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count[0] = dataSnapshot.getValue(Integer.class);
                Log.i("count", String.valueOf(count[0]));
                getValue.setValue(count[0]+1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static int getCount(){
        final int[] count = {0};

        final DatabaseReference getValue = db.child("count");
        getValue.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count[0] = dataSnapshot.getValue(Integer.class);
                Log.i("count", String.valueOf(count[0]));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return count[0];
    }



}
