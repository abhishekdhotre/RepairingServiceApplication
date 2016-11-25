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
    String result=null;
    String[] data;
    String uta_net_id;
    String role_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        AdminID = (TextView) findViewById(R.id.AdminID);
        String s;
        s = getIntent().getStringExtra("data");
        AdminID.setText("ID: " + s);
        String uta_net_id = null, role_id = null;
        //parse json data
        try{
            JSONArray ja=new JSONArray(s);
            JSONObject jo;
            data=new String[ja.length()];
                jo=ja.getJSONObject(0);
                uta_net_id = jo.getString("uta_net_id");
                role_id = jo.getString("role_id");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        AdminID.setText("UTA ID: " + uta_net_id + " Role ID:  " + role_id);
    }

    public void openAddUserActivity(View v) {
        Intent intent = new Intent(this, AddUserActivity.class);
        startActivity(intent);
    }
}
