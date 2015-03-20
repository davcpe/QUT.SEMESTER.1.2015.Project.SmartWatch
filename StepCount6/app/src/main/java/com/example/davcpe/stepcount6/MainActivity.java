package com.example.davcpe.stepcount6;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private long timestamp;

    private TextView textViewStepCounter;

    private TextView textViewStepDetector;

    private Thread detectorTimeStampUpdaterThread;

    private Handler handler;

    private boolean isRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewStepCounter = (TextView) findViewById(R.id.textView2);
        textViewStepDetector = (TextView) findViewById(R.id.textView4);

        registerForSensorEvents();

        setupDetectorTimestampUpdaterThread();
    }


    public void registerForSensorEvents() {
        SensorManager sManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Step Counter
        sManager.registerListener(new SensorEventListener() {

                                      @Override
                                      public void onSensorChanged(SensorEvent event) {
                                          float steps = event.values[0];
                                          textViewStepCounter.setText((int) steps + "");
                                      }

                                      @Override
                                      public void onAccuracyChanged(Sensor sensor, int accuracy) {

                                      }
                                  }, sManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
                SensorManager.SENSOR_DELAY_UI);

        // Step Detector
        sManager.registerListener(new SensorEventListener() {

                                      @Override
                                      public void onSensorChanged(SensorEvent event) {
                                          // Time is in nanoseconds, convert to millis
                                          timestamp = event.timestamp / 1000000;
                                      }

                                      @Override
                                      public void onAccuracyChanged(Sensor sensor, int accuracy) {

                                      }
                                  }, sManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR),
                SensorManager.SENSOR_DELAY_UI);
    }

    private void setupDetectorTimestampUpdaterThread() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                textViewStepDetector.setText(DateUtils
                        .getRelativeTimeSpanString(timestamp));
            }
        };




        detectorTimeStampUpdaterThread = new Thread() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        Thread.sleep(5000);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        detectorTimeStampUpdaterThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
        detectorTimeStampUpdaterThread.interrupt();
    }

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
