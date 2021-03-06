package com.example.dchernetskyi.homefinance.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dima on 06.01.2016.
 */
//todo lear upgrade
public class HFDB extends SQLiteOpenHelper {

    private static final String dbName = "HomeFinance";
    private static final int dbVersion = 1;

    public static final String column_ID = "_id"; // for all tables

    //User table
    public static final String table_Users = "USERS";
    public static final String column_NAME = "NAME";

    //Expense items table
    public static final String table_ExpenseItems = "EXPENSE_ITEMS";
    public static final String column_ExpenseItemName = "NAME";

    //Expense journal table
    public static final String table_Journal = "JOURNAL";
    public static final String column_JournalDate = "DATE";
    public static final String column_JournalUserName = "USER";
    public static final String column_JournalExpenseItem = "EXPENSE_ITEM";
    public static final String column_JournalSpend = "AMOUNT_SPEND";
    public static final String column_JournalComment = "COMMENT";

    //Create scripts
    private static final String createUserTable =
            "create table " + table_Users + " ("
            + column_ID + " integer primary key autoincrement,"
            + column_NAME + " text" + ");";
    private static final String createExpensesTable =
            "create table " + table_ExpenseItems + " ("
            + column_ID + " integer primary key autoincrement,"
            + column_NAME + " text"
            + ");";

    private static final String createJournalTable =
            "create table " + table_Journal + " ("
            + column_ID + " integer primary key autoincrement,"
            + column_JournalDate + " integer,"
            + column_JournalUserName + " text,"
            + column_JournalExpenseItem + " text,"
            + column_JournalSpend + " real,"
            + column_JournalComment + " text"
            + ");";

    public HFDB(Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createUserTable);
        db.execSQL(createExpensesTable);
        db.execSQL(createJournalTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
