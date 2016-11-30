package edu.uta.utarepairingservices;

// Home Page for our Service Provider

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ServiceProviderHomeActivity extends AppCompatActivity {

    Button btnAppStatus, btnViewProfile;

    private static String uta_net_id = "";
    UserInfo ui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_home);
        btnAppStatus = (Button) findViewById(R.id.btnAppStatus);
        btnViewProfile = (Button) findViewById(R.id.btnViewProfile);
        btnAppStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "C1", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(getBaseContext(), ViewAppointmentStatusActivity.class));
            }
        });

        btnViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "C2", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(getBaseContext(), ViewProfileActivity.class));
            }
        });
    }
}
