package edu.uta.utarepairingservices;

// This activity sif ro the Customer to provide rating for the Service Provider after the task has been compeleted
//provide rating
import android.media.Rating;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
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

public class ProvideRatingActivity extends AppCompatActivity {

    Button btnRating;
    RatingBar ratingBar;

    @Override
    //create
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provide_rating);
        btnRating = (Button) findViewById(R.id.submitRating);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float rating = ratingBar.getRating();
                String strrating = String.valueOf(rating);
                String request_id="1";
                Toast.makeText(getBaseContext(), ""+rating, Toast.LENGTH_LONG).show();

                    BackgroundTask backgroundTask = new BackgroundTask();
                    backgroundTask.execute(request_id, strrating);
                    finish();


            }
        });
    }

    class BackgroundTask extends AsyncTask<String, Void, String> {

        String add_info_url;

        @Override
        protected void onPreExecute() {
            add_info_url = "http://kedarnadkarny.com/utarepair/provide_rating.php";
        }

        @Override
        protected String doInBackground(String... args) {
            String request, rate;
            request = args[0];
            rate = args[1];
            try {
                URL url = new URL(add_info_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream,"UTF-8")));
                String data_string = URLEncoder.encode("request_id","UTF-8")+"="+URLEncoder.encode(request,"UTF-8")+"&"+
                        URLEncoder.encode("rating","UTF-8")+"="+URLEncoder.encode(rate,"UTF-8");
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
            Toast.makeText(ProvideRatingActivity.this, s, Toast.LENGTH_SHORT).show();
        }
    }

}
