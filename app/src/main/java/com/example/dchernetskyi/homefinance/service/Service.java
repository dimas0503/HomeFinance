package com.example.dchernetskyi.homefinance.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.dchernetskyi.homefinance.dao.HFDB;

/**
 * Created by Dima on 06.01.2016.
 */
public class Service {
    private HFDB dbHelper;
    private SQLiteDatabase db;
    private Context context;

    public Service(Context context) {
        this.context = context;
        dbHelper = new HFDB(context);
    }

    private void openRWDatabase(){
        try {
            db.isOpen();
        }catch (NullPointerException e){
            db = dbHelper.getWritableDatabase();
        }
    }

    private void openRDatabase(){
        try {
            db.isOpen();
        }catch (NullPointerException e){
            db = dbHelper.getReadableDatabase();
        }
    }

    private void closeDB(){
        if(db != null){
            db.close();
        }
    }

    public void addUser(String name){
        openRWDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbHelper.column_NAME, name);
        db.insert(dbHelper.table_Name, null, cv);
    }

    public void delUser(long id){
        openRWDatabase();
        db.delete(dbHelper.table_Name,dbHelper.column_ID + "=" + id,null);
    }

    public Cursor getUserList(){
        openRWDatabase();
        return db.query(dbHelper.table_Name,null,null,null,null,null,null,null);
    }
}
