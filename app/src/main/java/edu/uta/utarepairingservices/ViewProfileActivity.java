package edu.uta.utarepairingservices;

// Use the same Activity for all three users. I know the GUI is different. You can programmatically hide and show elements

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ViewProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
    }
}
