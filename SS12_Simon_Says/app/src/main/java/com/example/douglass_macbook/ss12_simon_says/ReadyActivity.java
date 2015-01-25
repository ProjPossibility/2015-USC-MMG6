package com.example.douglass_macbook.ss12_simon_says;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class ReadyActivity extends ActionBarActivity {

    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready);

        // Start ready signal going
        timer.schedule(sendReadySignal, 0);
    }

    protected TimerTask sendReadySignal = new TimerTask() {
        @Override
        public void run() {
            //TODO send playerNumber in the hashmap
            ParseCloud.callFunctionInBackground("ready", new HashMap<String, Object>(),
                    new FunctionCallback<HashMap<String, Object>>() {
                        @Override
                        public void done(HashMap<String, Object> result, com.parse.ParseException e) {
                            if (e == null) { // i.e. no error
                                String response = (String) result.get("response");
                                if (response.equals("wait")) {
                                    // Send the Ready signal again after half a second
                                    timer.schedule(sendReadySignal, 500);
                                } else if (response.equals("end")) {
                                    // Navigate to EndActivity
                                    endGame();
                                } else { // assume we got a valid instruction
                                    // Set the static instruction object in GameActivity
                                    GameActivity.instruction = result;

                                    // Schedule the game to start
                                    Long startTimeMilliseconds = (Long)result.get("timeStamp");
                                    if(startTimeMilliseconds != null) {
                                        // Make sure startTimeMilliseconds isn't in the past
                                        long nowMilliseconds = new Date().getTime();
                                        if(nowMilliseconds < startTimeMilliseconds) {// Schedule the start game
                                            timer.schedule(new TimerTask() {
                                                @Override
                                                public void run() {
                                                    // Navigate to GameActivity
                                                    startGame();
                                                }
                                            }, new Date(startTimeMilliseconds));
                                        }
                                        else { // i.e. startTimeMilliseconds is in the past
                                            // Navigate to GameActivity
                                            startGame();
                                        }
                                    }
                                }
                            }
                        }
                    });
        }
    };



    protected void startGame() {
        Intent myIntent = new Intent(ReadyActivity.this, GameActivity.class);
        ReadyActivity.this.startActivityForResult(myIntent, 0);
    }

    protected void endGame() {
        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        timer.schedule(sendReadySignal, 0);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ready, menu);
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
