package edu.uta.utarepairingservices;

// Use the same Activity for all three users. I know the GUI is different. You can programmatically hide and show elements

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import android.widget.ArrayAdapter;
import android.os.StrictMode;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


public class ViewProfileActivity extends Activity {
    String UserId;

    ArrayAdapter<String> adapter;
    int RoleId;
    String address;
    InputStream is=null;
    String line=null;
    String result=null;
    String data;
    String netId;
    UserInfo user;
    Button btnBook;
    int spID;
    String s;
    UserInfo ui;

    String name, gender, email, street, city;
    int contact, houseNo, postal;
    float rating;

    TextView tvNameValue, tvGenderValue, tvContactValue, tvEmailValue, tvHouseNoValue, tvStreetValue, tvPostalCodeValue, tvCityValue, tvViewProfileText, tvRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_view_profile);

        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        /*tvNameValue = (TextView) findViewById(R.id.tvNameValue);
        tvGenderValue = (TextView) findViewById(R.id.tvGenderValue);
        tvContactValue = (TextView) findViewById(R.id.tvContactValue);
        tvEmailValue = (TextView) findViewById(R.id.tvEmailValue);
        tvHouseNoValue = (TextView) findViewById(R.id.tvHouseNoValue);
        tvStreetValue = (TextView) findViewById(R.id.tvStreetValue);
        tvPostalCodeValue = (TextView) findViewById(R.id.tvPostalCodeValue);
        tvCityValue = (TextView) findViewById(R.id.tvCityValue);
        tvViewProfileText = (TextView) findViewById(R.id.tvViewProfileText);
        tvRating = (TextView) findViewById(R.id.tvRating);*/

        btnBook = (Button) findViewById(R.id.btnBookAppointment);

        ui = new UserInfo();
        s = getIntent().getStringExtra("view");
        if(s.equals("view_sp")) {
            //tvViewProfileText.setText("Service Provider Profile");
            address = "link";
            netId = ui.getUta_net_id();
            RoleId = Integer.parseInt(UserInfo.getRoleId());
            btnBook.setVisibility(View.GONE);
        }
        else if(s.equals("view_sp2")) {
            //tvViewProfileText.setText("Service Provider Profile");
            netId = String.valueOf(ui.getSpID());
            RoleId = 2;
            address = "link";
            btnBook.setVisibility(View.VISIBLE);
        }
        else if(s.equals("view_cu")) {
            address = "link";
            btnBook.setVisibility(View.GONE);
            RoleId = Integer.parseInt(UserInfo.getRoleId());
            netId = UserInfo.getUta_net_id();
        }
        else {
            Toast.makeText(getBaseContext(), "SOME ERROR", Toast.LENGTH_LONG).show();
        }

        getData();
        //setProfileData();

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewProfileActivity.this, BookAppointmentActivity.class));
            }
        });
    }

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
                data_string = URLEncoder.encode("UserId", "UTF-8") + "=" + URLEncoder.encode(netId, "UTF-8");
            }
            else if(s.equals("view_sp")) {
                data_string = URLEncoder.encode("UserId", "UTF-8") + "=" + URLEncoder.encode(netId, "UTF-8");
            }
            else if(s.equals("view_sp2")) {
                data_string = URLEncoder.encode("SpId", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(ui.getSpID()), "UTF-8");
            }

            bufferedWriter.write(data_string);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
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
            br.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        //parse json data
        try{
            JSONArray ja=new JSONArray(result);
            JSONObject jo;
            data=new String();

            jo=ja.getJSONObject(0);

            if(RoleId==1) {

                TextView textViewToChange = (TextView) findViewById(R.id.tvViewProfileText);
                textViewToChange.setText("Customer" + " " + "Profile");

                textViewToChange = (TextView) findViewById(R.id.tvNameValue);
                textViewToChange.setText(jo.getString("firstname") +" "+ jo.getString("lastname"));

                textViewToChange = (TextView) findViewById(R.id.tvGenderValue);
                textViewToChange.setText(jo.getString("gender"));

                textViewToChange = (TextView) findViewById(R.id.tvHouseNoValue);
                textViewToChange.setText(jo.getString("house_no"));

                textViewToChange = (TextView) findViewById(R.id.tvStreetValue);
                textViewToChange.setText(jo.getString("street"));

                textViewToChange = (TextView) findViewById(R.id.tvPostalCodeValue);
                textViewToChange.setText(jo.getString("postal_code"));

                textViewToChange = (TextView) findViewById(R.id.tvCityValue);
                textViewToChange.setText(jo.getString("city"));

                textViewToChange = (TextView) findViewById(R.id.tvContactValue);
                textViewToChange.setText(jo.getString("contact"));

                textViewToChange = (TextView) findViewById(R.id.tvEmailValue);
                textViewToChange.setText(jo.getString("email"));

                try {
                    URL url = new URL("link?UserId="+netId+"&RoleId="+RoleId);
                    InputStream in = url.openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    ImageView img = (ImageView) findViewById(R.id.imgProfile);
                    img.setImageBitmap(bitmap);
                }
                catch(Exception e){
                    e.printStackTrace();
                }

                //Set Visibility for Customer
                textViewToChange = (TextView) findViewById(R.id.tvRatingText);
                textViewToChange.setVisibility(View.INVISIBLE);
                RatingBar rb = (RatingBar) findViewById(R.id.ratingBar);
                rb.setVisibility(View.INVISIBLE);
            }
            else if(RoleId==2) {
                TextView textViewToChange = (TextView) findViewById(R.id.tvViewProfileText);
                textViewToChange.setText("Service Provider" + " " + "Profile");

                textViewToChange = (TextView) findViewById(R.id.tvNameValue);
                textViewToChange.setText(jo.getString("firstname") +" "+ jo.getString("lastname"));

                textViewToChange = (TextView) findViewById(R.id.tvGenderValue);
                textViewToChange.setText(jo.getString("gender"));

                textViewToChange = (TextView) findViewById(R.id.tvHouseNoValue);
                textViewToChange.setText(jo.getString("house_no"));

                textViewToChange = (TextView) findViewById(R.id.tvStreetValue);
                textViewToChange.setText(jo.getString("street"));

                textViewToChange = (TextView) findViewById(R.id.tvPostalCodeValue);
                textViewToChange.setText(jo.getString("postal_code"));

                textViewToChange = (TextView) findViewById(R.id.tvCityValue);
                textViewToChange.setText(jo.getString("city"));

                textViewToChange = (TextView) findViewById(R.id.tvContactValue);
                textViewToChange.setText(jo.getString("contact"));

                textViewToChange = (TextView) findViewById(R.id.tvEmailValue);
                textViewToChange.setText(jo.getString("email"));

                tvRating = (TextView) findViewById(R.id.tvRating);
                tvRating.setText(jo.getString("rating"));

                try {
                    URL url = new URL("link?UserId="+netId+"&RoleId="+RoleId);
                    InputStream in = url.openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    ImageView img = (ImageView) findViewById(R.id.imgProfile);
                    img.setImageBitmap(bitmap);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            else if(RoleId==3){
                TextView textViewToChange = (TextView) findViewById(R.id.tvViewProfileText);
                textViewToChange.setText("Admin" + " " + "Profile");

                textViewToChange = (TextView) findViewById(R.id.tvNameValue);
                textViewToChange.setText(jo.getString("firstname") +" "+ jo.getString("lastname"));

                textViewToChange = (TextView) findViewById(R.id.tvGenderValue);
                textViewToChange.setText(jo.getString("gender"));

                textViewToChange = (TextView) findViewById(R.id.tvContactValue);
                textViewToChange.setText(jo.getString("contact"));

                textViewToChange = (TextView) findViewById(R.id.tvEmailValue);
                textViewToChange.setText(jo.getString("email"));

                try {
                    URL url = new URL("link?UserId="+netId+"&RoleId="+RoleId);
                    InputStream in = url.openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    ImageView img = (ImageView) findViewById(R.id.imgProfile);
                    img.setImageBitmap(bitmap);
                }
                catch(Exception e){
                    e.printStackTrace();
                }

                //Set Visibility for Admin
                textViewToChange = (TextView) findViewById(R.id.tvRatingText);
                textViewToChange.setVisibility(View.INVISIBLE);
                RatingBar rb = (RatingBar) findViewById(R.id.ratingBar);
                rb.setVisibility(View.INVISIBLE);
                textViewToChange = (TextView) findViewById(R.id.tvHouseNoValue);
                textViewToChange.setVisibility(View.INVISIBLE);
                textViewToChange = (TextView) findViewById(R.id.tvStreetValue);
                textViewToChange.setVisibility(View.INVISIBLE);
                textViewToChange = (TextView) findViewById(R.id.tvPostalCodeValue);
                textViewToChange.setVisibility(View.INVISIBLE);
                textViewToChange = (TextView) findViewById(R.id.tvCityValue);
                textViewToChange.setVisibility(View.INVISIBLE);
                textViewToChange = (TextView) findViewById(R.id.tvHouseNoText);
                textViewToChange.setVisibility(View.INVISIBLE);
                textViewToChange = (TextView) findViewById(R.id.tvStreetText);
                textViewToChange.setVisibility(View.INVISIBLE);
                textViewToChange = (TextView) findViewById(R.id.tvPostalCodeText);
                textViewToChange.setVisibility(View.INVISIBLE);
                textViewToChange = (TextView) findViewById(R.id.tvCityText);
                textViewToChange.setVisibility(View.INVISIBLE);
            }
            else{
                try {
                    URL url = new URL("link?UserId="+spID+"&RoleId="+0);
                    InputStream in = url.openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    ImageView img = (ImageView) findViewById(R.id.imgProfile);
                    img.setImageBitmap(bitmap);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
