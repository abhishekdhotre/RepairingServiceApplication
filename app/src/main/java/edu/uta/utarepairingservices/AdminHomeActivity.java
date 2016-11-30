package edu.uta.utarepairingservices;

// Home Page for our Admin

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

public class AdminHomeActivity extends AppCompatActivity {

    TextView AdminID;
    UserInfo ui;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        ui = new UserInfo();
        AdminID = (TextView) findViewById(R.id.AdminID);
        AdminID.setText("UTA ID: " + ui.getUta_net_id());
        btnLogout = (Button) findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), LoginActivity.class);
                i.putExtra("logout", "logout");
                startActivity(i);
            }
        });
    }

    public void openAddUserActivity(View v) {
        Intent intent = new Intent(this, AddUserActivity.class);
        startActivity(intent);
    }

    public void openDeleteUserActivity(View v) {
        Intent intent = new Intent(this, DeleteUserActivity.class);
        startActivity(intent);
    }
}
