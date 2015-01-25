package com.example.douglass_macbook.ss12_simon_says;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class GameActivity extends ActionBarActivity implements SensorEventListener {

    TextView textView_instructions;
    TextView textView_p1;
    TextView textView_p2;
    TextView textView_p3;
    TextView textView_p4;
    TextView textView_round;

    ImageView imageView_check;
    ImageView imageView_cross;
    ImageView imageView_go;
    ImageView imageView_winner;

    int round = 1;
    boolean simonSays;
    boolean leftRotate = false;
    boolean rightRotate = false;
    boolean forwardRotate = false;
    boolean backRotate = false;
    int action;
    int playerScore = 0;
    long timeStamp;
    static int currentPlayerNumber;
    static int currentPlayerNumId;
    static HashMap<String, Object> instruction;
    int max_players = 4;
    ArrayList <Integer> who;
    List<String> actionsArray;

    List<String> roundStrings = Arrays.asList("P1: ", "P2: ", "P3: ", "P4: ");
    // Timer stuff
    private Timer myTimer = new Timer();
    private TimerTask mCommandTimer;

    // Sensor stuff
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private long mLastUpdateTime;

    // Constants
    public static final int SENSOR_UPDATE_DELAY = 100;
    public static final float ROTATION_THRESHOLD = 0.65f;

    // Timer
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //dougStuff
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //setting textviews
        textView_instructions = (TextView)findViewById(R.id.textView_instruction);
        textView_p1 = (TextView)findViewById(R.id.textView_P1);
        textView_p2 = (TextView)findViewById(R.id.textView_P2);
        textView_p3 = (TextView)findViewById(R.id.textView_P3);
        textView_p4 = (TextView)findViewById(R.id.textView_P4);

        textView_round = (TextView)findViewById(R.id.textView_roundNum);
        actionsArray = Arrays.asList( getApplicationContext().getResources().getStringArray(R.array.instructions_group) );

        //setting imageviews
        imageView_check = (ImageView) findViewById(R.id.correct_image);
        imageView_cross = (ImageView) findViewById(R.id.wrong_image);
        imageView_go = (ImageView) findViewById(R.id.go_image);
        imageView_winner = (ImageView) findViewById(R.id.winner_image);

        updatePlayerScore(playerScore);

        // Get data from the instruction
        simonSays = (boolean)instruction.get("simonSays");
        who = (ArrayList<Integer>) instruction.get("who");
        action = (int)instruction.get("action");
        timeStamp = (long)instruction.get("timeStamp");

        displayInstructions();
    }

    private void displayInstructions() {
        round++;
        String insert = Integer.toString(round);
        textView_round.setText("insert");
        textView_instructions.setText("");
        if (simonSays) {
            textView_instructions.setText("Simon says");
        }
        if (max_players == who.size()) {
            //everyone
            textView_instructions.append(" everyone");
        }
        //insert instructions
        if (action >= 0 && action <= 14) {
            insert = actionsArray.get(action);
            textView_instructions.append(" " + insert);
        }

        // Set timer to call go()
        //TODO start audio that reads the instruction
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                go();
            }
        }, 4000);
    }

    private void go() {
        // Show go image
        displayImage(imageView_go, View.VISIBLE);

        // Set timer to call sensorBegin()
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Hide go image
                displayImage(imageView_go, View.GONE);

                sensorBegin();
            }
        }, 2000);
    }

    private void sensorBegin() {
        // Register accelerometer to turn it on
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        // Set timer to call sensorEnd()
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sensorEnd();
            }
        }, 1000);
    }

    private void sensorEnd() {
        // Pause the sensor
        mSensorManager.unregisterListener(this, mAccelerometer);

        // Detect success or fail
        boolean success = false;
        switch(action){
            case 0:
                if(userShouldDoAction() && leftRotate && !rightRotate){
                    //needs to go left
                    success = true;
                }
                else{
                    success = false;
                }
                break;
            case 1:
                if(userShouldDoAction() && rightRotate && !leftRotate){
                    //needs to go right
                    success = true;
                }
                else{
                    success = false;
                }
                break;
            case 2:
                if(userShouldDoAction() && backRotate && !forwardRotate){
                    //needs to go up
                    success = true;

                }else{
                    success = false;
                }
                break;
            case 3:
                if(userShouldDoAction() && forwardRotate && !backRotate){
                    //needs to go down
                    success = true;
                }
                else{
                    success = false;
                }
                break;
            case 4:
                if(userShouldDoAction()){
                    //needs to go punch
                    success = true;
                }
                else{
                    success = false;
                }
                break;
            case 5:
                if(userShouldDoAction()){
                    //needs to elbow
                    success = true;
                }
                else{
                    success = false;
                }
                break;
            case 6:
                if(userShouldDoAction() && !leftRotate && !rightRotate && !forwardRotate && !backRotate){
                    //needs to stay
                    success = true;
                }
                else{
                    success = false;
                }
                break;
            case 7:
                if(userShouldDoAction() && (leftRotate || rightRotate || forwardRotate || backRotate)){
                    //needs to move
                    success = true;
                }else{
                    success = false;
                }
                break;

            // Mental math actions
            case 8:
                if(currentPlayerNumber%2!=0)
                    currentPlayerNumber+=7;
                break;
            case 9:
                if(currentPlayerNumber%2==0)
                    currentPlayerNumber+=7;
                break;
            case 10:
                if(currentPlayerNumber%2!=0)
                    currentPlayerNumber-=4;
                break;
            case 11:
                if(currentPlayerNumber%2==0)
                    currentPlayerNumber-=4;
                break;
            case 12:
                currentPlayerNumber+=7;
                break;
            case 13:
                currentPlayerNumber-=4;
                break;
            case 14:
                currentPlayerNumber*=2;
                break;
            default: //is an action item
                break;
        }

        if(success)
        {
            playerScore++;
            updatePlayerScore( playerScore );
            displayImage(imageView_check, View.VISIBLE);
        }
        else
        {
            displayImage(imageView_cross, View.VISIBLE);
        }

        resetSensors();

        // Call the "result" function
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("playerNumber", currentPlayerNumId);
        params.put("isSuccess", success);
        ParseCloud.callFunctionInBackground("result", params,
                new FunctionCallback<HashMap<String, Object>>() {
                    @Override
                    public void done(HashMap<String, Object> result, com.parse.ParseException e) {
                        // Do nothing
                    }
                });



        // Set timer to call sensorBegin()
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Hide check/cross images
                displayImage(imageView_check, View.GONE);
                displayImage(imageView_cross, View.GONE);

                // Return to ReadyActivity
                finish();
            }
        }, 1000);
    }



    // Sets all sensor indicators to false
    private void resetSensors() {
        leftRotate = false;
        rightRotate = false;
        forwardRotate = false;
        backRotate = false;
    }



    private void displayImage(final ImageView whichImage, final int visibility) {
        if(whichImage != null)
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    whichImage.setVisibility(visibility);
                }
            });
        }
    }

    private void updatePlayerScore(int playerScore) {
        final String score = Integer.toString(playerScore);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView_p1.setText( roundStrings.get(currentPlayerNumId - 1) + score );
            }
        });
    }
    boolean userShouldDoAction(){
        return (who.contains(currentPlayerNumId)) && (simonSays);
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

    //DOUGS
    @Override
    public void onSensorChanged(SensorEvent event) {
        // Detect if accelerometer sensor data changed
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Check last update time (less intense use of resources?)
            long currTime = System.currentTimeMillis();
            if (currTime - mLastUpdateTime > SENSOR_UPDATE_DELAY) {
                mLastUpdateTime = currTime;
                // Calculate normalized rotation values.
                // Reference: http://stackoverflow.com/a/15149421/555544
                float[] values = event.values;
                double norm = Math.sqrt(values[0] * values[0]
                        + values[1] * values[1]
                        + values[2] * values[2]);
                values[0] /= norm;
                values[1] /= norm;
                values[2] /= norm;

                // Detect x-rotation
                if (values[0] > ROTATION_THRESHOLD) {
                    Toast.makeText(this, "Left rotate detected", Toast.LENGTH_SHORT).show();
                    leftRotate = true;
                } else if (values[0] < -ROTATION_THRESHOLD) {
                    Toast.makeText(this, "Right rotate detected", Toast.LENGTH_SHORT).show();
                    rightRotate = true;
                }

                // Detect y-rotation
                if (values[1] > ROTATION_THRESHOLD) {
                    Toast.makeText(this, "Forward rotate detected", Toast.LENGTH_SHORT).show();
                    forwardRotate = true;
                } else if (values[1] < -ROTATION_THRESHOLD) {
                    Toast.makeText(this, "Back rotate detected", Toast.LENGTH_SHORT).show();
                    backRotate = true;
                }
            }
        }
    }

    @Override
    protected void onResume () {
        super.onResume();
    }
    @Override
    protected void onPause () {
        super.onPause();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
