package com.example.davcpe.health;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

/**
 * Created by davcpe on 3/17/2015.
 */
public class HomeActivity extends FragmentActivity {
ViewPager pager;
MyPagerAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);


    }

    public void CategoryPage(){
        pager.setCurrentItem(0);
    }
    public void AddfriendPage(){
        pager.setCurrentItem(1);
    }
    public void rankPage(){
        pager.setCurrentItem(2);
    }
    public void invitationPage(){
        pager.setCurrentItem(3);
    }



}
