package com.appdever.healthapp;

import android.app.Dialog;
import android.content.Context;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import adapters.ItemTextOnlyAdapter;


public class CyclingFragment extends Fragment implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private View viewInflate;

    public CyclingFragment() {
    }

    TextView txt_activity,txt_distance,txt_speed,txt_duration;
    Button btn_play,btn_detail1,btn_detail2,btn_activity,btn_restart;
    Chronometer stopWatch;
    long startTime = 0L;
    long countUp;
    String h,m,s;
    int start=0;
    GoogleMap myMap;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    boolean mUpdatesRequested = false;
    Double lat,lng,old_distance=0.0;
    int SetLocation = 0;
    LatLng latlng,old_latlng;
    float speed_sum;
    ArrayList<Float> speed_avg = new ArrayList<Float>();

    int run=0,stop=0;
    MainActivity main;
    Dialog dialog;

    float speed;

    LinearLayout layout_detail;

    long start_h;
    long start_m;
    long start_s;
    long sum_start_time;

    /**
     *  Track whether an authorization activity is stacking over the current activity, i.e. when
     *  a known auth error is being resolved, such as showing the account chooser or presenting a
     *  consent dialog. This avoids common duplications as might happen on screen rotations, etc.
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        viewInflate = inflater.inflate(R.layout.activity_cycling, null);
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


        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();

        myMap = ((SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map)).getMap();

        myMap.setMyLocationEnabled(true);
        myMap.getUiSettings().setZoomControlsEnabled(true);
        myMap.getUiSettings().setMyLocationButtonEnabled(true);
        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //myMap.getUiSettings().setAllGesturesEnabled(false);

        layout_detail = (LinearLayout)viewInflate.findViewById(R.id.layout_detail);
        stopWatch = (Chronometer) viewInflate.findViewById(R.id.chrono);
        btn_activity = (Button) viewInflate.findViewById(R.id.btn_activity);
        btn_activity.setText("Cycling");
        btn_play = (Button) viewInflate.findViewById(R.id.btn_play);
        btn_detail1 = (Button) viewInflate.findViewById(R.id.btn_detail1);
        btn_detail2 = (Button) viewInflate.findViewById(R.id.btn_detail2);
        btn_restart = (Button) viewInflate.findViewById(R.id.btn_restart);

        txt_activity = (TextView)viewInflate.findViewById(R.id.txt_activity);
        txt_activity.setText("Activity : Cycling");
        txt_distance = (TextView)viewInflate.findViewById(R.id.txt_distance);
        txt_speed = (TextView)viewInflate.findViewById(R.id.txt_speed);
        txt_duration = (TextView)viewInflate.findViewById(R.id.txt_duration);

        btn_detail1.setTag("Distance");
        btn_detail2.setTag("Speed");

        stopWatch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
            @Override
            public void onChronometerTick(Chronometer arg0) {
                Calendar c = Calendar.getInstance();
                long stop_h = c.get(Calendar.HOUR);
                long stop_m = c.get(Calendar.MINUTE);
                long stop_s = c.get(Calendar.SECOND);

                long am_pm = c.get(Calendar.AM_PM);
                if(am_pm==1){
                    stop_h +=12;
                }

                long sum_stop_time = ((stop_h*3600)+(stop_m*60)+stop_s) - sum_start_time;

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
                btn_detail1.setVisibility(View.VISIBLE);
                btn_detail2.setVisibility(View.VISIBLE);


                btn_detail1.setTag("Distance");
                btn_detail1.setBackgroundResource(R.drawable.btn_corner_orange);
                btn_detail1.setText("Distance\n(km)");

                btn_detail2.setTag("Speed");
                btn_detail2.setBackgroundResource(R.drawable.btn_corner_red);
                btn_detail2.setText("Speed\n(km/hr)");

                btn_play.setText("00:00:00");
                myMap.clear();
                stop = 0;


            }
        });


        btn_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialog("Activity");
            }
        });

        btn_detail1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialog("Detail1");
            }
        });

        btn_detail2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialog("Detail2");
            }
        });

    }


    public void setTimeStart(){

        mGoogleApiClient.connect();
        Calendar c = Calendar.getInstance();
        start_h = c.get(Calendar.HOUR);
        start_m = c.get(Calendar.MINUTE);
        start_s = c.get(Calendar.SECOND);
        long am_pm = c.get(Calendar.AM_PM);
        if(am_pm==1){
            start_h +=12;
        }

        sum_start_time = (start_h*3600)+(start_m*60)+start_s;

        stopWatch.start();
        btn_play.setCompoundDrawablesWithIntrinsicBounds(null,null,null,getResources().getDrawable(R.drawable.ic_stop));
        start=1;

        myMap.clear();
    }


    public void setTimeStop() {


        Marker marker_start = myMap.addMarker(new MarkerOptions()
                .position(latlng)
                .title("START")
                .snippet("HERE"));

        marker_start.showInfoWindow();

        Marker marker_stop = myMap.addMarker(new MarkerOptions()
                .position(old_latlng)
                .title("STOP")
                .snippet("HERE"));

        marker_stop.showInfoWindow();
        mGoogleApiClient.disconnect();
        stopWatch.stop();
        btn_play.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_play));
        start = 0;
        stop = 1;

        try {

            btn_activity.setVisibility(View.GONE);
            btn_play.setVisibility(View.GONE);
            btn_detail1.setVisibility(View.GONE);
            btn_detail2.setVisibility(View.GONE);

            Calendar c = Calendar.getInstance();
            long stop_h = c.get(Calendar.HOUR);
            long stop_m = c.get(Calendar.MINUTE);
            long stop_s = c.get(Calendar.SECOND);
            long am_pm = c.get(Calendar.AM_PM);
            if(am_pm==1){
                stop_h +=12;
            }

            long duration = ((stop_h*3600)+(stop_m*60)+stop_s) -((start_h*3600)+(start_m*60)+start_s);

/*
            if(start_h>=stop_h){
                duration = (((stop_h+12)*3600)+(stop_m*60)+stop_s)-((start_h*3600)+(start_m*60)+start_s);
            }else{
                duration = ((stop_h*3600)+(stop_m*60)+stop_s)-((start_h*3600)+(start_m*60)+start_s);
            }
*/

            long duration_h = duration/3600;
            long duration_m = (duration/60)-(duration_h*60);
            long duration_s = duration - ((duration_h*3600)+(duration_m*60));
            txt_distance.setText("Distance : " + formatNumber(old_distance));
            txt_speed.setText("Averange Speed : " + formatSpeed(speed_sum / run) + " km/hr");
            txt_duration.setText("Duration : "+formatTime((duration_h))+":"+formatTime((duration_m))+":"+formatTime((duration_s))+" hours");


            layout_detail.setVisibility(View.VISIBLE);




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

            case "Detail1":setListDetail1();

                break;

            case "Detail2":setListDetail2();

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
                if(pos!=2) {
                    dialog.cancel();
                    main.displayView(pos);
                }
            }
        });
    }

    public void setListDetail1(){
        String[] choice = {"Distance","Speed"};
        ListView listView = (ListView) dialog.findViewById(R.id.listView);
        listView.setAdapter(new ItemTextOnlyAdapter(getActivity(), choice));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch(position){
                    case 0:
                        btn_detail1.setTag("Distance");
                        btn_detail1.setBackgroundResource(R.drawable.btn_corner_orange);
                        btn_detail1.setText("Distance\n" + formatNumber(old_distance));
                        break;
                    case 1:
                        btn_detail1.setTag("Speed");
                        btn_detail1.setBackgroundResource(R.drawable.btn_corner_red);
                        btn_detail1.setText("Speed\n" + formatSpeed(speed) + " km/hr");
                        break;
                    default:
                        break;
                }
                dialog.cancel();

            }
        });
    }

    public void setListDetail2(){
        String[] choice = {"Distance","Speed"};
        ListView listView = (ListView) dialog.findViewById(R.id.listView);
        listView.setAdapter(new ItemTextOnlyAdapter(getActivity(), choice));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch(position){
                    case 0:
                        btn_detail2.setTag("Distance");
                        btn_detail2.setBackgroundResource(R.drawable.btn_corner_orange);
                        btn_detail2.setText("Distance\n"+formatNumber(old_distance));
                        break;
                    case 1:
                        btn_detail2.setTag("Speed");
                        btn_detail2.setBackgroundResource(R.drawable.btn_corner_red);
                        btn_detail2.setText("Speed\n"+formatSpeed(speed)+" km/hr");
                        break;
                    default:
                        break;
                }
                dialog.cancel();

            }
        });
    }









    OnLocatonListener mCallbackLocation;

    public interface OnLocatonListener {

        public void onLocationChanged(Location prevLoc, Location currentLoc);
    }

    public void setOnLocatonListener(OnLocatonListener listener) {

        try {
            mCallbackLocation = (OnLocatonListener) listener;
        } catch (ClassCastException e)  {
            throw new ClassCastException(this.toString() + " must implement OnLocatonListener");
        }
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public boolean isServicesAvailable() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        return (resultCode == ConnectionResult.SUCCESS);
    }


    @Override
    public void onConnected(Bundle bundle) {
        // TODO Auto-generated method stub
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        //mLocationRequest.setSmallestDisplacement(1);
        mLocationRequest.setFastestInterval(500);

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("LatLng",location.toString());
        if(stop==0) {

            try {
                if (SetLocation == 0) {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18);
                    myMap.moveCamera(cameraUpdate);
                    SetLocation = 1;
                    latlng = new LatLng(location.getLatitude(), location.getLongitude());
                    old_latlng = new LatLng(location.getLatitude(), location.getLongitude());
                    mGoogleApiClient.disconnect();

                } else {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                    myMap.animateCamera(cameraUpdate);
                    speed = location.getSpeed();


                    Double distance = SphericalUtil.computeDistanceBetween(old_latlng, new LatLng(location.getLatitude(), location.getLongitude()));

                    PolylineOptions options = new PolylineOptions().width(8).color(Color.BLUE).geodesic(true);
                    options.add(old_latlng);
                    options.add(new LatLng(location.getLatitude(), location.getLongitude()));
                    myMap.addPolyline(options);
                    old_latlng = new LatLng(location.getLatitude(), location.getLongitude());

                    if (speed != 0) {
                        old_distance += distance;
                        speed_avg.add(speed);
                        speed_sum += speed + 1;
                        run++;
                    }

                    switch (btn_detail1.getTag().toString()) {
                        case "Distance":
                            btn_detail1.setText("Distance\n" + formatNumber(old_distance));
                            break;
                        case "Speed":
                            btn_detail1.setText("Speed\n" + formatSpeed(speed) + " km/hr");
                            break;
                        default:
                            break;
                    }

                    switch (btn_detail2.getTag().toString()) {
                        case "Distance":
                            btn_detail2.setText("Distance\n" + formatNumber(old_distance));
                            break;
                        case "Speed":
                            btn_detail2.setText("Speed\n" + formatSpeed(speed) + " km/hr");
                            break;
                        default:
                            break;
                    }

                }


            } catch (Exception e) {
                Log.e("Location", e.toString());

            }

        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    @Override
    public void onStart()  {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        // After disconnect() is called, the client is considered "dead".
       // mGoogleApiClient.disconnect();
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

        return String.format("%4.2f %s", distance, unit);
    }


    private String formatSpeed(float speed) {
        return String.format("%4.1f", speed);
    }

    private String formatTime(long time) {
        return String.format("%02d", time);
    }

}
