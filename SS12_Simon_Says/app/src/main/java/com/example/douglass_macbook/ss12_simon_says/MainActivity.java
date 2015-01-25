package com.example.douglass_macbook.ss12_simon_says;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.parse.FunctionCallback;
import com.parse.Parse;
import com.parse.ParseCloud;

import java.util.HashMap;
import java.util.TimerTask;

public class MainActivity extends ActionBarActivity {

    MediaPlayer mMediaPlayer;
    Button button_startGame;
    Button button_help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_startGame = (Button)findViewById(R.id.button_startgame);
        button_startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, JoinActivity.class);
                MainActivity.this.startActivityForResult(myIntent, 1);
            }
        });

        button_help = (Button)findViewById(R.id.button_help);
        button_help.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, ActivityInstruction.class);
                MainActivity.this.startActivityForResult(myIntent, 1);
            }
        });

        // Play welcome message
        mMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.welcome);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mMediaPlayer.release();
            }
        });
        mMediaPlayer.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Call "leave" function when this page is navigated to from either the Join, Ready, or Game pages
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("playerNumber", GameActivity.currentPlayerNumId);
        ParseCloud.callFunctionInBackground("leave", params,
                new FunctionCallback<HashMap<String, Object> >() {
                    @Override
                    public void done(HashMap<String, Object> result, com.parse.ParseException e) {
                        // Do nothing
                    }
                });
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
