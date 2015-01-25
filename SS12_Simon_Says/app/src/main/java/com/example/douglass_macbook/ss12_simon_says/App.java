package com.example.douglass_macbook.ss12_simon_says;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "9yqUakCzEIKfujACCHu063LqshOLUZrySAspjCO9", "rV1JpxIniI8YBl6dt5lG0bFj6r0TtE2uHUIn3Rx1");
    }
}
