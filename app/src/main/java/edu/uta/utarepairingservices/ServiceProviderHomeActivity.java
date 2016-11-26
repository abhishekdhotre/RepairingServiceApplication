package edu.uta.utarepairingservices;

// Home Page for our Service Provider

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ServiceProviderHomeActivity extends AppCompatActivity {

    Button btnAppStatus;

    private static String uta_net_id = "";
    UserInfo ui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_home);
        btnAppStatus = (Button) findViewById(R.id.btnAppStatus);
        btnAppStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), ViewAppointmentStatusActivity.class));
            }
        });
    }
}
