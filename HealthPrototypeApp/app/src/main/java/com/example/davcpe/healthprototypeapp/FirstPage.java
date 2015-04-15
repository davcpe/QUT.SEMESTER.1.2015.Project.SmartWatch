package com.example.davcpe.healthprototypeapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;




public class FirstPage extends ActionBarActivity {

    private Button btnAct,btnRank,btnFriend,btnChallengeHistory;
    private ImageView imgNotification;
    private  String user_name,user_id,strlattitude,strlongtitude;
    private  Double currentlat,currentLong;
    GPSTracker objGpsTracker = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        //GetIntentData
        GetIntentData();


        //BindWidget
        BindWidget();

        //OnclickAction
        OnclickAction();

    }

    private void GetIntentData() {
        Intent objIntent = getIntent();
        user_id = objIntent.getStringExtra("UserID");
        user_name =objIntent.getStringExtra("UserName");


        strlattitude=objIntent.getStringExtra("Lat");
        if(strlattitude == null){
            currentlat = objGpsTracker.getLatitude();
            strlattitude = String.valueOf(currentlat);
        }
        else if(strlattitude != null){
            strlattitude = objIntent.getStringExtra("Lat");
        }


        strlongtitude=objIntent.getStringExtra("Long");
        if(strlongtitude == null){
            currentLong = objGpsTracker.getLongitude();
            strlongtitude = String.valueOf(currentLong);
        }
        else if(strlongtitude != null){
            strlongtitude = objIntent.getStringExtra("Long");
        }


    }//GetIntentData


    private void BindWidget() {
        //Button
        btnAct =(Button)findViewById(R.id.buttonAct);
        btnRank =(Button)findViewById(R.id.buttonRank);
        btnFriend =(Button)findViewById(R.id.buttonFriend);
        btnChallengeHistory=(Button)findViewById(R.id.buttonChallenge);

        //Image
        imgNotification = (ImageView)findViewById(R.id.imageNotification);

        //GPS
        objGpsTracker = new GPSTracker(FirstPage.this);

    }//BindWidget


    private void OnclickAction() {
        btnAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent = new Intent(FirstPage.this,HomeActivity.class);
                objIntent.putExtra("UserName",user_name);
                objIntent.putExtra("UserID",user_id);
                objIntent.putExtra("Lat",strlattitude);
                objIntent.putExtra("Long",strlongtitude);

                startActivity(objIntent);
                finish();
            }
        }); //Click Activity Button

    }//OnclickAction


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first_page, menu);
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
