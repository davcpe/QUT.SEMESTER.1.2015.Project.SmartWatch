package com.example.davcpe.health;


import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by davcpe on 3/17/2015.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public int getCount() {
        return 4;
    }

    public Fragment getItem(int position) {
        if(position == 0)
            return new CategoryFrag();
        else if(position == 1)
            return new AddfriendFrag();
        else if(position == 2)
            return new RankFrag();
        else if(position == 3)
            return new InvitationFrag();

        return null;
    }

}
