package com.example.davcpe.healthprototypeapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ArrayAdapter;
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
import android.widget.TextView;


public class HomeActivity extends ActionBarActivity {

    private Spinner spinner_Act;
    private ImageView imgStart;
    private MyAlertDialog objAlertDialog;
    ArrayAdapter<CharSequence> adapter;



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

        //BindWidget
        BindWidget();

       //SetUpListView
        SetUpListView();

       //SetUpStartEvent
        SetUpStartEvent();
    }




    private void BindWidget() {
        spinner_Act = (Spinner)findViewById(R.id.spinnerACT);
        imgStart    = (ImageView)findViewById(R.id.imageStartbtn);



        //Timer
        mTimerLabel = (TextView) findViewById(R.id.textTimer);


    }//BindWidget

    private void SetUpListView() {
       adapter = ArrayAdapter.createFromResource(this,R.array.activity,android.R.layout.simple_spinner_item);
       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       spinner_Act.setAdapter(adapter);


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
    }//SetUpStartEvent

    private void InviteFriendDialog() {
        final AlertDialog.Builder objAlert = new AlertDialog.Builder(this);
        objAlert.setTitle("Save this Challenge to History and Challenge friend");
        objAlert.setMessage("Detail:  "+timerStop1+" ");
        objAlert.setCancelable(false);
        objAlert.setPositiveButton("Accept",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Intent objIntent = new Intent(MainActivity.this, HomeActivity.class);
//                objIntent.putExtra("Name",strName);
//                startActivity(objIntent);
//                finish();

            }
        });
        objAlert.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
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
