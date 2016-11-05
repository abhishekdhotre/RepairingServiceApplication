package edu.uta.utarepairingservices;

// Home Page for our Admin

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AdminHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
    }

    public void openAddUserActivity(View v) {
        Intent intent = new Intent(this, AddUserActivity.class);
        startActivity(intent);
    }
}
