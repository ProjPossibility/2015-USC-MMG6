package com.example.douglass_macbook.ss12_simon_says;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.HashMap;
import java.util.List;


public class JoinActivity extends ActionBarActivity {

    Button start_button;
    String userId;
    ParseObject user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        //"signing us user"
        user = new ParseObject("User");
        user.saveInBackground();
        user.put("lives", "3");
        userId = user.getObjectId();

        //Testing parse code
        ParseCloud.callFunctionInBackground("hello", new HashMap<String, Object>(), new FunctionCallback<HashMap<String, Object> >() {
            @Override
            public void done(HashMap<String, Object> map, com.parse.ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), (String)map.get("world"), Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(getApplicationContext(), "ParseException", Toast.LENGTH_LONG).show();
                }
            }
        });

        //assign this to match xml button
        start_button = (Button) findViewById(R.id.button_start);
        start_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if there are other users
                ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                        if (e == null && parseObjects.size()>1) {
                            Toast.makeText(getApplicationContext(), Integer.toString(parseObjects.size()), Toast.LENGTH_SHORT).show();
                            startGame();
                        } else {
                            Toast.makeText(getApplicationContext(), "Need at least two users!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    private void startGame() {
        Intent myIntent = new Intent(JoinActivity.this, GameActivity.class);
        JoinActivity.this.startActivity(myIntent);
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
