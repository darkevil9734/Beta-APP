package com.example.legia.mobileweb.Database.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class BillDB extends SQLiteOpenHelper{
    private static final String TABLE_NAME = "Bill";
    private static final String DATABASE_NAME = "BillDB";

    public BillDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    private static final String BILL_ID = "id";
    private static final String BILL_NAME = "soLuongMua";

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+ TABLE_NAME +
                "(" +
                BILL_ID + " INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                BILL_NAME + " TEXT  NOT NULL" +
                ")";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertData(int id, int soluong){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO " + TABLE_NAME  +  " VALUES (?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindDouble(1, id);
        statement.bindDouble(2, soluong);

        statement.executeInsert();
    }
}
