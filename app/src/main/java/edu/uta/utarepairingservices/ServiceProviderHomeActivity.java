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
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_home);
        btnAppStatus = (Button) findViewById(R.id.btnAppStatus);
        btnViewProfile = (Button) findViewById(R.id.btnViewProfile);
        String message = "";
        message = ui.getLoginMessage();
        if(message.equals("login")) {
            Toast.makeText(this, "Login Successful!", Toast.LENGTH_LONG).show();
            ui.setLoginMessage("");
        }

        message = ui.getMessage();
        if(message!="") {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }

        btnAppStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ViewAppointmentStatusActivity.class);
                intent.putExtra("view", "view_sp");
                startActivity(intent);
            }
        });

        btnLogout = (Button) findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), LoginActivity.class);
                i.putExtra("logout", "logout");
                startActivity(i);
            }
        });

        btnViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), ViewProfileActivity.class);
                        i.putExtra("view", "view_sp");
                startActivity(i);
            }
        });
    }
}
