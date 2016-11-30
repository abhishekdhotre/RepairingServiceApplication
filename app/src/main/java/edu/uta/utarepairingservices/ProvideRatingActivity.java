package edu.uta.utarepairingservices;

// This activity sif ro the Customer to provide rating for the Service Provider after the task has been compeleted

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class ProvideRatingActivity extends AppCompatActivity {

    int requestID;
    UserInfo ui;
    String fullname, result;
    InputStream is=null;
    String line=null;
    TextView txtHeader;
    Button btnSubmit;
    RatingBar ratingBar;
    float rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provide_rating);
        ui = new UserInfo();
        requestID = ui.getRequestID();
        txtHeader = (TextView) findViewById(R.id.txtHeader);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        fullname = getIntent().getStringExtra("fullname");

        txtHeader.setText(fullname);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRating();
            }
        });
    }

    public void submitRating() {
        rating =ratingBar.getRating();
        Toast.makeText(getBaseContext(), "Stars: "+rating, Toast.LENGTH_LONG).show();

        try {
            URL url = new URL("http://kedarnadkarny.com/utarepair/provide_rating.php");
            HttpURLConnection con=(HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            OutputStream outputStream = con.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream,"UTF-8")));
            String data_string = "";

            data_string = URLEncoder.encode("request_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(requestID), "UTF-8")+"&"+
                            URLEncoder.encode("rating","UTF-8")+"="+URLEncoder.encode(String.valueOf(rating),"UTF-8");


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
                Toast.makeText(getBaseContext(), "You have rated " + fullname + "'s work", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ProvideRatingActivity.this, CustomerHomeActivity.class));
            }
        }
        catch (Exception e){
            e.printStackTrace();

        }
    }
}
