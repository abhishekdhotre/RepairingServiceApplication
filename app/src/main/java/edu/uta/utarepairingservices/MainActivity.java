package edu.uta.utarepairingservices;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Activity","This is Main Activity");
//        startActivity(new Intent(this, LoginActivity.class));
        startActivity(new Intent(this, LoginActivity.class));
    }
}
