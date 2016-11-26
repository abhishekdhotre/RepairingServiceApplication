package edu.uta.utarepairingservices;

// Home Page for our Admin

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

public class AdminHomeActivity extends AppCompatActivity {

    TextView AdminID;
    private static String ID = "";
    String role_id;
    UserInfo ui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        ui = new UserInfo();
        AdminID = (TextView) findViewById(R.id.AdminID);
        AdminID.setText("UTA ID: " + ui.getUta_net_id());
    }

    public void openAddUserActivity(View v) {
        Intent intent = new Intent(this, AddUserActivity.class);
        startActivity(intent);
    }
}
