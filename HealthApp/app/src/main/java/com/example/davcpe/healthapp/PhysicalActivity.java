package com.example.davcpe.healthapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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


public class PhysicalActivity extends Activity {

    //Explicit
    private CategoryTABLE objCategoryTABLE;
    private  String[] strListCategoryName;
    private  int[]myTarget;
    private TextView txtShowOfficer;
    private  String strMyofficer, strMyCategoryName, strItems ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical);

        //Bind Widget
        bindWidget();

        objCategoryTABLE = new CategoryTABLE(this);

        //SetUp Text Officer
         setUpTxtShow();

        //Synchronize JSON SQLite
        synchronizeJSONtoCategory();


        //setUp All Array
        setUPAllArray();

        //Create ListView
        createListView();

    }//Oncreate






    private void createListView() {

        int[]myTarget = {R.drawable.walk_icon,R.drawable.yoga_icon2,R.drawable.cycling_icon}; // Add Images

        MyAdapter objMyAdapter = new MyAdapter(PhysicalActivity.this, strListCategoryName,myTarget);
        final ListView objListView = (ListView)findViewById(R.id.CoffeelistView);
        objListView.setAdapter(objMyAdapter);  // Fusion Part with Activity_order and List_view_row

        //Active Click
        objListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                strMyCategoryName = strListCategoryName[position];


                    //Show Choose Items

                    ShowChooseItem();


            }//event
        });

    }//createListView

    private void ShowChooseItem() {

        CharSequence[] charItems = {"10 steps ","20 steps"," 30 steps","40 steps","50 steps"};

        AlertDialog.Builder objBuilder = new AlertDialog.Builder(this);
        objBuilder.setIcon(R.drawable.questionicon);
        objBuilder.setTitle("How many steps?");
        objBuilder.setCancelable(false);
        objBuilder.setSingleChoiceItems(charItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which){

                    case 0:
                        strItems="1";
                        break;
                    case 1:
                        strItems ="2";
                        break;
                    case 2:
                        strItems="3";
                        break;
                    case 3:
                        strItems="4";
                        break;
                    case 4:
                        strItems="5";
                        break;

                }//switch

                dialog.dismiss();

               // checklog();

                //Up New Order
               // upNewOrder();

            }
        });
        AlertDialog objAlertDialog = objBuilder.create();
        objAlertDialog.show();



    }//ShowSelect_Items

    private void setUPAllArray() {
        strListCategoryName = objCategoryTABLE.listCategoryName();
    }//setUPAllArray

    private void synchronizeJSONtoCategory() {

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
            HttpPost objHttpPost     =  new HttpPost("http://www.puneethbedre.com/health/php_get_data_Category.php");
            HttpResponse objHttpResponse =  objHttpClient.execute(objHttpPost);
            HttpEntity objHttpEntity   =  objHttpResponse.getEntity();
            objInputStream = objHttpEntity.getContent();

        }catch (Exception e){
            Log.d("Category", "Error from InputStram==>" + e.toString());
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
            Log.d("Category","Error Create String==>"+e.toString());
        }

        // Update Value to SQLite

        try {
            final JSONArray objJSONArray =  new JSONArray(strJSON);
            for(int i=0;i< objJSONArray.length();i++){
                JSONObject objJSONObject = objJSONArray.getJSONObject(i);
                String strCategoryID = objJSONObject.getString("category_id");
                String strCategoryName = objJSONObject.getString("category_name");

                // CoffeeTABLE objCofeeTable = new CoffeeTABLE(this);

                long insertValueCoffee  = objCategoryTABLE.addValueCoffee(strCategoryID,strCategoryName);

            }//for


        }catch (Exception e){
            Log.d("Category","Error Update Value==>"+e.toString());
        }
    }//synchronizeJSONtoCategory

    private void setUpTxtShow() {
        strMyofficer = getIntent().getExtras().getString("Name");
        txtShowOfficer.setText(strMyofficer);
    }//SetUpTextShow

    private void bindWidget() {

        txtShowOfficer = (TextView)findViewById(R.id.txtShowOfficer);

    }//bindWidget


    public void Back(){
        Intent objIntent = new Intent(PhysicalActivity.this,MobileActivity.class);
        startActivity(objIntent);
        finish();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_physical, menu);
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
