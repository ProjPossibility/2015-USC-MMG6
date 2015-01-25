package com.example.douglass_macbook.ss12_simon_says;

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


public class ReadyActivity extends ActionBarActivity {

    boolean simonSays;
    ArrayList <Integer> arraylist;
    int action;
    long timeStamp;
    Date now;
    long millis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready);


        ParseCloud.callFunctionInBackground("get_instruction",
                new HashMap<String, Object>(),
                new FunctionCallback<HashMap<String, Object>>() {
                    @Override
                    public void done(HashMap<String, Object> instruction, com.parse.ParseException e) {
                        if (e == null) {
                            simonSays = (boolean) instruction.get("simonSays");
                            arraylist = (ArrayList<Integer>) instruction.get("who");
                            action = (int) instruction.get("action");
                            timeStamp = (long) instruction.get("timeStamp");
                            //Date timeStampDate = (Date)instruction.get("timestamp");

                            //SET PLAYER NUM ID
                            //handleEvents();

                        } else {
                            Toast.makeText(getApplicationContext(), "Exception on server query", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        now = new Date();
        millis = now.getTime();

        //get now time see if greater - if greater do immediately , else wait for time
        if(millis>timeStamp){
            //do immediately(go to gameActivity
        } else {
            //wait for time

            now = new Date();
            long millis2 = now.getTime();



        }

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
