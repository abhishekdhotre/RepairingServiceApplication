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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class ViewAppointmentStatusActivity extends Activity {

    ListView lv;
    ArrayAdapter<String> adapter;
    String address="";
    InputStream is=null;
    String line=null;
    String result=null;
    HashMap hm;
    String[] data;
    String[] requestIDList;
    TextView infobox;
    String s;
    UserInfo ui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment_status);
        lv = (ListView) findViewById(R.id.listView);
        ui = new UserInfo();

        s = getIntent().getStringExtra("view");
        if(s.equals("view_sp")) {
            address="link";
        }
        else if(s.equals("view_cu")) {
            address="link";
        }
        else {
            Toast.makeText(getBaseContext(), "SOME ERROR", Toast.LENGTH_LONG).show();
        }

        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        getData();

        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(onListClick);
        ui = new UserInfo();
        infobox = (TextView) findViewById(R.id.infobox);
        infobox.setText(ui.getUta_net_id());
        infobox.setVisibility(View.GONE);
    }

    private AdapterView.OnItemClickListener onListClick=new AdapterView.OnItemClickListener(){
        public void onItemClick(AdapterView<?>parent,View view,int position,long id){
            if(s.equals("view_sp")) {
                ui.setRequestID(Integer.parseInt(requestIDList[position]));
                Intent i=new Intent(ViewAppointmentStatusActivity.this, ViewAppointment.class);
                i.putExtra("view", "view_sp");
                startActivity(i);
            }
            else if(s.equals("view_cu")) {
                ui.setRequestID(Integer.parseInt(requestIDList[position]));
                Intent i=new Intent(ViewAppointmentStatusActivity.this, ViewAppointment.class);
                i.putExtra("view", "view_cu");
                startActivity(i);
            }
        }
    };
    private  void getData(){

        try {
            URL url = new URL(address);
            HttpURLConnection con=(HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            OutputStream outputStream = con.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream,"UTF-8")));
            String data_string = "";
            if(s.equals("view_cu")) {
                data_string = URLEncoder.encode("uta_net_id", "UTF-8") + "=" + URLEncoder.encode(ui.getUta_net_id(), "UTF-8");
            }
            else if(s.equals("view_sp")) {
                data_string = URLEncoder.encode("uta_net_id", "UTF-8") + "=" + URLEncoder.encode(ui.getUta_net_id(), "UTF-8");
            }

            bufferedWriter.write(data_string);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            is=new BufferedInputStream(con.getInputStream());
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
            requestIDList=new String[ja.length()];
            if(s.equals("view_cu")) {
                for(int i=0;i<ja.length();i++){
                    jo=ja.getJSONObject(i);
                    data[i]=jo.getString("request_id")+" | "+jo.getString("title")+" | "+jo.getString("status");
                    requestIDList[i] = jo.getString("request_id");
                }
            }
            else if(s.equals("view_sp")) {
                for(int i=0;i<ja.length();i++){
                    jo=ja.getJSONObject(i);
                    data[i]=jo.getString("request_id")+" | "+jo.getString("title")+" | "+jo.getString("status");
                    requestIDList[i] = jo.getString("request_id");
                }
            }


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
