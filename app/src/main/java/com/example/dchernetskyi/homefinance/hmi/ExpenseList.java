package com.example.dchernetskyi.homefinance.hmi;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
public class ExpenseList extends Activity implements OnClickListener{
    private ListView lvExpenseItems;
    private Map<Integer, String> contextMenu;
    private Cursor cursor;
    private SimpleCursorAdapter scAdapter;
    private Service service;
    private HFDB dbHelper;
    private EditText etExpenseItem;
    private String[] from = new String[] {dbHelper.column_NAME};
    private int[] to = new int[]{R.id.etExpenseListItem};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_list);

        service = new Service(this);

        lvExpenseItems = (ListView) findViewById(R.id.lvExpenseItemList);
        etExpenseItem = (EditText) findViewById(R.id.etExpenseItemName);

        contextMenu = new HashMap<>();
        contextMenu.put(1,"remove item");

        updateExpenseItemList();
        registerForContextMenu(lvExpenseItems);
    }

    private void updateExpenseItemList(){
        cursor = service.getExpenseList();
        scAdapter = new SimpleCursorAdapter(this,R.layout.expense_list_item,cursor,from,to,1);
        lvExpenseItems.setAdapter(scAdapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        switch (v.getId()){
            case R.id.lvExpenseItemList:
                menu.add(0,1,0,contextMenu.get(1));
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 1:
                AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                service.delExpenseItem(acmi.id);
                updateExpenseItemList();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddNewExpenseItem:
                service.addExpenseItem(etExpenseItem.getText().toString());
                etExpenseItem.setText(null);
                updateExpenseItemList();
                break;
        }
    }
}
