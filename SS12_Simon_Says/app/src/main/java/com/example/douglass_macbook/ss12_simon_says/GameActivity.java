package com.example.douglass_macbook.ss12_simon_says;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;

import java.util.HashMap;


public class GameActivity extends ActionBarActivity {

    TextView textView_instructions;
    TextView textView_p1;
    TextView textView_p2;
    TextView textView_p3;
    TextView textView_p4;
    TextView textView_round;

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

        ParseCloud.callFunctionInBackground("hello", new HashMap<String, Object>(), new FunctionCallback<String>() {
            @Override
            public void done(String s, com.parse.ParseException e) {
                if (e == null) {
                    // result is "Hello world!"
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

                }
            }
        });

        //JSON CODE
//        //getting
//        try {
//            //URL url = new URL ("http://SOMETHING.json");
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.connect();
//            // gets the server json data
//            BufferedReader bufferedReader =
//                    new BufferedReader(new InputStreamReader(
//                            urlConnection.getInputStream()));
//            String next;
//            while ((next = bufferedReader.readLine()) != null){
//                JSONArray ja = new JSONArray(next);
//                for (int i = 0; i < ja.length(); i++) {
//                    JSONObject jo = (JSONObject) ja.get(i);
//                    //items.add(jo.getString("text"));
//                }
//            }
//        } catch (MalformedURLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
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
