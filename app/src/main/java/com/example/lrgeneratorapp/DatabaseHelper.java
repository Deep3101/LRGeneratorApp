package com.example.lrgeneratorapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "lr_database.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE lr_history (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "lr_no TEXT," +
                "date TEXT," +
                "consignor TEXT," +
                "consignee TEXT," +
                "from_city TEXT," +
                "to_city TEXT," +
                "truck_no TEXT," +
                "invoice_no TEXT," +
                "pdf_path TEXT" +
                ")";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS lr_history");
        onCreate(db);

    }

    public void insertLRHistory(
            String lrNo,
            String date,
            String consignor,
            String consignee,
            String fromCity,
            String toCity,
            String truck,
            String invoice,
            String pdfPath
    ) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("lr_no", lrNo);
        values.put("date", date);
        values.put("consignor", consignor);
        values.put("consignee", consignee);
        values.put("from_city", fromCity);
        values.put("to_city", toCity);
        values.put("truck_no", truck);
        values.put("invoice_no", invoice);
        values.put("pdf_path", pdfPath);

        db.insert("lr_history", null, values);

        db.close();
    }

    public ArrayList<LRHistory> getAllHistory() {

        ArrayList<LRHistory> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM lr_history ORDER BY id DESC", null);

        while (cursor.moveToNext()) {

            String lrNo = cursor.getString(1);
            String date = cursor.getString(2);
            String consignor = cursor.getString(3);
            String consignee = cursor.getString(4);
            String fromCity = cursor.getString(5);
            String toCity = cursor.getString(6);
            String truck = cursor.getString(7);
            String invoice = cursor.getString(8);
            String pdfPath = cursor.getString(9);

            list.add(new LRHistory(lrNo, date, consignor, consignee, fromCity, toCity, truck, invoice, pdfPath));
        }

        cursor.close();

        return list;
    }

    public void deleteLR(String lrNo){

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("lr_history","lr_no=?",new String[]{lrNo});

    }
}