package com.example.douglass_macbook.ss12_simon_says;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class ReadyActivity extends ActionBarActivity {

    Timer timer = new Timer();
    ImageView imageView_winner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready);
        imageView_winner = (ImageView)findViewById(R.id.winner_image);
        // Start ready signal going
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendReadySignal();
            }
        }, 0);
    }

    protected void sendReadySignal() {
        // Make params
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("playerNumber", GameActivity.currentPlayerNumId);

        // Call "ready" function
        ParseCloud.callFunctionInBackground("ready", params,
                new FunctionCallback<HashMap<String, Object>>() {
                    @Override
                    public void done(HashMap<String, Object> result, com.parse.ParseException e) {
                        if (e == null) { // i.e. no error
                            String response = (String) result.get("response");
                            if (response.equals("wait")) {
                                // Send the Ready signal again after half a second
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        sendReadySignal();
                                    }
                                }, 500);
                            } else if (response.equals("end")) {
                                // Navigate to EndActivity
                                endGame(result);
                            } else if (response.equals("instruction")) {
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



    protected void startGame() {
        Intent myIntent = new Intent(ReadyActivity.this, GameActivity.class);
        ReadyActivity.this.startActivityForResult(myIntent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendReadySignal();
            }
        }, 0);
    }


    protected void endGame(HashMap<String, Object> result) {
        ArrayList<HashMap<String, Object>> players = (ArrayList<HashMap<String, Object> >)result.get("players");
        if(players != null) {
            int maxScore = -1;
            int playerNumber = 0;
            for (HashMap<String, Object> player : players) {
                int score = (int) player.get("score");
                if (score > maxScore) {
                    maxScore = score;
                    playerNumber = (int) player.get("playerNumber");
                }
            }

            // playerNumber is the winner and they have a score of maxScore
            if (GameActivity.currentPlayerNumId == playerNumber) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView_winner.setVisibility(View.VISIBLE);
                    }
                });
            }
        }


        // Schedule returning back to JoinActivity
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Hide winner image
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView_winner.setVisibility(View.GONE);
                    }
                });
                finish();
            }
        }, 4500);
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
