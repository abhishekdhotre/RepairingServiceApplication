package edu.uta.utarepairingservices;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.Objects;

public class ViewAppointment extends AppCompatActivity {

    TextView txtTitle, txtDescription, txtPosted, txtStatus;
    Button btnCancel, btnRate, btnAccept, btnReject, btnComplete;
    UserInfo ui;
    int requestID;
    InputStream is=null;
    String line=null;
    String result=null;
    String s;
    String title, description, datetime, fullname;
    private static String status;
    String requrl=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtPosted = (TextView) findViewById(R.id.txtPosted);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnRate = (Button) findViewById(R.id.btnRate);
        btnAccept = (Button) findViewById(R.id.btnAccept);
        btnReject = (Button) findViewById(R.id.btnReject);
        btnComplete = (Button) findViewById(R.id.btnComplete);
        btnComplete.setVisibility(View.GONE);
        s = getIntent().getStringExtra("view");
        ui = new UserInfo();
        requestID = ui.getRequestID();

        getData();
        txtTitle.setText(title);
        txtDescription.setText(description);
        txtStatus.setText(status);
        txtPosted.setText(datetime);

        if(s.equals("view_sp")) {
            btnAccept.setVisibility(View.GONE);
            btnReject.setVisibility(View.GONE);
            btnRate.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            btnComplete.setVisibility(View.GONE);
            if(status.equals("Pending")) {
                btnAccept.setVisibility(View.VISIBLE);
                btnReject.setVisibility(View.VISIBLE);
            }
            if(status.equals("Accept")) {
                btnComplete.setVisibility(View.VISIBLE);
            }
            if(status.equals("Reject") || status.equals("Complete")) {
                btnAccept.setVisibility(View.GONE);
                btnReject.setVisibility(View.GONE);
            }
        }
        else if(s.equals("view_cu")) {
            btnAccept.setVisibility(View.GONE);
            btnReject.setVisibility(View.GONE);
            btnRate.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            btnComplete.setVisibility(View.GONE);

            if (status.equals("Pending")){
                btnCancel.setVisibility(View.VISIBLE);
            }

            if (status.equals("Accept")) {
                Toast.makeText(getBaseContext(), "Job Accepted by Service Provider!", Toast.LENGTH_LONG).show();
            }

            if(status.equals("Reject")) {
                Toast.makeText(getBaseContext(), "Job Rejected by Service Provider!", Toast.LENGTH_LONG).show();
            }

            if(status.equals("Complete")) {
                btnRate.setVisibility(View.VISIBLE);
            }
        }



        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAppointment();
            }
        });

        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewAppointment.this, ProvideRatingActivity.class);
                i.putExtra("fullname", fullname);
                startActivity(i);
            }
        });

        // Accept Appointment
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String address = "http://kedarnadkarny.com/utarepair/accept_appointment.php";
                    URL url = new URL(address);
                    HttpURLConnection con=(HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream,"UTF-8")));
                    String data_string = URLEncoder.encode("request_id","UTF-8")+"="+URLEncoder.encode(String.valueOf(requestID),"UTF-8");
                    bufferedWriter.write(data_string);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    is=new BufferedInputStream(con.getInputStream());
                    BufferedReader br=new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb=new StringBuilder();

                    while((line=br.readLine())!=null){
                        sb.append(line);
                    }
                    is.close();
                    result=sb.toString();
                    if(result.equals("UPDATED")) {
                        Toast.makeText(getBaseContext(), "Appointment Accepted!", Toast.LENGTH_LONG);
                        ui.setMessage("Appointment Accepted!");
                        startActivity(new Intent(getBaseContext(), ServiceProviderHomeActivity.class));
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        // Reject
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String address = "http://kedarnadkarny.com/utarepair/reject_appointment.php";
                    URL url = new URL(address);
                    HttpURLConnection con=(HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream,"UTF-8")));
                    String data_string = URLEncoder.encode("request_id","UTF-8")+"="+URLEncoder.encode(String.valueOf(requestID),"UTF-8");
                    bufferedWriter.write(data_string);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    is=new BufferedInputStream(con.getInputStream());
                    BufferedReader br=new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb=new StringBuilder();

                    while((line=br.readLine())!=null){
                        sb.append(line);
                    }
                    is.close();
                    result=sb.toString();
                    if(result.equals("UPDATED")) {
                        Toast.makeText(getBaseContext(), "Appointment Rejected!", Toast.LENGTH_LONG).show();
                        ui.setMessage("Appointment Rejected!");
                        startActivity(new Intent(getBaseContext(), ServiceProviderHomeActivity.class));
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String address = "http://kedarnadkarny.com/utarepair/complete_appointment.php";
                    URL url = new URL(address);
                    HttpURLConnection con=(HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream,"UTF-8")));
                    String data_string = URLEncoder.encode("request_id","UTF-8")+"="+URLEncoder.encode(String.valueOf(requestID),"UTF-8");
                    bufferedWriter.write(data_string);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    is=new BufferedInputStream(con.getInputStream());
                    BufferedReader br=new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb=new StringBuilder();

                    while((line=br.readLine())!=null){
                        sb.append(line);
                    }
                    is.close();
                    result=sb.toString();
                    if(result.equals("UPDATED")) {
                        Toast.makeText(getBaseContext(), "Appointment Completed!", Toast.LENGTH_LONG).show();
                        ui.setMessage("Appointment Completed!");
                        startActivity(new Intent(getBaseContext(), ServiceProviderHomeActivity.class));
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

//end of creator
    }

    public void cancelAppointment() {
        try {
            URL url = new URL("http://kedarnadkarny.com/utarepair/cancel_appointment.php");
            HttpURLConnection con=(HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            OutputStream outputStream = con.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream,"UTF-8")));
            String data_string = URLEncoder.encode("request_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(requestID), "UTF-8");
            bufferedWriter.write(data_string);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            is=new BufferedInputStream(con.getInputStream());
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            StringBuilder sb=new StringBuilder();

            while((line=br.readLine())!=null){
                sb.append(line);

            }
            is.close();
            result=sb.toString();
            if(result.equals("UPDATED")) {
                btnCancel.setVisibility(View.GONE);
                txtStatus.setText("Cancelled!");
                Toast.makeText(getBaseContext(),"Appointment Cancelled!", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getData() {
        try {
            URL url = new URL("http://kedarnadkarny.com/utarepair/single_appointment.php");
            HttpURLConnection con=(HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            OutputStream outputStream = con.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream,"UTF-8")));
            String data_string = "";

            data_string = URLEncoder.encode("request_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(requestID), "UTF-8");


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

            try {
                url = new URL("http://kedarnadkarny.com/utarepair/view_appointment_image.php?request_id="+requestID);
                InputStream in = url.openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                ImageView img = (ImageView) findViewById(R.id.imageView2);
                img.setImageBitmap(bitmap);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        catch (Exception e){
            e.printStackTrace();

        }

        //parse json data
        try{
            JSONObject jo = new JSONObject(result);
            int length = jo.length();
            if(s.equals("view_cu")) {
            int reqID=0;
                for(int i=0;i<length;i++){
                    reqID = Integer.parseInt(jo.getString("request_id"));
                    title = jo.getString("title");
                    description = jo.getString("description");
                    status = jo.getString("status");
                    datetime = jo.getString("datetime");
                    fullname = jo.getString("firstname") +" "+ jo.getString("lastname");
                }
                Log.d("response", reqID + " " + title + " " + description + " " + status + " " + datetime);
            }
            else if(s.equals("view_sp")) {
                for(int i=0;i<length;i++){
                    title = jo.getString("title");
                    description = jo.getString("description");
                    status = jo.getString("status");
                    datetime = jo.getString("datetime");
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
