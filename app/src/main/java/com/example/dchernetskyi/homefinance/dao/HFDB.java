package com.example.dchernetskyi.homefinance.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dima on 06.01.2016.
 */
public class HFDB extends SQLiteOpenHelper {

    private static final String dbName = "HomeFinance";
    private static final int dbVersion = 1;

    public static final String table_Name = "USERS";
    public static final String column_ID = "_id";
    public static final String column_NAME = "NAME";
    private static final String dbCreateScript =
            "create table " + table_Name + " ("
            + column_ID + " integer primary key autoincrement,"
            + column_NAME + " text" + ");";

    public HFDB(Context context) {
        super(context, dbName, null, dbVersion);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(dbCreateScript);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
