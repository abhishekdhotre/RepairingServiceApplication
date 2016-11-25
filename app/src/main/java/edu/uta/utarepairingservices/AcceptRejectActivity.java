package edu.uta.utarepairingservices;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import edu.uta.utarepairingservices.R;
import edu.uta.utarepairingservices.ViewAppointmentStatusActivity;

public class AcceptRejectActivity extends Activity {

    String address="http://kedarnadkarny.com/utarepair/accept_appointment.php";
    BufferedReader reader = null;
    String[] accept;
    String customerID=null;
    String result = null;
    String request=null;
    String requrl=null;
    ListView lv;
    Context context;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_reject);


        //String []data =getIntent().getStringExtra("accept");
        Intent intent = getIntent();
        String data = intent.getStringExtra("accept");


        String parts[] = data.split("\\|+");

        request = parts[0];


       // TextView title = (TextView) findViewById(R.id.title);
        //title.setText(request);
        context=this;
        lv = (ListView) findViewById(R.id.listView);
        getData();
        //lv.setAdapter(new CustomAdaptor(this,accept));
       String acc[]=accept[0].split("\\|+");

        TextView title = (TextView) findViewById(R.id.title);
        title.setText("Title :"+acc[2]);

        TextView cname= (TextView) findViewById(R.id.cname);
        cname.setText("Customer Name :"+acc[1]);

        TextView date= (TextView) findViewById(R.id.date);
        date.setText("Date :"+acc[4]);

        TextView description= (TextView) findViewById(R.id.description);
        description.setText("Description :"+acc[3]);

        final Button acceptBtn= (Button) findViewById(R.id.acceptBtn);
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String status=acceptBtn.getText().toString();
                try {
                    requrl = URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(status, "UTF-8");
                    URL url = new URL(address);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoOutput(true);
                    con.setRequestMethod("POST");
                    OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                    //int i=Integer.parseInt(request);
                    wr.write(requrl);
                    wr.flush();
                }
                catch (Exception e){
                    Log.e("APP_TAG", Log.getStackTraceString(e));

                }

                Toast.makeText(getBaseContext(), "Appointment is accepted", Toast.LENGTH_SHORT ).show();

            }
        });

        final Button rejectBtn= (Button) findViewById(R.id.rejectBtn);
        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String status=rejectBtn.getText().toString();
                try {
                    requrl = URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(status, "UTF-8");
                    URL url = new URL(address);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoOutput(true);
                    con.setRequestMethod("POST");
                    OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                    //int i=Integer.parseInt(request);
                    wr.write(requrl);
                    wr.flush();
                }
                catch (Exception e){
                    Log.e("APP_TAG", Log.getStackTraceString(e));

                }

                Toast.makeText(getBaseContext(), "Appointment is rejected", Toast.LENGTH_SHORT ).show();

            }
        });







    }

    private void getData(){
        try {
            requrl = URLEncoder.encode("request","UTF-8")+"="+URLEncoder.encode(request,"UTF-8");
            URL url = new URL(address);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            //int i=Integer.parseInt(request);
            wr.write(requrl);
            wr.flush();

            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }
            result=sb.toString();


        } catch (Exception e) {

            Log.e("APP_TAG", Log.getStackTraceString(e));
        }


        //parse json data
        try {
            JSONArray ja = new JSONArray(result);
            JSONObject jo;
            accept = new String[ja.length()];
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                accept[i] = jo.getString("request_id") + " | " + jo.getString("firstname")+" "+jo.getString("lastname")+ " | " +
                        jo.getString("title") + " | " + jo.getString("description")+" | "+jo.getString("datetime")+" | "+
                       jo.getString("status");
            }

        } catch (Exception e) {
            Log.e("APP_TAG", Log.getStackTraceString(e));

        }

    }
}


