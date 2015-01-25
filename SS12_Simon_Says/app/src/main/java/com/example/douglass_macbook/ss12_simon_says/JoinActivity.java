package com.example.douglass_macbook.ss12_simon_says;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.HashMap;


public class JoinActivity extends ActionBarActivity {

    Button start_button;
    String userId;
    ParseObject user;
    TextView userID;
    ListView userList;
    ArrayList<String> arrayListUsers;
    Boolean gameStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        userID = (TextView)findViewById(R.id.textView_title);
        userList = (ListView)findViewById(R.id.listView);

        //update users to list
        updateUsers();

        // Get player number
        ParseCloud.callFunctionInBackground("join",
                new HashMap<String, Object>(),
                new FunctionCallback<Integer>() {
                    @Override
                    public void done(Integer receivedPlayerNumber, com.parse.ParseException e) {
                        if (e == null) {
                            GameActivity.currentPlayerNumber = receivedPlayerNumber;
                            GameActivity.currentPlayerNumId = receivedPlayerNumber;
                            //TODO maybe: enable the start button
                        } else {
                            Toast.makeText(getApplicationContext(), "ParseException", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            //assign this to match xml button
            start_button = (Button) findViewById(R.id.button_start);
            start_button.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startGame();
                }
        });
    }

    private void updateUsers() {

        while(!gameStarted){
//            String response = (String) result.get("response");
//            if (response.equals("wait")) {
//                // Send the Ready signal again after half a second
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        sendReadySignal();
//                    }
//                }, 500);

//                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListUsers);
//              arrayListUsers = new ArrayList<String>();
//              for (int i = 0; i < arrayListUsers.length(); ++i) {
//              list.add(values[i]);
//                }
//                userList.setAdapter(adapter);
        }
    }

    private void startGame() {
        Intent myIntent = new Intent(JoinActivity.this, ReadyActivity.class);
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
