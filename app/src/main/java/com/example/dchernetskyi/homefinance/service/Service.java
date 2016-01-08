package com.example.dchernetskyi.homefinance.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SimpleCursorAdapter;

import com.example.dchernetskyi.homefinance.dao.HFDB;

/**
 * Created by Dima on 06.01.2016.
 */
public class Service {
    private HFDB dbHelper;
    private SQLiteDatabase db;
    private Context context;
    Cursor cursor;
    JournalListViewCursorAdapter journalCursorAdapter;

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
        db.insert(dbHelper.table_Users, null, cv);
    }

    public void addExpenseItem(String name){
        openRWDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbHelper.column_ExpenseItemName, name);
        db.insert(dbHelper.table_ExpenseItems, null, cv);
    }


    public void delUser(long id){
        openRWDatabase();
        db.delete(dbHelper.table_Users, dbHelper.column_ID + "=" + id, null);
    }

    public void delExpenseItem(long id){
        openRWDatabase();
        db.delete(dbHelper.table_ExpenseItems, dbHelper.column_ID + "=" + id, null);
    }

    public JournalListViewCursorAdapter getExpensesJournal(){
        openRWDatabase();
        cursor = db.query(dbHelper.table_Journal,null,null,null,null,null,dbHelper.column_ID + " DESC");
        journalCursorAdapter = new JournalListViewCursorAdapter(context,cursor,true);
        journalCursorAdapter.setDbHelper(dbHelper);
        return journalCursorAdapter;
    }



    public void storeToJournal(Long date, String uName, String expItem, Double spendAm, String comment){
        openRWDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbHelper.column_JournalDate,date);
        cv.put(dbHelper.column_JournalUserName,uName);
        cv.put(dbHelper.column_JournalExpenseItem,expItem);
        cv.put(dbHelper.column_JournalSpend,spendAm);
        cv.put(dbHelper.column_JournalComment,comment);
        db.insert(dbHelper.table_Journal, null, cv);
    }

    public Cursor getUserList(){
        openRWDatabase();
        return db.query(dbHelper.table_Users,null,null,null,null,null,null,null);
    }

    public Cursor getExpenseList(){
        openRWDatabase();
        return db.query(dbHelper.table_ExpenseItems,null,null,null,null,null,null,null);
    }
}
