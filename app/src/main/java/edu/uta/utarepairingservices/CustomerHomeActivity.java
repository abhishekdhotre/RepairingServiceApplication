package edu.uta.utarepairingservices;

// Home Page for our Customer

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class CustomerHomeActivity extends AppCompatActivity {

    private static String ID = "";
    TextView tv14;
    UserInfo ui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        tv14 = (TextView) findViewById(R.id.textView14);
        ui = new UserInfo();
        tv14.setText(ui.getUta_net_id());
    }
}
