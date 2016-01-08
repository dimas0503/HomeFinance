package com.example.dchernetskyi.homefinance.service;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CursorAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dchernetskyi.homefinance.R;
import com.example.dchernetskyi.homefinance.dao.HFDB;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dima on 08.01.2016.
 */
public class JournalListViewCursorAdapter extends CursorAdapter {

    private HFDB dbHelper;

    public JournalListViewCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public void setDbHelper(HFDB dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.expense_journal_list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        //Get columns to fill
        TextView tvDate = (TextView) view.findViewById(R.id.tvJournalDate);
        TextView tvUserName = (TextView) view.findViewById(R.id.tvJournalUserName);
        TextView tvExpenseItem = (TextView) view.findViewById(R.id.tvJournalExpenseItem);
        TextView tvSpendAmount = (TextView) view.findViewById(R.id.tvJournalSpendAmount);
        TextView tvComment = (TextView) view.findViewById(R.id.tvJournalComment);

        //fill columns with data from cursor
        Date date = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(dbHelper.column_JournalDate)));
        tvDate.setText(dateFormat.format(date));
        tvUserName.setText(cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.column_JournalUserName)));
        tvExpenseItem.setText(cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.column_JournalExpenseItem)));
        tvSpendAmount.setText(cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.column_JournalSpend)));
        tvComment.setText(cursor.getString(cursor.getColumnIndexOrThrow(dbHelper.column_JournalComment)));
    }
}
