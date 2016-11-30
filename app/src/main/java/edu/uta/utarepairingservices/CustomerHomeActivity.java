package edu.uta.utarepairingservices;

// Home Page for our Customer

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CustomerHomeActivity extends AppCompatActivity {

    private static String ID = "";
    TextView tv14;
    UserInfo ui;
    Button btnBookAppt, btnViewProfile, btnAppStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        tv14 = (TextView) findViewById(R.id.textView14);
        btnBookAppt = (Button) findViewById(R.id.btnBookApp);
        btnAppStatus = (Button) findViewById(R.id.btnAppStatus);
        ui = new UserInfo();
        tv14.setText(ui.getUta_net_id());

        btnBookAppt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerHomeActivity.this, ServiceCategoryActivity.class));
            }
        });

        btnViewProfile = (Button) findViewById(R.id.btnViewProfile);
        btnViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ViewProfileActivity.class);
                intent.putExtra("view", "view_cu");
                startActivity(intent);
            }
        });

        btnAppStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ViewAppointmentStatusActivity.class);
                intent.putExtra("view", "view_cu");
                startActivity(intent);
            }
        });
    }
}
