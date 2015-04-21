package com.appdever.healthapp;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;


import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import adapters.ItemTextOnlyAdapter;


public class SleepFragment extends Fragment {

    private View viewInflate;

    public SleepFragment() {
    }

    TextView txt_distance,txt_speed,txt_duration;
    Button btn_play,btn_activity,btn_restart;
    ImageView img_status;
    Chronometer stopWatch;
    long startTime = 0L;
    long countUp;
    String h,m,s;
    int start=0;

    MainActivity main;
    Dialog dialog;


    LinearLayout layout_detail;

    SQLiteDatabase mDb;
    MyDbHelper mHelper;
    Cursor mCursor;


    long start_h;
    long start_m;
    long start_s;
    long start_day;
    long sum_start_time;

    /**
     *  Track whether an authorization activity is stacking over the current activity, i.e. when
     *  a known auth error is being resolved, such as showing the account chooser or presenting a
     *  consent dialog. This avoids common duplications as might happen on screen rotations, etc.
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        viewInflate = inflater.inflate(R.layout.activity_sleep, null);
        return viewInflate;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);

        main = (MainActivity) this.getActivity();

        mHelper = new MyDbHelper(getActivity());
        mDb = mHelper.getWritableDatabase();

        layout_detail = (LinearLayout)viewInflate.findViewById(R.id.layout_detail);
        stopWatch = (Chronometer) viewInflate.findViewById(R.id.chrono);
        btn_activity = (Button) viewInflate.findViewById(R.id.btn_activity);
        btn_play = (Button) viewInflate.findViewById(R.id.btn_play);
        btn_restart = (Button) viewInflate.findViewById(R.id.btn_restart);

        txt_distance = (TextView)viewInflate.findViewById(R.id.txt_distance);
        txt_speed = (TextView)viewInflate.findViewById(R.id.txt_speed);
        txt_duration = (TextView)viewInflate.findViewById(R.id.txt_duration);

        img_status = (ImageView)viewInflate.findViewById(R.id.img_status);
        img_status.setVisibility(View.GONE);

        stopWatch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
            @Override
            public void onChronometerTick(Chronometer arg0) {

                Calendar c = Calendar.getInstance();
                long stop_h = c.get(Calendar.HOUR);
                long stop_m = c.get(Calendar.MINUTE);
                long stop_s = c.get(Calendar.SECOND);
                long ampm = c.get(Calendar.AM_PM);
                long day = c.get(Calendar.DAY_OF_MONTH);
                if(ampm==1){
                    stop_h +=12;
                }

                day-=start_day;

                day*=24;
                stop_h+=day;
                long sum_stop_time = ((stop_h)*3600)+(stop_m*60)+stop_s;

                sum_stop_time = sum_stop_time - sum_start_time;

                stop_h = sum_stop_time/3600;
                stop_m = (sum_stop_time/60)-(stop_h*60);
                stop_s = sum_stop_time - ((stop_m*60)+(stop_h*3600));
                String asText = formatTime(stop_h)+":"+formatTime(stop_m)+ ":" +formatTime(stop_s);
                btn_play.setText(asText);
            }
        });

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(start==0){
                    setTimeStart();
                }else {
                    setTimeStop();
                }

            }
        });


        btn_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_detail.setVisibility(View.GONE);
                btn_activity.setVisibility(View.VISIBLE);
                btn_play.setVisibility(View.VISIBLE);
                img_status.setVisibility(View.GONE);
                btn_play.setText("00:00:00");
                mHelper.onStartTimer(mDb,0,0,0,0,0);
                mHelper.onStopTimer(mDb,0,0,0,0,0);

            }
        });


        btn_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialog("Activity");
            }
        });


        mCursor = mDb.rawQuery("SELECT * FROM " + MyDbHelper.TB_SLEEP, null);
        mCursor.moveToFirst();

        long get_start_h = mCursor.getInt(mCursor.getColumnIndex(MyDbHelper.COL_START_H));
        long get_start_m = mCursor.getInt(mCursor.getColumnIndex(MyDbHelper.COL_START_M));
        long get_start_s = mCursor.getInt(mCursor.getColumnIndex(MyDbHelper.COL_START_S));


        if(get_start_h!=0||get_start_m!=0||get_start_s!=0){
            sum_start_time = (get_start_h*3600)+(get_start_m*60)+get_start_s;
            start_day = mCursor.getInt(mCursor.getColumnIndex(MyDbHelper.COL_START_DAY));
            setTimeContinue();
        }



    }

    public void setTimeStart(){

        startTime = SystemClock.uptimeMillis();
        Calendar c = Calendar.getInstance();
        start_h = c.get(Calendar.HOUR);
        start_m = c.get(Calendar.MINUTE);
        start_s = c.get(Calendar.SECOND);
        start_day = c.get(Calendar.DAY_OF_MONTH);
        long am_pm = c.get(Calendar.AM_PM);
        if(am_pm==1){
            start_h +=12;
        }



        sum_start_time = (start_h*3600)+(start_m*60)+start_s;

        mHelper.onStartTimer(mDb,start_h,start_m,start_s,am_pm,start_day);
        mHelper.onStopTimer(mDb,0,0,0,0,0);

        stopWatch.start();
        btn_play.setCompoundDrawablesWithIntrinsicBounds(null,null,null,getResources().getDrawable(R.drawable.ic_stop));
        start=1;
    }

    public void setTimeContinue(){

        stopWatch.start();
        btn_play.setCompoundDrawablesWithIntrinsicBounds(null,null,null,getResources().getDrawable(R.drawable.ic_stop));
        start=1;
    }


    public void setTimeStop() {

        stopWatch.stop();
        btn_play.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_play));
        start = 0;

        try {

            btn_activity.setVisibility(View.GONE);
            btn_play.setVisibility(View.GONE);

            Calendar c = Calendar.getInstance();
            long stop_h = c.get(Calendar.HOUR);
            long stop_m = c.get(Calendar.MINUTE);
            long stop_s = c.get(Calendar.SECOND);
            long am_pm = c.get(Calendar.AM_PM);
            long stop_day = c.get(Calendar.DAY_OF_MONTH);
            if(am_pm==1){
                stop_h +=12;
            }

            mHelper.onStopTimer(mDb,stop_h,stop_m,stop_s,am_pm,stop_day);

            mCursor = mDb.rawQuery("SELECT * FROM " + MyDbHelper.TB_SLEEP, null);
            mCursor.moveToFirst();

            long get_start_h = mCursor.getInt(mCursor.getColumnIndex(MyDbHelper.COL_START_H));
            long get_start_m = mCursor.getInt(mCursor.getColumnIndex(MyDbHelper.COL_START_M));
            long get_start_s = mCursor.getInt(mCursor.getColumnIndex(MyDbHelper.COL_START_S));
            long get_start_ampm = mCursor.getInt(mCursor.getColumnIndex(MyDbHelper.COL_START_AMPM));
            long get_stop_h = mCursor.getInt(mCursor.getColumnIndex(MyDbHelper.COL_STOP_H));
            long get_stop_m = mCursor.getInt(mCursor.getColumnIndex(MyDbHelper.COL_STOP_M));
            long get_stop_s = mCursor.getInt(mCursor.getColumnIndex(MyDbHelper.COL_STOP_S));
            long get_stop_ampm = mCursor.getInt(mCursor.getColumnIndex(MyDbHelper.COL_STOP_AMPM));
            long get_start_day=mCursor.getInt(mCursor.getColumnIndex(MyDbHelper.COL_START_DAY));
            long get_stop_day=mCursor.getInt(mCursor.getColumnIndex(MyDbHelper.COL_STOP_DAY));
            String txt_start="AM",txt_stop="AM";
            long sum_day;
            sum_day = get_stop_day-get_start_day;
            sum_day*=24;
            long duration = (((get_stop_h+sum_day)*3600)+(get_stop_m*60)+get_stop_s) -((get_start_h*3600)+(get_start_m*60)+get_start_s);


            long duration_h = duration/3600;
            long duration_m = (duration/60)-(duration_h*60);
            long duration_s = duration - ((duration_h*3600)+(duration_m*60));

            if(get_start_ampm==1){
                txt_start="PM";
                get_start_h-=12;
            }

            if(get_stop_ampm==1){
                txt_stop="PM";
                get_stop_h-=12;
            }

            txt_distance.setText("Start to Sleep : "+formatTime(get_start_h)+":"+formatTime(get_start_m)+":"+formatTime(get_start_s)+" "+txt_start);
            txt_speed.setText("Wake up : "+formatTime(get_stop_h)+":"+formatTime(get_stop_m)+":"+formatTime(get_stop_s)+" "+txt_stop);
            txt_duration.setText("Duration : "+formatTime(duration_h)+":"+formatTime(duration_m)+":"+formatTime(duration_s)+" hours");
            if((10<=get_start_h&&get_start_h<=11)&&(7<=duration_h&&duration_h<=8)){
                img_status.setImageResource(R.drawable.mood_good);
            }else{
                img_status.setImageResource(R.drawable.mood_fail);
            }


            img_status.setVisibility(View.VISIBLE);
            layout_detail.setVisibility(View.VISIBLE);

            mHelper.onStartTimer(mDb,0,0,0,0,0);
            mHelper.onStopTimer(mDb,0,0,0,0,0);

        }catch (Exception e){
            Log.e("Detail",e.toString());
        }

    }




    public void setDialog(String type){

        dialog = new Dialog(getActivity());
        //final Dialog dialog_category = new Dialog(NewStoreActivity.this);
        dialog.requestWindowFeature
                (dialog.getWindow().FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_text_only);
        dialog.show();

        TextView txt_nav_category = (TextView) dialog.findViewById(R.id.txt_nav_listView);
        //txt_nav_category.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        ImageButton btn_close = (ImageButton) dialog.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        switch(type){
            case "Activity":setListActivity();

                break;

            default:
                break;

        }

    }

    public void setListActivity(){
        String[] choice = {"Walking","Cycling","Sleep"};
        ListView listView = (ListView) dialog.findViewById(R.id.listView);
        listView.setAdapter(new ItemTextOnlyAdapter(getActivity(), choice));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos = position+1;
                if(pos!=3) {
                    dialog.cancel();
                    main.displayView(pos);
                }
            }
        });
    }


    @Override
    public void onStart()  {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private String formatNumber(double distance) {
        String unit = " m";
        if (distance < 1) {
            distance *= 1000;
            unit = " mm";
        } else if (distance > 1000) {
            distance /= 1000;
            unit = " km";
        }

        return String.format("%4.0f%s", distance, unit);
    }


    private String formatSpeed(float speed) {
        return String.format("%4.1f", speed);
    }

    private String formatTime(long time) {
        return String.format("%02d", time);
    }



}
