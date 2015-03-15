package com.example.davcpe.healthapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class AddfriendActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfriend);
    }// Oncreate

    public void CategoryPage(View view) {

        Intent objIntent = new Intent(AddfriendActivity.this,CategoryActivity.class);
        startActivity(objIntent);
        finish();

    }
    public void AddfriendPage(View view) {
        Intent objIntent = new Intent(AddfriendActivity.this,AddfriendActivity.class);
        startActivity(objIntent);
        finish();

    }
    public void rankPage(View view) {
        Intent objIntent = new Intent(AddfriendActivity.this,RankActivity.class);
        startActivity(objIntent);
        finish();

    }
    public void invitationPage(View view) {
        Intent objIntent = new Intent(AddfriendActivity.this,InviationActivity.class);
        startActivity(objIntent);
        finish();

    }
    public void homePage(View view) {
        Intent objIntent = new Intent(AddfriendActivity.this,HomeActivity.class);
        startActivity(objIntent);
        finish();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_addfriend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
