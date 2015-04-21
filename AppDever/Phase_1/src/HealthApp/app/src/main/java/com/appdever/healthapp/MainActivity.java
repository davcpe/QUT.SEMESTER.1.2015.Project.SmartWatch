package com.appdever.healthapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import adapters.MenuBarListAdapter;
import adapters.MenuBarListAdapter.OnMGListAdapterAdapterListener;
import config.UIConfig;
import models.Menu;
import models.Menu.HeaderType;


public class MainActivity extends FragmentActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    // nav drawer title
    private CharSequence mDrawerTitle;
    // used to store app title
    private CharSequence mTitle;
    private Menu[] MENUS;

    private Fragment currFragment;

    SQLiteDatabase mDb;
    MyDbHelper mHelper;
    Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkDatabase();
        //mTitle = mDrawerTitle = "";
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        updateMenuList();

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_action_menu, //nav menu toggle icon
                R.string.no_name, // nav drawer open - description for accessibility
                R.string.no_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mHelper = new MyDbHelper(MainActivity.this);
        mDb = mHelper.getWritableDatabase();

        mCursor = mDb.rawQuery("SELECT * FROM " + MyDbHelper.TB_SLEEP, null);
        mCursor.moveToFirst();

        long get_start_h = mCursor.getInt(mCursor.getColumnIndex(MyDbHelper.COL_START_H));
        long get_start_m = mCursor.getInt(mCursor.getColumnIndex(MyDbHelper.COL_START_M));
        long get_start_s = mCursor.getInt(mCursor.getColumnIndex(MyDbHelper.COL_START_S));


        if(get_start_h!=0||get_start_m!=0||get_start_s!=0){
            displayView(3);
        }else{
            displayView(1);
        }



    }

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            updateMenuList();
            return true;
        }else{
            return false;
        }

    }



    @Override
    public boolean onPrepareOptionsMenu(android.view.Menu menu) {
        // if nav drawer is opened, hide the action items
//        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }


    public void displayView(int position) {

        // clear back stack
        FragmentManager fm = this.getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {

            case 1:
                fragment = new WalkingFragment();
                break;
            case 2:
                fragment = new CyclingFragment();
                break;
            case 3:
                fragment = new SleepFragment();
                break;
            case 5:
                fragment = new FriendFragment();
                break;
            case 6:
                fragment = new RankFragment();
                break;
            case 8:
                fragment = new HistoryFragment();
                break;

            default:
                break;
        }

        // update selected item and title, then close the drawer
       try{
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
//        setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        }catch (Exception e){
           Log.e("Side bar",e.toString());
       }
        if(currFragment != null && fragment != null) {
            boolean result = fragment.getClass().equals( currFragment.getClass());
            if(result)
                return;
        }

        if (fragment != null) {

            if(fragment instanceof CyclingFragment) {
                currFragment = fragment;
                Handler h = new Handler();
                h.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_container, currFragment).commit();
                    }
                }, 500);
            }
            else {

                currFragment = fragment;
                FragmentManager fragmentManager = this.getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment).commit();
            }

        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }


    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);


    }

    public void updateMenuList() {
        MENUS = UIConfig.MENUS_BAR;
        showList();
    }

    public void showList() {

        MenuBarListAdapter adapter = new MenuBarListAdapter(
                this, MENUS.length, R.layout.menu_entry);

        adapter.setOnMGListAdapterAdapterListener(new OnMGListAdapterAdapterListener() {

            @Override
            public void OnMGListAdapterAdapterCreated(MenuBarListAdapter adapter, View v,
                                                      int position, ViewGroup viewGroup) {
                // TODO Auto-generated method stub

                FrameLayout frameCategory = (FrameLayout) v.findViewById(R.id.frameCategory);
                FrameLayout frameHeader = (FrameLayout) v.findViewById(R.id.frameHeader);

                frameCategory.setVisibility(View.GONE);
                frameHeader.setVisibility(View.GONE);

                Menu menu = MENUS[position];

                if(menu.getHeaderType() == HeaderType.HeaderType_CATEGORY) {
                    frameCategory.setVisibility(View.VISIBLE);
                    Spanned title = Html.fromHtml(MainActivity.this.getResources().getString(menu.getMenuResTitle()));
                    TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
                    tvTitle.setText(title);

                    ImageView imgViewIcon = (ImageView) v.findViewById(R.id.imgViewIcon);
                    imgViewIcon.setImageResource(menu.getMenuResIconSelected());
                }
                else {
                    frameHeader.setVisibility(View.VISIBLE);

                    Spanned title = Html.fromHtml(MainActivity.this.getResources().getString(menu.getMenuResTitle()));
                    TextView tvTitleHeader = (TextView) v.findViewById(R.id.tvTitleHeader);
                    tvTitleHeader.setText(title);
                }
            }
        });
        mDrawerList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void checkDatabase(){
        String url = "/data/data/" + getPackageName() + "/databases/HealthApp";
        File f = new File(url);
        if(!f.exists()) {
            try {
                mHelper = new MyDbHelper(this);
                mDb = mHelper.getWritableDatabase();
                mDb.close();
                mHelper.close();
                InputStream in = getAssets().open("HealthApp");
                OutputStream out = new FileOutputStream(url);
                byte[] buffer = new byte[in.available()];
                in.read(buffer);
                out.write(buffer, 0, buffer.length);
                in.close();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
