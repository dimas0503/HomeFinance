package com.example.dchernetskyi.homefinance.hmi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.dchernetskyi.homefinance.R;

public class MainActivity extends Activity{
    private String LOG_TAG = "myLog";
    private ListView item_list;
    private String[] menus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        item_list = (ListView) findViewById(R.id.main_list);
        menus = getResources().getStringArray(R.array.menu_items);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,menus);
        item_list.setAdapter(adapter);

        item_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG,position+"");
                switch (position){
                    case 0:
                        startActivity(new Intent(getApplicationContext(),UserList.class));
                        break;
                    case 1:
                        startActivity(new Intent(getApplicationContext(),ExpenseList.class));
                        break;
                    case 2:
                        startActivity(new Intent(getApplicationContext(),ExpenseJournal.class));
                        break;
                }
            }
        });
    }
}
