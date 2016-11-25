package edu.uta.utarepairingservices;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

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
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText utaNetIdET, passwordET;
    String utaNetId, password;
    AlertDialog alertDialog;
    String result=null;
    String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        utaNetIdET = (EditText) findViewById(R.id.utaNetIDET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        btnLogin = (Button) findViewById(R.id.buttonLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utaNetId = utaNetIdET.getText().toString();
                password = passwordET.getText().toString();
                if(TextUtils.isEmpty(utaNetId)) {
                    Toast.makeText(getBaseContext(), "Please enter UTA NET ID!", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(password)) {
                    Toast.makeText(getBaseContext(), "Please enter Password!", Toast.LENGTH_SHORT).show();
                }
                else {
                    BackgroundTask backgroundTask = new BackgroundTask();
                    backgroundTask.execute(utaNetId, password);
                    finish();
                }
            }
        });
    }

    class BackgroundTask extends AsyncTask<String, Void, String> {

        String add_info_url;
        Context ctx;

        public BackgroundTask(Context ctx) {
            this.ctx = ctx;
        }

        public BackgroundTask() {
            int x=0;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*alertDialog = new AlertDialog.Builder(ctx).create();
            alertDialog.setTitle("Login Information...");*/
            add_info_url = "http://kedarnadkarny.com/utarepair/login_user.php";
        }

        @Override
        protected String doInBackground(String... args) {
            String utanetid, password;
            utanetid = args[0];
            password = args[1];
            try {
                URL url = new URL(add_info_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream,"UTF-8")));
                String data_string = URLEncoder.encode("utanetid","UTF-8")+"="+URLEncoder.encode(utanetid,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder sb=new StringBuilder();
                String response = "";
                String line = "";
                while((line=bufferedReader.readLine())!=null){
                    sb.append(line +"\n");

                }
                result=sb.toString();
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPreExecute();
            //Log.d("response", s);
            if(!s.equals("failed")) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUta_net_id(utaNetId);
                Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();

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
                UserInfo ui = new UserInfo();
                ui.setUta_net_id(uta_net_id);
                if(role_id.equals(null)){
                    Toast.makeText(getBaseContext(), "Null Pointer", Toast.LENGTH_SHORT).show();
                }
                else if(role_id.equals("1")) {
                    ui.setIDForCustomer();
                    Intent intent = new Intent(getBaseContext(), CustomerHomeActivity.class);
                    startActivity(intent);
                }
                else if (role_id.equals("2")) {
                    ui.setIDForServiceProvider();
                    Intent intent = new Intent(getBaseContext(), ServiceProviderHomeActivity.class);
                    startActivity(intent);
                }
                else if (role_id.equals("3")) {
                    ui.setIDForAdmin();
                    Intent intent = new Intent(getBaseContext(), AdminHomeActivity.class);
                    startActivity(intent);
                }

            }
            else {
                Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(getBaseContext(), "Paused", Toast.LENGTH_SHORT).show();
    }
}
