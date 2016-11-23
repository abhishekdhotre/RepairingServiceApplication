package edu.uta.utarepairingservices;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import edu.uta.utarepairingservices.R;
import edu.uta.utarepairingservices.ViewAppointmentStatusActivity;

public class AcceptRejectActivity extends Activity {

    String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_reject);
        data=getIntent().getStringExtra(ViewAppointmentStatusActivity.accept);
        String parts[]=data.split(" ");

        TextView title=(TextView)findViewById(R.id.title);
        title.setText(parts[1]);
    }
}
