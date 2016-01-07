package com.example.dchernetskyi.homefinance.hmi;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dchernetskyi.homefinance.R;
import com.example.dchernetskyi.homefinance.dao.HFDB;
import com.example.dchernetskyi.homefinance.service.Service;

import static android.widget.AdapterView.*;

/**
 * Created by Dima on 07.01.2016.
 */
public class ExpenseJournal extends Activity implements OnClickListener{

    private Cursor cursorUserList;
    private Cursor cursorExpensesList;
    private Cursor cursorExpensesJournal;

    private SimpleCursorAdapter scAdapterUserList;
    private SimpleCursorAdapter scAdapterExpensesList;
    private SimpleCursorAdapter scAdapterExpensesJournal;

    private Service service;
    private HFDB dbHelper;

    private String[] fromUserList = new String[] {dbHelper.column_NAME};
    private String[] fromExpenseList = new String[] {dbHelper.column_NAME};
    private String[] fromExpenseJournal = new String[] {dbHelper.column_JournalDate,dbHelper.column_JournalUserName,dbHelper.column_JournalExpenseItem,dbHelper.column_JournalSpend};

    private int[] toUserList = new int[]{R.id.spinUserListItem};
    private int[] toExpenseList = new int[]{R.id.spinExpenseListItem};
    private int[] toExpenseJournal = new int[]{R.id.spinJournalDate,R.id.spinJournalUserName,R.id.spinJournalExpenseItem,R.id.spinJournalSpendAmount};

    private Spinner spinnerUserName;
    private Spinner spinnerExpenseItems;
    private CalendarView cvJournalDate;
    private EditText etMoneySpend;
    private ListView lvExpensesJournal;

    private String TAG = "MY_LOG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_journal);

        spinnerUserName = (Spinner) findViewById(R.id.spinUserName);
        spinnerExpenseItems = (Spinner) findViewById(R.id.spinExpenseList);
        cvJournalDate = (CalendarView) findViewById(R.id.calViewJournalDate);
        etMoneySpend = (EditText) findViewById(R.id.etValueSpend);
        lvExpensesJournal = (ListView) findViewById(R.id.lvExpenseJournal);

        service = new Service(getApplicationContext());
        updateUserList();
        updateExpenseList();
        updateExpenseJournal();
    }

    private void updateUserList(){
        cursorUserList = service.getUserList();
        scAdapterUserList = new SimpleCursorAdapter(this,R.layout.expense_journal_spin_user_list_item, cursorUserList, fromUserList, toUserList, 1);
        spinnerUserName.setAdapter(scAdapterUserList);
    }

    private void updateExpenseList(){
        cursorExpensesList = service.getExpenseList();
        scAdapterExpensesList = new SimpleCursorAdapter(this,R.layout.expense_journal_spin_expense_list_item, cursorExpensesList, fromExpenseList, toExpenseList, 1);
        spinnerExpenseItems.setAdapter(scAdapterExpensesList);
    }

    private void updateExpenseJournal(){
        cursorExpensesJournal = service.getExpensesJournal();
        scAdapterExpensesJournal = new SimpleCursorAdapter(this,R.layout.expense_journal_list_item, cursorExpensesJournal, fromExpenseJournal, toExpenseJournal, 1);
        lvExpensesJournal.setAdapter(scAdapterExpensesJournal);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddToJournal:
                String userName = ((TextView) spinnerUserName.getSelectedView().findViewById(R.id.spinUserListItem)).getText().toString();
                String expenseItem = ((TextView) spinnerUserName.getSelectedView().findViewById(R.id.spinUserListItem)).getText().toString();
                Double spendAmount = 0.0;
                if(etMoneySpend != null && etMoneySpend.getText().toString().equals("") == false){
                    spendAmount = Double.parseDouble(etMoneySpend.getText().toString());
                }
                Log.d(TAG, cvJournalDate.getDate() + "|" + etMoneySpend + " | " + " | " + userName + " | " + spendAmount + "");
                service.storeToJournal(cvJournalDate.getDate(),userName,expenseItem,spendAmount);
                etMoneySpend.setText(null);
                updateExpenseJournal();
                break;
        }
    }
}
