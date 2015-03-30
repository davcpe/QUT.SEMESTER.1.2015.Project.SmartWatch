package com.example.davcpe.healthprototypeapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

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


public class MainActivity extends ActionBarActivity {

    private  UserTABLE objUserTABLE;
    private  FriendTable objFriendTable;

    private  String strUserChoose,strPasswordChoose,strPasswordTrue, strName,strUser_ID;
    private EditText edtUser, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //BindWidget
        bindWidget();


        //CreateTable
        objUserTABLE = new UserTABLE(this);
        objFriendTable = new FriendTable(this);

        //delete All data
        deleteData();

        //synJsonTOSQLite
        synJsonTOSQLite();




    }

    public void clickLogin(View view){
        strUserChoose = edtUser.getText().toString().trim();
        strPasswordChoose = edtPassword.getText().toString().trim();

        if(strUserChoose.equals("")|| strPasswordChoose.equals("")){
            MyAlertDialog objMyAlert = new MyAlertDialog();
            objMyAlert.errorDialog(MainActivity.this,"Missing Input Data","Please complete blank");
            //Alert Error
        }else{

            checkUser();

        }

    }
    private void checkUser() {
        try{
            String strData[] = objUserTABLE.searchUser(strUserChoose);//From EditText UserName Input
            strPasswordTrue = strData[3];
            strUser_ID = strData[1];
            strName  = strData[2];
            Log.d("Health","Welcome  "+strName);

            checkPassword();


        }catch (Exception e){
            MyAlertDialog objMyAlert = new MyAlertDialog();
            objMyAlert.errorDialog(MainActivity.this, "There is no user","NO"+strUserChoose +"in this Database");

        }

    }
    private void checkPassword() {
        if (strPasswordChoose.equals(strPasswordTrue)) {



            //Intent to Order Activity
            wellCOmeUser();
        }else{

            MyAlertDialog objMyAlert = new MyAlertDialog();
            objMyAlert.errorDialog(MainActivity.this,"Password False","Please Try again");
        }

    }//CheckPassword

    private void wellCOmeUser() {

        final AlertDialog.Builder objAlert = new AlertDialog.Builder(this);
        objAlert.setIcon(R.drawable.welcome_icon);
        objAlert.setTitle("Welcome to The Healthy Challenges !!!");
        objAlert.setMessage("Welcome  "+strName+"\n"+"to The Healthy Challenges !!!");
        objAlert.setCancelable(false);
        objAlert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent objIntent = new Intent(MainActivity.this, FirstPage.class);
                objIntent.putExtra("UserName",strName);
                objIntent.putExtra("UserID",strUser_ID);
                startActivity(objIntent);
                finish();

            }
        });
        objAlert.show();


    }//welcome user


    private void synJsonTOSQLite() {
        //setUp Policy
        if(Build.VERSION.SDK_INT>9){
            StrictMode.ThreadPolicy mypolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(mypolicy);
        }//if
        InputStream objInputStream = null;
        String strJSON ="";//GetData
        //Create objInputStream

        try{

            HttpClient objHttpClient   =  new DefaultHttpClient();
            HttpPost objHttpPost     =  new HttpPost("http://puneethbedre.com/health/php_get_data.php");
            HttpResponse objHttpResponse =  objHttpClient.execute(objHttpPost);
            HttpEntity objHttpEntity   =  objHttpResponse.getEntity();
            objInputStream = objHttpEntity.getContent();

        }catch (Exception e){
            Log.d("Heath", "Error from InputStram==>" + e.toString());
        }

        //Change InputStream to String
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
            Log.d("Health","Error Create String==>"+e.toString());
        }

        //Up Value to SQLite
        try{
            final JSONArray objJSONArray =  new JSONArray(strJSON);
            for(int i=0;i< objJSONArray.length();i++){
                JSONObject objJSONObject = objJSONArray.getJSONObject(i);
                String strUserID = objJSONObject.getString("user_id");
                String strUserName = objJSONObject.getString("user_name");
                String strPassword = objJSONObject.getString("user_password");
                String strOfficer = objJSONObject.getString("user_officer");

                long insertValue  = objUserTABLE.addValueUser(strUserID,strUserName,strPassword,strOfficer);

            }//for
        }catch (Exception e ){
            Log.d("Health","Error Update Value==>"+e.toString());
        }

    }

    private void deleteData() {
        SQLiteDatabase objSQLite = openOrCreateDatabase("Health.db",MODE_PRIVATE,null);
        objSQLite.delete("userTABLE",null,null);
    }

    private void bindWidget() {
        edtUser  = (EditText)findViewById(R.id.editUserName);
        edtPassword = (EditText)findViewById(R.id.editPassword);

    }//Bind Widget


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
