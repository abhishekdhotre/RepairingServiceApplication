package edu.uta.utarepairingservices;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class AddUserActivity extends Activity {

    Spinner roleSpinner;
    EditText firstNameET, lastNameET;
    String first_name, last_name;
    Button REG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected()) {
            Toast.makeText(this, "Connected!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "No connection!", Toast.LENGTH_SHORT).show();
        }

        roleSpinner = (Spinner)findViewById(R.id.roleSpinner);
        String[] items = new String[]{"Customer", "Service Provider"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, items);
        roleSpinner.setAdapter(adapter);
        firstNameET = (EditText)findViewById(R.id.firstNameET);
        lastNameET = (EditText)findViewById(R.id.lastNameET);
        REG = (Button)findViewById(R.id.registerUser);
        REG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first_name = firstNameET.getText().toString();
                last_name = lastNameET.getText().toString();
                if(TextUtils.isEmpty(first_name)) {
                    Toast.makeText(getBaseContext(), "Please enter First Name!", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(last_name)) {
                    Toast.makeText(getBaseContext(), "Please enter Last Name!", Toast.LENGTH_SHORT).show();
                }
                else {
                    BackgroundTask backgroundTask = new BackgroundTask();
                    backgroundTask.execute(first_name, last_name);
                    finish();
                }
            }
        });
    }

    public void selectGender(View v) {
        boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId())
        {
            case R.id.radio_female:
                Toast.makeText(this, "FEMALE", Toast.LENGTH_SHORT).show();
                break;
            case R.id.radio_male:
                Toast.makeText(this, "MALE", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    class BackgroundTask extends AsyncTask<String, Void, String> {

        String add_info_url;

        @Override
        protected void onPreExecute() {
            add_info_url = "http://kedarnadkarny.com/register_user.php";
        }

        @Override
        protected String doInBackground(String... args) {
            String fname, lname;
            fname = args[0];
            lname = args[1];
            try {
                URL url = new URL(add_info_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream,"UTF-8")));
                String data_string = URLEncoder.encode("firstname","UTF-8")+"="+URLEncoder.encode(fname,"UTF-8")+"&"+
                        URLEncoder.encode("lastname","UTF-8")+"="+URLEncoder.encode(lname,"UTF-8");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();
                httpURLConnection.disconnect();
                return "One Row Inserted";
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
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(AddUserActivity.this, s, Toast.LENGTH_SHORT).show();
        }
    }

}
