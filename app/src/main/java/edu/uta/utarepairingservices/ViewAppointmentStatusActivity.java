package edu.uta.utarepairingservices;

// This Activity is for the Customer to see history of appointments and their status

import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class ViewAppointmentStatusActivity extends Activity {

    ListView lv;
    ArrayAdapter<String> adapter;
    String address="http://kedarnadkarny.com/utarepair/appointment_status_spv.php";
    InputStream is=null;
    String line=null;
    String result=null;
    HashMap hm;
    public static String accept;
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
        lv.setOnItemClickListener(onListClick);

    }

    private AdapterView.OnItemClickListener onListClick=new AdapterView.OnItemClickListener(){
        public void onItemClick(AdapterView<?>parent,View view,int position,long id){
            Intent i=new Intent(ViewAppointmentStatusActivity.this, AcceptRejectActivity.class);
           // i.putExtra(accept,data);
            String val= (String) parent.getItemAtPosition(position);
            i.putExtra("accept",val);
            startActivity(i);


        }

    };
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
            hm=new HashMap();
            data=new String[ja.length()];
            for(int i=0;i<ja.length();i++){
                jo=ja.getJSONObject(i);
                hm.put("customer_id",jo.getString("customer_id"));
                hm.put("title",jo.getString("title"));
                data[i]=jo.getString("request_id")+" | "+jo.getString("customer_id")+" | "+jo.getString("title")+" | "+jo.getString("status");
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }








}
