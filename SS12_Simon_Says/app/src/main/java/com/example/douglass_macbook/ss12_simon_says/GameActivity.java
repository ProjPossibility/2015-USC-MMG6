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
    int [] who;
    int action;
    int playerScore = 0;
    long timeStamp;
    int currentPlayerNumber;
    int currentPlayerNumId;
    int max_players = 4;
    ArrayList <Integer> arraylist;
    List<String> actionsArray;

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

        //dougStuff
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        detect = false;

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

        ParseCloud.callFunctionInBackground("get_instruction",
            new HashMap<String, Object>(),
            new FunctionCallback<HashMap<String, Object>>()
            {
            @Override
            public void done(HashMap<String, Object> instruction, com.parse.ParseException e) {
                if (e == null) {


//                  simonSays = (boolean)instruction.get("simonSays");
//                  arraylist = (ArrayList<Integer>) instruction.get("who");
//                  action = (int)instruction.get("action");
//                  timeStamp = (long)instruction.get("timeStamp");


                    //timeStamp = (long)instruction.get("timeStamp");

                    //Date timeStampDate = (Date)instruction.get("timestamp");


                    //testing
                    //SET PLAYER NUM ID
                    handleEvents();
                    } else {
                        Toast.makeText(getApplicationContext(), "Exception on server query", Toast.LENGTH_LONG).show();
                    }
                }
            });

        //test events handling
        //testing

        //Sample input to test screens
        simonSays = true;
        arraylist = new ArrayList<Integer>();
        arraylist.add(1);
        arraylist.add(2);
        arraylist.add(3);
        arraylist.add(4);
        action = 0;

        //SET PLAYER NUM ID
        handleEvents();
    }

    private void handleEvents() {
        Toast.makeText(getApplicationContext(), "Handling events", Toast.LENGTH_LONG).show();
        round++;
        String insert = Integer.toString(round);
        textView_round.setText("1");
        textView_instructions.setText("");
        if(simonSays){
            textView_instructions.setText("Simon says");
        }
        if (max_players == arraylist.size()) {
            //everyone
            textView_instructions.append(" everyone");
        }
        //insert instructions
        if( action>=0 &&  action <=14){
            insert = arraylist.get(action).toString();
            textView_instructions.append(" "+insert);
        }
        switch(action){
            case 0:
                if(userShouldDoAction() && leftRotate){
                    //needs to go left
                    playerScore++;
                    updatePlayerScore( playerScore );
                    displayImage("check");
                }
                else{
                    displayImage("cross");
                }
                break;
            case 1:
                if(userShouldDoAction() && rightRotate){
                    //needs to go right
                    playerScore++;
                    updatePlayerScore( playerScore );
                    displayImage("check");
                }
                else{
                    displayImage("cross");
                }
                break;
            case 2:
                if(userShouldDoAction()){
                    //needs to go up
                    playerScore++;
                    updatePlayerScore( playerScore );
                    displayImage("check");
                }else{
                    displayImage("cross");
                }
                break;
            case 3:
                if(userShouldDoAction()){
                    //needs to go down
                    playerScore++;
                    updatePlayerScore( playerScore );
                    displayImage("check");
                }
                else{
                    displayImage("cross");
                }
                break;
            case 4:
                if(userShouldDoAction()){
                    //needs to go punch
                    playerScore++;
                    updatePlayerScore( playerScore );
                    displayImage("check");
                }
                else{
                    displayImage("cross");
                }
                break;
            case 5:
                if(userShouldDoAction()){
                    //needs to elbow
                    playerScore++;
                    updatePlayerScore( playerScore );
                    displayImage("check");
                }
                else{
                    displayImage("cross");
                }
                break;
            case 6:
                if(userShouldDoAction()){
                    //needs to stay
                    playerScore++;
                    updatePlayerScore( playerScore );
                    displayImage("check");
                }
                else{
                    displayImage("cross");
                }
                break;
            case 7:
                if(userShouldDoAction()){
                    //needs to move
                    playerScore++;
                    updatePlayerScore( playerScore );
                    displayImage("check");
                }else{
                    displayImage("cross");
                }
            break;
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
    }
    private void displayImage(String status) {
//        ImageView active = (ImageView)findViewById(R.id.correct_image);
//        if( status.equalsIgnoreCase("check") ){
//            active = (ImageView)findViewById(R.id.correct_image);
//        }
//        else if( status.equalsIgnoreCase("cross") ){
            imageView_cross.setVisibility(View.VISIBLE);
//          }
//        else if(status.equalsIgnoreCase("go")){
//            active = (ImageView)findViewById(R.id.go_image);
//        }
//        else if(status.equalsIgnoreCase("winner")){
//            active = (ImageView)findViewById(R.id.winner_image);
//        }
//        Log.d("fucccking", "shieeet");
//        Calendar c = Calendar.getInstance();
//        int seconds_start = c.get(Calendar.SECOND);
//        int seconds_end = c.get(Calendar.SECOND);
//        int difference = seconds_end-seconds_start;
//        do {
//            active.setVisibility(View.VISIBLE);
//        }while (difference < 2 );
//        active.setVisibility(View.GONE);
    }

    private void updatePlayerScore(int playerScore) {
        String score = Integer.toString(playerScore);
        switch(currentPlayerNumId){
            case 1:
                textView_p1.setText(score);
                break;
            case 2:
                textView_p2.setText(score);
                break;
            case 3:
                textView_p3.setText(score);
                break;
            case 4:
                textView_p4.setText(score);
                break;
            default:
                break;
        }
    }
    boolean userShouldDoAction(){
        if (action==currentPlayerNumber)
            return true;
        else
            return false;
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

                // Debug: send to text view
                //TextView tv = (TextView) findViewById(R.id.sensor_values);
                //tv.setText(String.format("%.5g%n", values[0]) + "\n" + detect);
                if (values[0] > ROTATION_THRESHOLD && !detect) {
                    Toast.makeText(this, "Left rotate detected", Toast.LENGTH_SHORT).show();
                    detect = true;
                    leftRotate = true;
                } else if (values[0] < -ROTATION_THRESHOLD && !detect) {
                    Toast.makeText(this, "Right rotate detected", Toast.LENGTH_SHORT).show();
                    detect = true;
                    rightRotate = true;
                }
            }
        }
    }
        @Override
        protected void onResume () {
            super.onResume();

            // Register sensors, rendering them active
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        @Override
        protected void onPause () {
            super.onPause();
            // Pause the sensor
            mSensorManager.unregisterListener(this, mAccelerometer);
        }

    public void testClear(View view) {
        detect = false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
