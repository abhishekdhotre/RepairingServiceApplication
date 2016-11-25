package edu.uta.utarepairingservices;

// Use the same Activity for all three users. I know the GUI is different. You can programmatically hide and show elements

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.widget.ArrayAdapter;
import android.os.StrictMode;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStreamReader;


public class ViewProfileActivity extends Activity {
    String UserId;

    ListView lv;
    ArrayAdapter<String> adapter;
    int RoleId = 2;
    String address;
    InputStream is=null;
    String line=null;
    String result=null;
    String data;
    String netId;
    UserInfo user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_view_profile);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        user = new UserInfo();
        netId = user.getUta_net_id();
        getData();

        /*adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        lv.setAdapter(adapter);*/
    }

    private  void getData(){
        try {
            if(RoleId==1) {
                address ="http://kedarnadkarny.com/utarepair/view_customer_profile.php";
            }
                else if(RoleId==2) {
                address ="http://kedarnadkarny.com/utarepair/view_service_provider_profile.php";
            }
            else if(RoleId==3){
                address ="http://kedarnadkarny.com/utarepair/view_admin_profile.php";
            }

            address = address.concat("?UserId=").concat(netId);
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

                RatingBar rb = (RatingBar) findViewById(R.id.ratingBar);
                rb.setRating(Float.parseFloat(jo.getString("rating")));
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
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}