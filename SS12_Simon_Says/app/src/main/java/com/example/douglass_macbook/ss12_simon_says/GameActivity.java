package com.example.douglass_macbook.ss12_simon_says;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class GameActivity extends ActionBarActivity implements SensorEventListener {

    // Timer stuff
    private Timer myTimer = new Timer();
    private TimerTask mCommandTimer;

    // Sensor stuff
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private long mLastUpdateTime;
    private boolean detect;

    // Constants
    public static final int SENSOR_UPDATE_DELAY = 100;
    public static final float ROTATION_THRESHOLD = 0.65f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        detect = false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Register sensors, rendering them active
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Pause the sensor
        mSensorManager.unregisterListener(this, mAccelerometer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
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
    public void onSensorChanged(SensorEvent event) {

        // Detect if accelerometer sensor data changed
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Check last update time (less intense use of resources?)
            long currTime = System.currentTimeMillis();
            if(currTime - mLastUpdateTime > SENSOR_UPDATE_DELAY) {
                mLastUpdateTime = currTime;

                // Calculate normalized rotation values.
                // Reference: http://stackoverflow.com/a/15149421/555544
                float[] values = event.values;
                double norm = Math.sqrt(values[0]*values[0]
                        + values[1]*values[1]
                        + values[2]*values[2]);
                values[0] /= norm;
                values[1] /= norm;
                values[2] /= norm;

                // Debug: send to text view
                TextView tv = (TextView) findViewById(R.id.sensor_values);
                tv.setText(String.format("%.5g%n", values[0]) + "\n" + detect);
                if(values[0] > ROTATION_THRESHOLD && !detect) {
                    Toast.makeText(this, "Left rotate detected", Toast.LENGTH_SHORT).show();
                    detect = true;
                }
                else if(values[0] < -ROTATION_THRESHOLD && !detect) {
                    Toast.makeText(this, "Right rotate detected", Toast.LENGTH_SHORT).show();
                    detect = true;
                }
            }
        }
    }

    @Override
    // Method required for implementing SensorEventListener; has no functionality
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // left blank
    }

    public void testClear(View view) {
        detect = false;
    }
}
