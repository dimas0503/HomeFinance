package com.example.dchernetskyi.homefinance.hmi;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.dchernetskyi.homefinance.R;
import com.example.dchernetskyi.homefinance.dao.HFDB;
import com.example.dchernetskyi.homefinance.service.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dchernetskyi on 02.01.2016.
 */
public class UserList extends Activity implements OnClickListener{

    private ListView lvUserList;
    private String[] userList;
    private Map<Integer, String> contextMenu;
    private Cursor cursor;
    private SimpleCursorAdapter scAdapter;
    private Service service;
    private HFDB dbHelper;
    private EditText etUserName;
    private String[] from = new String[] {dbHelper.column_NAME};
    private int[] to = new int[]{R.id.etUserListItem};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);

        lvUserList = (ListView) findViewById(R.id.lvUser_List);
        etUserName = (EditText) findViewById(R.id.etUserName);


        contextMenu = new HashMap<>();
        contextMenu.put(1,"add user");
        contextMenu.put(2,"remove user");

        service = new Service(getApplicationContext());
        updateUserList();
        registerForContextMenu(lvUserList);
    }

    private void updateUserList(){
        cursor = service.getUserList();
        scAdapter = new SimpleCursorAdapter(this,R.layout.user_list_item,cursor,from,to,1);
        lvUserList.setAdapter(scAdapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        switch (v.getId()){
            case R.id.lvUser_List:
                menu.add(0,1,0,contextMenu.get(1));
                menu.add(0,2,0,contextMenu.get(2));
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 1:
                new Intent(getApplicationContext(),UserList.class);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddNewUser:
                service.addUser(etUserName.getText().toString());
                etUserName.setText(null);
                updateUserList();
                break;
        }
    }
}
