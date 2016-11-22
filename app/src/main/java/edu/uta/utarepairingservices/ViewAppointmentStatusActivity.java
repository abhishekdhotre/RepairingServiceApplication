package edu.uta.utarepairingservices;

// This Activity is for the Customer to see history of appointments and their status

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewAppointmentStatusActivity extends Activity {

    ListView lv;
    ArrayAdapter<String> adapter;
    String address="http://kedarnadkarny.com/utarepair/appointment_status_spv.php";
    InputStream is=null;
    String line=null;
    String result=null;
    String[]data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment_status);
        lv = (ListView) findViewById(R.id.listView);

        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        getData();

        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        lv.setAdapter(adapter);
    }

    private  void getData(){

        try {
            URL url = new URL(address);
            HttpURLConnection con=(HttpURLConnection) url.openConnection();
            is=new BufferedInputStream(con.getInputStream());

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            StringBuilder sb=new StringBuilder();
            while((line=br.readLine())!=null){
                sb.append(line +"\n");

            }
            is.close();
            result=sb.toString();


        }
        catch (Exception e){
            e.printStackTrace();

        }

        //parse json data
        try{
            JSONArray ja=new JSONArray(result);
            JSONObject jo;
            data=new String[ja.length()];
            for(int i=0;i<ja.length();i++){
                jo=ja.getJSONObject(i);
                data[i]=jo.getString("customer_id")+"  "+jo.getString("title");
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


}
