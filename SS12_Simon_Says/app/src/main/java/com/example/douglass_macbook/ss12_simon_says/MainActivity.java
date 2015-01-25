package com.example.douglass_macbook.ss12_simon_says;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.parse.Parse;

public class MainActivity extends ActionBarActivity {

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
                MainActivity.this.startActivity(myIntent);
            }
        });

        button_help = (Button)findViewById(R.id.button_help);
        button_help.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, ActivityInstruction.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        // Parse stuff:
        // Enable Local Datastore.
        // We only want these two lines to be called one time
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "9yqUakCzEIKfujACCHu063LqshOLUZrySAspjCO9", "rV1JpxIniI8YBl6dt5lG0bFj6r0TtE2uHUIn3Rx1");
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
