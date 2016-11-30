package edu.uta.utarepairingservices;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

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

public class AddUserActivity extends Activity {

    Spinner roleSpinner;
    EditText firstNameET, lastNameET, houseNoET, streetET, postalcodeET, cityET, contactET, emailET, passwordET;
    String first_name, last_name, house_no, street, postalcode, city, contact, email, role, password;
    Button REG;
    String gender = "";
    String result=null;
    InputStream is=null;
    String line=null;
    RadioButton maleRad, femaleRad;

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
        houseNoET = (EditText)findViewById(R.id.houseNoET);
        streetET = (EditText) findViewById(R.id.streetET);
        postalcodeET = (EditText) findViewById(R.id.postalcodeET);
        cityET = (EditText) findViewById(R.id.cityET);
        contactET = (EditText) findViewById(R.id.contactET);
        emailET = (EditText) findViewById(R.id.emailET);
        passwordET = (EditText) findViewById(R.id.passwordET);
        maleRad = (RadioButton) findViewById(R.id.radio_male);
        femaleRad = (RadioButton) findViewById(R.id.radio_female);


        REG = (Button)findViewById(R.id.registerUser);

        REG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first_name = firstNameET.getText().toString();
                last_name = lastNameET.getText().toString();
                house_no = houseNoET.getText().toString();
                street = streetET.getText().toString();
                postalcode = postalcodeET.getText().toString();
                city = cityET.getText().toString();
                contact = contactET.getText().toString();
                email = emailET.getText().toString();
                role = roleSpinner.getSelectedItem().toString();
                if (role.toLowerCase().equals("customer")) {
                    role = "1";
                }
                else {
                    role = "2";
                }
                password = passwordET.getText().toString();

                if(TextUtils.isEmpty(first_name)) {
                    Toast.makeText(getBaseContext(), "Please enter First Name!", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(last_name)) {
                    Toast.makeText(getBaseContext(), "Please enter Last Name!", Toast.LENGTH_SHORT).show();
                }
                else {
                    registerUser();
                }
            }
        });
    }

    public void selectGender(View v) {
        boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId())
        {
            case R.id.radio_female:
                gender = "female";
                break;
            case R.id.radio_male:
                gender = "male";
                break;
        }
    }

    public void registerUser() {
        try {
            String address = "http://kedarnadkarny.com/utarepair/register_user.php";
            URL url = new URL(address);
            HttpURLConnection con=(HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            OutputStream outputStream = con.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream,"UTF-8")));
            String data_string = URLEncoder.encode("firstname","UTF-8")+"="+URLEncoder.encode(first_name,"UTF-8")+"&"+
                    URLEncoder.encode("lastname","UTF-8")+"="+URLEncoder.encode(last_name,"UTF-8")+"&"+
                    URLEncoder.encode("gender","UTF-8")+"="+URLEncoder.encode(gender,"UTF-8")+"&"+
                    URLEncoder.encode("house_no","UTF-8")+"="+URLEncoder.encode(house_no,"UTF-8")+"&"+
                    URLEncoder.encode("street","UTF-8")+"="+URLEncoder.encode(street,"UTF-8")+"&"+
                    URLEncoder.encode("postalcode","UTF-8")+"="+URLEncoder.encode(postalcode,"UTF-8")+"&"+
                    URLEncoder.encode("city","UTF-8")+"="+URLEncoder.encode(city,"UTF-8")+"&"+
                    URLEncoder.encode("contact","UTF-8")+"="+URLEncoder.encode(contact,"UTF-8")+"&"+
                    URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
                    URLEncoder.encode("role","UTF-8")+"="+URLEncoder.encode(role,"UTF-8")+"&"+
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
            if(result.equals("ADDED")) {
                Toast.makeText(getBaseContext(), "User Registered!", Toast.LENGTH_LONG);
                clearForm((ViewGroup)findViewById(R.id.rootLayout));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void clearForm(ViewGroup group)
    {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).setText("");
            }

            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
                clearForm((ViewGroup)view);
        }
        maleRad.setChecked(false);
        femaleRad.setChecked(false);
    }
}
