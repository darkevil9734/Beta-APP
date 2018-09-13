package com.example.legia.mobileweb.Database.Firebase;


import com.example.legia.mobileweb.DTO.User;
import com.example.legia.mobileweb.DTO.coupon;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FirebaseConnect {
    //CRUD
    // Creating new user node, which returns the unique key value
    // new user node would be /users/$userid/
    static DatabaseReference db = FirebaseDatabase.getInstance().getReference("coupon");

    public static void add(){
        String couponId = "def";

        // creating user object
        coupon coupon = new coupon("abc");

        // pushing user to 'users' node using the userId
        db.child(couponId).setValue(coupon);
    }


}
