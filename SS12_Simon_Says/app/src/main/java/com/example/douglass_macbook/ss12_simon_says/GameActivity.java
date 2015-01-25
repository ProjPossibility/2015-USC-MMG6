package com.example.douglass_macbook.ss12_simon_says;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class GameActivity extends ActionBarActivity {

    TextView textView_instructions;
    TextView textView_p1;
    TextView textView_p2;
    TextView textView_p3;
    TextView textView_p4;
    TextView textView_round;
    int round = 1;
    boolean simonSays;
    int [] who;
    int action;
    int timeStamp;
    int currentPlayerNumber;
    int max_players = 4;
    ArrayList <Integer> arraylist;
    List<String> actionsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //setting textviews
        textView_instructions = (TextView)findViewById(R.id.textView_instruction);
        textView_p1 = (TextView)findViewById(R.id.textView_P1);
        textView_p2 = (TextView)findViewById(R.id.textView_P2);
        textView_p3 = (TextView)findViewById(R.id.textView_P3);
        textView_p4 = (TextView)findViewById(R.id.textView_P4);
        textView_round = (TextView)findViewById(R.id.textView_round);
        actionsArray = Arrays.asList( getApplicationContext().getResources().getStringArray(R.array.instructions_group) );

        ParseCloud.callFunctionInBackground("get_instruction", new HashMap<String, Object>(), new FunctionCallback<HashMap<String, Object>>() {
            @Override
            public void done(HashMap<String, Object> instruction, com.parse.ParseException e) {
                if (e == null) {
                    simonSays = (boolean)instruction.get("simonSays");
                    arraylist = (ArrayList<Integer>) instruction.get("who");
                    action = (int)instruction.get("action");
                    timeStamp = (int)instruction.get("timestamp");
                    //Date timeStampDate = (Date)instruction.get("timestamp");
                    round++;
                    handleEvents();

                } else {
                    Toast.makeText(getApplicationContext(), "Exception on server query", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void handleEvents() {
        textView_instructions.setText("");
        if(simonSays){
            textView_instructions.setText("Simon says");
        }
        if (max_players == arraylist.size()) {
            //everyone
            textView_instructions.append(" everyone");

        }
        else{
            //just some people
            for(int i = 0; i<arraylist.size(); i++ ){
                String insert = arraylist.get(i).toString();
                textView_instructions.append(" "+insert);
                if( action>=0 &&  action <=14){
                    insert = actionsArray.get(action);
                    textView_instructions.append(insert);
                }
            }
        }
        //turn sensor on
        switch(action){
            case 0:
                if(userShouldDoAction() ){
                    //needs to go left
                }
                break;
            case 1:
                if(userShouldDoAction()){
                    //needs to go right
                }
                break;
            case 2:
                if(userShouldDoAction()){
                    //needs to go up
                }
                break;
            case 3:
                if(userShouldDoAction()){
                    //needs to go down
                }
                break;
            case 4:
                if(userShouldDoAction()){
                    //needs to go punch
                }
                break;
            case 5:
                if(userShouldDoAction()){
                    //needs to elbow
                }
                break;
            case 6:
                if(userShouldDoAction()){
                    //needs to stay
                }
                break;
            case 7:
                if(userShouldDoAction()){
                    //needs to move
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
}
