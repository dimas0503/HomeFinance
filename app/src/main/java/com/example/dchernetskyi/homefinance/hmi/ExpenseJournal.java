package com.example.dchernetskyi.homefinance.hmi;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dchernetskyi.homefinance.R;
import com.example.dchernetskyi.homefinance.dao.HFDB;
import com.example.dchernetskyi.homefinance.service.Service;

import java.util.HashMap;
import java.util.Map;

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
    private int[] toExpenseJournal = new int[]{R.id.tvJournalDate,R.id.tvJournalUserName,R.id.tvJournalExpenseItem,R.id.tvJournalSpendAmount};

    private Map<Integer, String> contextMenu;
    private Spinner spinnerUserName;
    private Spinner spinnerExpenseItems;
    private CalendarView cvJournalDate;
    private EditText etMoneySpend;
    private ListView lvExpensesJournal;
    private TextView etJournalComment;

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
        etJournalComment = (TextView) findViewById(R.id.etComment);

        contextMenu = new HashMap<>();
        contextMenu.put(1,"remove record");

        service = new Service(getApplicationContext());
        updateUserList();
        updateExpenseList();
        updateExpenseJournal();
        registerForContextMenu(lvExpensesJournal);
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
        lvExpensesJournal.setAdapter(service.getExpensesJournal());

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        switch (v.getId()){
            case R.id.lvExpenseJournal:
                menu.add(0,1,0,contextMenu.get(1));
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 1:
                AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                service.delJournalRecord(acmi.id);
                updateExpenseJournal();
                return true;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddToJournal:
                boolean canInsert = true;
                String userName = ((TextView) spinnerUserName.getSelectedView().findViewById(R.id.spinUserListItem)).getText().toString();
                String expenseItem = ((TextView) spinnerExpenseItems.getSelectedView().findViewById(R.id.spinExpenseListItem)).getText().toString();
                Double spendAmount = null;
                if(etMoneySpend != null && etMoneySpend.getText().toString().equals("") == false){
                    spendAmount = Double.parseDouble(etMoneySpend.getText().toString());
                }else {
                    Toast.makeText(this,"fill money spend",Toast.LENGTH_SHORT).show();
                    return;
                }
                String comment = etJournalComment.getText().toString();

                Log.d(TAG, cvJournalDate.getDate() + "|" + etMoneySpend + " | " + " | " + userName + " | " + spendAmount + "");
                service.storeToJournal(cvJournalDate.getDate(), userName, expenseItem, spendAmount,comment);
                Toast.makeText(this,"Record added",Toast.LENGTH_SHORT).show();
                etMoneySpend.setText(null);
                etJournalComment.setText(null);
                updateExpenseJournal();
                break;
        }
    }
}
