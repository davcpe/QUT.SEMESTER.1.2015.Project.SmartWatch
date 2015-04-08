package com.example.davcpe.healthprototypeapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class FriendActivity extends Activity {

    //Explicit
    private  FriendTable objFriendTable;
    private  String[] strListName;
    private TextView txtShowOfficer;
    private  String strMyOfficer,strUser_ID,strUserName,strLat,strLong;
    private Button btnCancel, btnChallenge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        //GetIntentData
        GetIntentData();

        //Bindwidget
        Bindwidget();

        //CreateTable
        objFriendTable = new FriendTable(this);

        //SetUpTxtUserShow
        SetUpTxtUserShow();

        //Synchronize JSON SQLite
        synchronizeJSONtoCoffee();

        //SetUpArray
        SetUpArray();

        //CreateListView
        CreateListView();

        //SetButtonOnclick
        SetButtonOnclick();


    }

    private void GetIntentData() {
        Intent objIntent = getIntent();
        strUser_ID = objIntent.getStringExtra("UserID");


    }//GetIntentData

    private  void  CreateListView(){



        MyAdapter objMyAdapter = new MyAdapter(FriendActivity.this, strListName);
        final ListView objListView = (ListView)findViewById(R.id.CoffeelistView);
        objListView.setAdapter(objMyAdapter);  // Fusion Part with Activity_order and List_view_row

    }

    private void SetUpArray(){

        strListName = objFriendTable.listName(strUser_ID);
    }//SetUpArray


    private void synchronizeJSONtoCoffee(){

        //Change Policy
        if(Build.VERSION.SDK_INT>9){

            StrictMode.ThreadPolicy myPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(myPolicy);
        }//if

        InputStream objInputStream = null;
        String strJSON ="";

        //Create InputStream
        try {

            HttpClient objHttpClient   =  new DefaultHttpClient();
            HttpPost objHttpPost     =  new HttpPost("http://puneethbedre.com/health/php_get_data_Friend.php");
            HttpResponse objHttpResponse =  objHttpClient.execute(objHttpPost);
            HttpEntity objHttpEntity   =  objHttpResponse.getEntity();
            objInputStream = objHttpEntity.getContent();

        }catch (Exception e){
            Log.d("CoffeShop", "Error from InputStram==>" + e.toString());
        }

        //Create str JSON

        try {

            BufferedReader objBufferedReader = new BufferedReader(new InputStreamReader(objInputStream ,"UTF-8"));
            StringBuilder objStringBuilder = new StringBuilder();
            String strLine = null;

            while((strLine = objBufferedReader.readLine()) != null){
                objStringBuilder.append(strLine);
            }//while
            objInputStream.close();
            strJSON = objStringBuilder.toString();

        }catch (Exception e){
            Log.d("CoffeShop","Error Create String==>"+e.toString());
        }

        // Update Value to SQLite

        try {
            final JSONArray objJSONArray =  new JSONArray(strJSON);
            for(int i=0;i< objJSONArray.length();i++){
                JSONObject objJSONObject = objJSONArray.getJSONObject(i);
                String strUserID = objJSONObject.getString("user_id");
                String strFriendName = objJSONObject.getString("friend_name");
                String strFriendID   = objJSONObject.getString("friend_id");
                // CoffeeTABLE objCofeeTable = new CoffeeTABLE(this);

                long insertValueCoffee  = objFriendTable.addValueUser(strUserID,strFriendName,strFriendID);

            }//for


        }catch (Exception e){
            Log.d("CoffeShop","Error Update Value==>"+e.toString());
        }
    }

    private  void SetUpTxtUserShow(){
        strMyOfficer = getIntent().getExtras().getString("UserName");
        txtShowOfficer.setText(strMyOfficer);
    }

    private  void Bindwidget(){

        txtShowOfficer = (TextView)findViewById(R.id.txtShowOfficer);
        btnChallenge   = (Button)findViewById(R.id.buttonChallenge);
        btnCancel      = (Button)findViewById(R.id.buttonCancel);

    }//Bindwidget

    private  void SetButtonOnclick(){
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent = new Intent(FriendActivity.this,FirstPage.class);
                objIntent.putExtra("UserName",strUserName);
                objIntent.putExtra("UserID",strUser_ID);
                objIntent.putExtra("Lat",strLat);
                objIntent.putExtra("Long",strLong);


                startActivity(objIntent);

                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend, menu);
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
