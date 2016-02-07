package com.example.dchernetskyi.homefinance.hmi;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.dchernetskyi.homefinance.R;
import com.example.dchernetskyi.homefinance.dao.HFDB;
import com.example.dchernetskyi.homefinance.service.Service;

import java.util.HashMap;
import java.util.Map;

import static android.view.View.*;

/**
 * Created by Dima on 07.01.2016.
 */
public class ExpenseList extends Fragment{
    private ListView lvExpenseItems;
    private Button btnAddExpense;
    private Map<Integer, String> contextMenu;
    private Cursor cursor;
    private SimpleCursorAdapter scAdapter;
    private Service service;
    private HFDB dbHelper;
    private EditText etExpenseItem;
    private String[] from = new String[] {dbHelper.column_NAME};
    private int[] to = new int[]{R.id.etExpenseListItem};

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.expense_list, container, false);
        Bundle args = getArguments();
        lvExpenseItems = (ListView) rootView.findViewById(R.id.lvExpenseItemList);
        etExpenseItem = (EditText) rootView.findViewById(R.id.etExpenseItemName);
        Button btnAddnewExItem = (Button) rootView.findViewById(R.id.btnAddNewExpenseItem);
        btnAddnewExItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                service.addExpenseItem(etExpenseItem.getText().toString());
                etExpenseItem.setText(null);
                updateExpenseItemList();
            }
        });
        contextMenu = new HashMap<>();
        contextMenu.put(1,"remove item");

        service = new Service(getContext());
        updateExpenseItemList();
        registerForContextMenu(lvExpenseItems);

        return rootView;
    }


    private void updateExpenseItemList(){
        cursor = service.getExpenseList();
        scAdapter = new SimpleCursorAdapter(getContext(),R.layout.expense_list_item,cursor,from,to,1);
        lvExpenseItems.setAdapter(scAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.expense_journal,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

/*

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        switch (v.getId()){
            case R.id.lvExpenseItemList:
                menu.add(0,1,0,contextMenu.get(1));
                break;
        }
    }

*/
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId() ){
            case R.id.delExpenseItem:
                AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                service.delExpenseItem(acmi.id);
                updateExpenseItemList();
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }
}
