package com.example.dchernetskyi.homefinance.hmi;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;

import com.example.dchernetskyi.homefinance.R;

import java.util.zip.Inflater;

public class MainActivity extends FragmentActivity{
    private String LOG_TAG = "myLog";
    private ListView item_list;
    private String[] menus;

    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.main_menu);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
    }
}
