package edu.uta.utarepairingservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
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

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText utaNetIdET, passwordET;
    String utaNetId, password, roleID;
    String result=null;
    String[] data;
    ProgressDialog progressDialog;
    InputStream is=null;
    String line=null;
    UserInfo ui;
    String s="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        utaNetIdET = (EditText) findViewById(R.id.utaNetIDET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setTitle("Authorizing");
        progressDialog.setMessage("Loading Profile...");
        btnLogin = (Button) findViewById(R.id.buttonLogin);
        ui = new UserInfo();
        s = getIntent().getStringExtra("logout");

        try {
            if(s.equals("logout") || !s.isEmpty()) {
                Toast.makeText(getBaseContext(), "You have been logged out!", Toast.LENGTH_LONG).show();
            }
        }

        catch(NullPointerException e) {
            e.printStackTrace();
        }

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
                    StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
                    checkLogin();
                }
            }
        });
    }

    public void checkLogin() {
        try {
            String address = "http://kedarnadkarny.com/utarepair/login_user.php";
            URL url = new URL(address);
            HttpURLConnection con=(HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            OutputStream outputStream = con.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream,"UTF-8")));
            String data_string = URLEncoder.encode("utanetid","UTF-8")+"="+URLEncoder.encode(utaNetId,"UTF-8")+"&"+
                    URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
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
        }
        catch (Exception e){
            e.printStackTrace();
        }

        //parse json data
        try{
            if(!result.equals("failed")) {
                JSONArray ja=new JSONArray(result);
                JSONObject jo;
                data=new String[ja.length()];
                jo=ja.getJSONObject(0);
                utaNetId = jo.getString("uta_net_id");
                roleID = jo.getString("role_id");
                Log.d("response", utaNetId + " " + roleID);

                UserInfo ui = new UserInfo();
                ui.setUta_net_id(utaNetId);
                ui.setRoleId(roleID);
                if(roleID.equals(null)){
                    Toast.makeText(getBaseContext(), "Null Pointer", Toast.LENGTH_SHORT).show();
                }
                else if(roleID.equals("1")) {
                    Intent intent = new Intent(getBaseContext(), CustomerHomeActivity.class);
                    ui.setLoginMessage("login");
                    intent.putExtra("login","login");
                    startActivity(intent);
                }
                else if (roleID.equals("2")) {
                    Intent intent = new Intent(getBaseContext(), ServiceProviderHomeActivity.class);
                    ui.setLoginMessage("login");
                    intent.putExtra("login","login");
                    startActivity(intent);
                }
                else if (roleID.equals("3")) {
                    Intent intent = new Intent(getBaseContext(), AdminHomeActivity.class);
                    ui.setLoginMessage("login");
                    intent.putExtra("login","login");
                    startActivity(intent);
                }
            }
            else {
                Toast.makeText(getBaseContext(), "Wrong Credentials!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}