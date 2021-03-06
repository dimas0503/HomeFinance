package com.example.dchernetskyi.homefinance.hmi;

import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
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

/**
 * Created by dchernetskyi on 02.01.2016.
 */
public class UserList extends Fragment{

    private ListView lvUserList;
    private Map<Integer, String> contextMenu;
    private Cursor cursor;
    private SimpleCursorAdapter scAdapter;
    private Service service;
    private HFDB dbHelper;
    private EditText etUserName;
    private String[] from = new String[] {dbHelper.column_NAME};
    private int[] to = new int[]{R.id.etUserListItem};


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_list, container, false);
        Bundle args = getArguments();
        lvUserList = (ListView) rootView.findViewById(R.id.lvUser_List);
        etUserName = (EditText) rootView.findViewById(R.id.etUserName);
        Button btnAddNewUser = (Button) rootView.findViewById(R.id.btnAddNewUser);
        btnAddNewUser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                service.addUser(etUserName.getText().toString());
                etUserName.setText(null);
                updateUserList();
            }
        });
        contextMenu = new HashMap<>();
        contextMenu.put(1,"remove user");

        service = new Service(getContext());
        updateUserList();
        registerForContextMenu(lvUserList);

        return rootView;
    }

    private void updateUserList(){
        cursor = service.getUserList();
        scAdapter = new SimpleCursorAdapter(getContext(),R.layout.user_list_item,cursor,from,to,1);
        lvUserList.setAdapter(scAdapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        switch (v.getId()){
            case R.id.lvUser_List:
                menu.add(0, 1, 0, contextMenu.get(1));
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 1:
                AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                service.delUser(acmi.id);
                updateUserList();
                return true;
        }
        return super.onContextItemSelected(item);
    }
}
