package com.example.davcpe.healthprototypeapp;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;


import android.app.Activity;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import android.app.ActionBar.OnNavigationListener;
import android.widget.Toast;


public class HomeActivity extends ActionBarActivity  {

    private Spinner spinner_Act,spinner_Scale;
    private ImageView imgStart,img_Act,img_Scale,imgBack;
    private TextView txtScale;
    private MyAlertDialog objAlertDialog;
    ArrayAdapter<CharSequence> adapterAct,adapterScale;
    private  String strItemAct,strScale; //SpinnerItem
    private   String user_name,user_id,user_Description;
    private EditText editDes;



    //SetUpTimer

    TextView mButtonLabel;

    // Counter of time since app started ,a background task
    private long mStartTime = 0L;
    private TextView mTimeLabel,mTimerLabel;

    // Handler to handle the message to the timer task
    private Handler mHandler = new Handler();

    static final int UPDATE_INTERVAL = 1000;



    String timerStop1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //GetIntentData
        GetIntentData();


        //BindWidget
        BindWidget();

       //SetUpListView
        SetUpListView();


       //SetUpStartEvent
        SetUpStartEvent();
    }

    private void GetIntentData() {
        Intent objIntent = getIntent();
        user_name = objIntent.getStringExtra("UserName");
        user_id = objIntent.getStringExtra("UserID");

    }//GetIntentData


    private void BindWidget() {
        spinner_Act   = (Spinner)findViewById(R.id.spinnerACT);
        spinner_Scale = (Spinner)findViewById(R.id.spinnerScale);
        imgStart      = (ImageView)findViewById(R.id.imageStartbtn);
        img_Act       = (ImageView)findViewById(R.id.imageACT);
        img_Scale     = (ImageView)findViewById(R.id.imageScale);
        imgBack       = (ImageView)findViewById(R.id.imageBack);
        txtScale      = (TextView)findViewById(R.id.textScale);
        editDes       = (EditText)findViewById(R.id.editDesc);



        //Timer
        mTimerLabel = (TextView) findViewById(R.id.textTimer);

        //GetDataFrom Editext
        user_Description = editDes.getText().toString().trim();





    }//BindWidget

    private void SetUpListView() {
       //Spinner Act
        adapterAct = ArrayAdapter.createFromResource(this,R.array.activity,android.R.layout.simple_spinner_item);
        adapterAct.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Act.setAdapter(adapterAct);
        spinner_Act.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                position = spinner_Act.getSelectedItemPosition();

               switch (position) {
                   case 0:
                   strItemAct="Cycling";
                       break;
                   case 1:
                   strItemAct="Sleep";
                       break;
               }

                if(strItemAct == "Sleep"){
                    img_Act.setImageResource(R.drawable.sleep_icon);
                }
                else if(strItemAct =="Cycling"){
                    img_Act.setImageResource(R.drawable.cycling_icon);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
      //Spinner Scale
        adapterScale = ArrayAdapter.createFromResource(this,R.array.scale,android.R.layout.simple_spinner_item);
        adapterScale.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Scale.setAdapter(adapterScale);
        spinner_Scale.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        strScale="Kcal";
                        break;
                    case 1:
                        strScale="Distance";
                        break;
                }

                if(strScale == "Kcal"){
                    img_Scale.setImageResource(R.drawable.kcal_icon);
                    txtScale.setText("0.00 Kcal");

                }
                else if(strScale =="Distance"){
                    img_Scale.setImageResource(R.drawable.distance_icon);
                    txtScale.setText("0.00 Km");

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }//SetUpListView



    private void SetUpStartEvent() {

        imgStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                imgStart.setImageResource(R.drawable.stop_icon2);
                if(mStartTime == 0L){
                    mStartTime = SystemClock.uptimeMillis();
                    mHandler.removeCallbacks(mUpdateTimeTask);
                    mHandler.postDelayed(mUpdateTimeTask, 100);

                }
                imgStart.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mHandler.removeCallbacks(mUpdateTimeTask);
                        mTimerLabel.setText(timerStop1);
                        mStartTime = 0L;



                        //InviteFriendDialog
                        InviteFriendDialog();
                    }
                });

            }
        });


        imgBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent = new Intent(HomeActivity.this,FirstPage.class);
                startActivity(objIntent);
                finish();
            }
        });



    }//SetUpStartEvent

    private void InviteFriendDialog() {
        final AlertDialog.Builder objAlert = new AlertDialog.Builder(this);
        objAlert.setTitle("Save this Challenge to  "+user_name+"'History and Challenge friend");
        objAlert.setMessage("Details:" + "\n" + "Duration: " + timerStop1 + "\n" + "Activity: " + strItemAct + "\n" + "Scale: " + strScale + "\n\n" + "Description: "+user_Description);
        objAlert.setCancelable(false);
        objAlert.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Intent objIntent = new Intent(MainActivity.this, HomeActivity.class);
//                objIntent.putExtra("Name",strName);
//                startActivity(objIntent);
//                finish();

            }
        });
        objAlert.setPositiveButton("Accept",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Intent objIntent = new Intent(MainActivity.this, HomeActivity.class);
//                objIntent.putExtra("Name",strName);
//                startActivity(objIntent);
//                finish();

            }
        });

        objAlert.show();
    }

    private Runnable mUpdateTimeTask = new Runnable(){

        public void run() {

            final long start = mStartTime;
            long millis = SystemClock.uptimeMillis()- start;

            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            mTimerLabel.setText("" + minutes + ":"
                    + String.format("%02d", seconds));

            timerStop1 = minutes + ":"
                    + String.format("%02d", seconds);

            mHandler.postDelayed(this, 200);

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
