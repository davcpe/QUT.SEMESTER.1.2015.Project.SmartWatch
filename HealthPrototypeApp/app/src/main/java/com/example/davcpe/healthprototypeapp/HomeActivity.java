package com.example.davcpe.healthprototypeapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;


public class HomeActivity extends FragmentActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        com.google.android.gms.location.LocationListener,
        GooglePlayServicesClient.OnConnectionFailedListener {

    ////Original Explicit//////////////////////////////
    private Spinner spinner_Act,spinner_Scale;
    private ImageView imgStart,img_Act,img_Scale,imgBack;
    private TextView txtScale;
    private MyAlertDialog objAlertDialog;
    ArrayAdapter<CharSequence> adapterAct,adapterScale;
    private  String strItemAct,strScale; //SpinnerItem
    private   String user_name,user_id,user_Description,strlattitude,strlongtitude;
    private   Double currentlat,currentlong;
    private EditText editDes;


    ///////////GoogleMap Explicit///////////////////////////////////////////////////////////////////
    GPSTracker gpsTracker ;
    private LatLng StartPoint;
    private GoogleMap myMap;            // map reference
    private PolylineOptions lineOptions = null;
    private LatLng point;
    private Button btn1;
    private TextView txtshowDistance,txtshowDistance2,txtshowDetails,txtLat,txtLong;
    Location location;

    private LatLng EndPoint;
    Double a = -27.4631387;
    Double b = 153.0230726;
    LatLng startPosition = new LatLng(a, b);
    LatLng endPosition = new LatLng(13.683660045847258, 100.53900808095932);
    LatLng endPosition2 = new LatLng(-27.4631387, 153.0230726);


    private LocationClient myLocationClient;
    private static final LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)         // 5 seconds
            .setFastestInterval(10)    // 10ms = 60fps
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private  Double EndLat,EndLong;//PutExtra to Userdetails Page
    private  String strEndLat,strEndLong; //PutExtra to Userdetails Page

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

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy
                    = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        //GetIntentData
        GetIntentData();


        //BindWidget
        BindWidget();

       //SetUpListView
        SetUpListView();


        //GetMapReference
        getMapReference();
    }

    private void GetIntentData() {
        Intent objIntent = getIntent();
        user_name = objIntent.getStringExtra("UserName");
        user_id = objIntent.getStringExtra("UserID");

        strlattitude =objIntent.getStringExtra("Lat");
        currentlat = Double.parseDouble(strlattitude);

        strlongtitude =objIntent.getStringExtra("Long");
        currentlong = Double.parseDouble(strlongtitude);

        StartPoint = new LatLng(currentlat,currentlong);

    }//GetIntentData


    private void BindWidget() {
        spinner_Act   = (Spinner)findViewById(R.id.spinnerACT);
        spinner_Scale = (Spinner)findViewById(R.id.spinnerScale);
        imgStart      = (ImageView)findViewById(R.id.imageStartbtn);
        img_Act       = (ImageView)findViewById(R.id.imageACT);
        img_Scale     = (ImageView)findViewById(R.id.imageScale);
        imgBack       = (ImageView)findViewById(R.id.imageBack);
        txtScale      = (TextView)findViewById(R.id.textScale);

        //Timer
        mTimerLabel = (TextView) findViewById(R.id.textTimer);

        //GPS
        gpsTracker = new GPSTracker(HomeActivity.this);



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

    private void InviteFriendDialog() {
        final AlertDialog.Builder objAlert = new AlertDialog.Builder(this);
        objAlert.setTitle("Save this Challenge to  "+user_name+"'History and Challenge friend");
        objAlert.setMessage("Details:" + "\n" + "Duration: " + timerStop1 + "\n" + "Activity: " + strItemAct + "\n" + "Scale: " + strScale + "\n\n" + "Value: "+txtScale.getText().toString().trim());
        objAlert.setCancelable(false);
        objAlert.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        objAlert.setPositiveButton("Accept",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent objIntent = new Intent(HomeActivity.this,FriendActivity.class);
                objIntent.putExtra("UserName",user_name);
                objIntent.putExtra("UserID",user_id);
                startActivity(objIntent);
                finish();


            }
        });

        objAlert.show();
    }

    //ShowUserDetails
    public void ShowUserDetails(){


        Intent objIntent = new Intent(HomeActivity.this,UserDetails.class);
        objIntent.putExtra("UserName",user_name);
        objIntent.putExtra("UserID",user_id);
        objIntent.putExtra("startLat",strlattitude);
        objIntent.putExtra("startLong",strlongtitude);
        objIntent.putExtra("ActivitySelect",strItemAct);
        objIntent.putExtra("ScaleSelect",strScale);
        objIntent.putExtra("Duration",timerStop1);
        objIntent.putExtra("ScaleDistance",txtScale.getText().toString().trim());

        EndLat = gpsTracker.getLatitude();
        strEndLat = String.valueOf(EndLat);
        objIntent.putExtra("EndLat",strEndLat);

        EndLong = gpsTracker.getLongitude();
        strEndLong = String.valueOf(EndLong);
        objIntent.putExtra("EndLong",strEndLong);


        startActivity(objIntent);
        finish();

    }

    //Duration Counter
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

    @Override
    protected  void onResume(){
        super.onResume();
        getMapReference();
        wakeUpLocationClient();
        myLocationClient.connect();
    }

    /**
     *      Activity's lifecycle event.
     *      onPause will be called when activity is going into the background,
     */
    @Override
    public void onPause(){
        super.onPause();
        if(myLocationClient != null){
            myLocationClient.disconnect();
        }
    }
    /**
     *
     * @param lat - latitude of the location to move the camera to
     * @param lng - longitude of the location to move the camera to
     *            Prepares a CameraUpdate object to be used with  callbacks
     */

    private void gotoMyLocation(double lat, double lng) {
        changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(lat, lng))
                        .zoom(17.5f)
                        .bearing(0)
                        .tilt(25)
                        .build()
        ), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                // Your code here to do something after the Map is rendered
            }

            @Override
            public void onCancel() {
                // Your code here to do something after the Map rendering is cancelled
            }
        });
    }
    /**
     *      When we receive focus, we need to get back our LocationClient
     *      Creates a new LocationClient object if there is none
     */
    private void wakeUpLocationClient() {
        if(myLocationClient == null){
            myLocationClient = new LocationClient(getApplicationContext(),
                    this,       // Connection Callbacks
                    this);      // OnConnectionFailedListener
        }
    }
    /**
     *      Get a map object reference if none exits and enable blue arrow icon on map
     */
    private void getMapReference() {
        if(myMap == null){
            myMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

        }
        if(myMap != null){
            myMap.setMyLocationEnabled(true);
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        myLocationClient.requestLocationUpdates(REQUEST,this); // LocationListener
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onLocationChanged( Location location) {
      //gotoMyLocation(location.getLatitude(), location.getLongitude());
        ////////////////////////////////////////////////////////////////
//        if(gpsTracker.getLocation() != null) {
//
//            Double lt = location.getLatitude();
//            Double ln = location.getLongitude();
//            if (android.os.Build.VERSION.SDK_INT > 9) {
//                StrictMode.ThreadPolicy policy
//                        = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//                StrictMode.setThreadPolicy(policy);
//            }

           // SetUpStartEvent();


//            gpsTracker.updateGPSCoordinates();
//
//            double lat = gpsTracker.getLatitude();
//            double lon = gpsTracker.getLongitude();
//            /////////////////////////////////////////////////////
//            LatLng EndPoint5 = new LatLng(lt,ln);
//            EndPoint = new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude());
//            GMapV2Direction mp2 = new GMapV2Direction();
//            Document doc2 = mp2.getDocument(StartPoint, EndPoint, GMapV2Direction.MODE_DRIVING);
//            String distance2 = mp2.getDistanceText(doc2);
//            String duration2 = mp2.getDurationText(doc2);
//            txtScale.setText(distance2);


//        }


        imgStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                imgStart.setImageResource(R.drawable.stop_icon2);
                gotoMyLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                if(mStartTime == 0L){
                    mStartTime = SystemClock.uptimeMillis();
                    mHandler.removeCallbacks(mUpdateTimeTask);
                    mHandler.postDelayed(mUpdateTimeTask, 100);

                }
                if( (gpsTracker.getLocation() != null)&&(strScale=="Distance")) {


                    if (android.os.Build.VERSION.SDK_INT > 9) {
                        StrictMode.ThreadPolicy policy
                                = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                    }


                    gpsTracker.updateGPSCoordinates();
                    /////////////////////////////////////////////////////
                    EndPoint = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                    GMapV2Direction mp2 = new GMapV2Direction();
                    Document doc2 = mp2.getDocument(StartPoint, EndPoint, GMapV2Direction.MODE_DRIVING);
                    String distance2 = mp2.getDistanceText(doc2);
                    String duration2 = mp2.getDurationText(doc2);
                    txtScale.setText(distance2);
                }

                if(  (gpsTracker.getLocation() != null)&&(strScale=="Kcal")    ) {
                    txtScale.setText("0.00 Kcal");
                }




                imgStart.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mHandler.removeCallbacks(mUpdateTimeTask);
                        mTimerLabel.setText(timerStop1);
                        mStartTime = 0L;

                        //InviteFriendDialog
                        //InviteFriendDialog();

                        //ShowUserDetials
                        ShowUserDetails();
                    }
                });


            }
        });


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    private void changeCamera(CameraUpdate update, GoogleMap.CancelableCallback callback) {
        myMap.moveCamera(update);
    }
}
