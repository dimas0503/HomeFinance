package com.example.dchernetskyi.homefinance.hmi;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dchernetskyi.homefinance.R;
import com.example.dchernetskyi.homefinance.dao.HFDB;
import com.example.dchernetskyi.homefinance.service.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Dima on 07.01.2016.
 */
public class ExpenseJournal extends Fragment {


    private ActionMode actionMode;

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
    private Button btnAddNewExpense;
    private Button btnRemoveExpenseItems;

    private String TAG = "MY_LOG";

    View rootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.expense_journal, container, false);
        Bundle args = getArguments();

        spinnerUserName = (Spinner) rootView.findViewById(R.id.spinUserName);
        spinnerExpenseItems = (Spinner) rootView.findViewById(R.id.spinExpenseList);
        cvJournalDate = (CalendarView) rootView.findViewById(R.id.calViewJournalDate);
        etMoneySpend = (EditText) rootView.findViewById(R.id.etValueSpend);
        lvExpensesJournal = (ListView) rootView.findViewById(R.id.lvExpenseJournal);
        etJournalComment = (TextView) rootView.findViewById(R.id.etComment);

        btnAddNewExpense = (Button) rootView.findViewById(R.id.btnAddToJournal);
        btnRemoveExpenseItems = (Button) rootView.findViewById(R.id.btnRemoveFromJournal);

        btnRemoveExpenseItems.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox;
                for (int i =0; i< lvExpensesJournal.getChildCount(); i++){
                    checkBox = (CheckBox) lvExpensesJournal.getChildAt(i).findViewById(R.id.exJournalCheckBox);
                    if (checkBox.isChecked()){
                        Log.d(TAG,i+" "+checkBox+"|"+lvExpensesJournal.getAdapter().getItemId(i));
                        service.delJournalRecord(lvExpensesJournal.getAdapter().getItemId(i));
                        updateExpenseJournal();
                    }
                }
            }
        });

        btnAddNewExpense.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean canInsert = true;
                String userName = ((TextView) spinnerUserName.getSelectedView().findViewById(R.id.spinUserListItem)).getText().toString();
                String expenseItem = ((TextView) spinnerExpenseItems.getSelectedView().findViewById(R.id.spinExpenseListItem)).getText().toString();
                Double spendAmount = null;
                if(etMoneySpend != null && etMoneySpend.getText().toString().equals("") == false){
                    spendAmount = Double.parseDouble(etMoneySpend.getText().toString());
                }else {
                    Toast.makeText(getContext(),"fill money spend",Toast.LENGTH_SHORT).show();
                    return;
                }
                String comment = etJournalComment.getText().toString();

                Log.d(TAG, cvJournalDate.getDate() + "|" + etMoneySpend + " | " + " | " + userName + " | " + spendAmount + "");
                service.storeToJournal(cvJournalDate.getDate(), userName, expenseItem, spendAmount,comment);
                Toast.makeText(getContext(),"Record added",Toast.LENGTH_SHORT).show();
                etMoneySpend.setText(null);
                etJournalComment.setText(null);
                updateExpenseJournal();
            }
        });


        contextMenu = new HashMap<>();
        contextMenu.put(1, "remove record");

        service = new Service(getContext());
        updateUserList();
        updateExpenseList();
        updateExpenseJournal();
        registerForContextMenu(lvExpensesJournal);

        return rootView;
    }

    private void updateUserList(){
        cursorUserList = service.getUserList();
        scAdapterUserList = new SimpleCursorAdapter(getContext(),R.layout.expense_journal_spin_user_list_item, cursorUserList, fromUserList, toUserList, 1);
        spinnerUserName.setAdapter(scAdapterUserList);
    }

    private void updateExpenseList(){
        cursorExpensesList = service.getExpenseList();
        scAdapterExpensesList = new SimpleCursorAdapter(getContext(),R.layout.expense_journal_spin_expense_list_item, cursorExpensesList, fromExpenseList, toExpenseList, 1);
        spinnerExpenseItems.setAdapter(scAdapterExpensesList);
    }

    private void updateExpenseJournal() {
        lvExpensesJournal.setAdapter(service.getExpensesJournal());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.expense_journal,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.deleteEJItem:
                AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                service.delJournalRecord(acmi.id);
                updateExpenseJournal();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
