package com.example.douglass_macbook.ss12_simon_says;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class JoinActivity extends ActionBarActivity {

    Button start_button;
    String userId;
    ParseObject user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        // Parse stuff:
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "9yqUakCzEIKfujACCHu063LqshOLUZrySAspjCO9", "rV1JpxIniI8YBl6dt5lG0bFj6r0TtE2uHUIn3Rx1");

        //"signing us user"
        user = new ParseObject("User");
        user.saveInBackground();
        user.put("foo", "bar");
        userId = user.getObjectId();

        //assign this to match xml button
        start_button = (Button) findViewById(R.id.button_start);
        start_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if there are other users
                //Parse
                ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
                query.whereEqualTo("foo", "bar");
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                        if (e == null && parseObjects.size()>1) {
                            Toast.makeText(getApplicationContext(), parseObjects.size(), Toast.LENGTH_SHORT);
                            Log.d("score", "Retrieved " + parseObjects.size() + " scores");
                        } else {
                            Log.d("score", "Error: " + e.getMessage());
                        }
                    }
                });
                //Start the game
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_join, menu);
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
