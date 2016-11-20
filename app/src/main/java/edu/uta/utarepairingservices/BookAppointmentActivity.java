package edu.uta.utarepairingservices;

// This activity is for the customer to book an appointment

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import static android.R.attr.data;

public class BookAppointmentActivity extends AppCompatActivity {

    EditText titleET, descriptionET;
    String titleST, descriptionST;
    Button Book_Appointment, Browse_Gallery, Take_Picture;
    private static int RESULT_LOAD_IMAGE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_book_appointment);

            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo!=null && networkInfo.isConnected()) {
                Toast.makeText(this, "Connected!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "No connection!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        titleET = (EditText)findViewById(R.id.title);
        descriptionET = (EditText)findViewById(R.id.describe_problem);
        Book_Appointment = (Button)findViewById(R.id.book_appointment);
        Browse_Gallery = (Button)findViewById(R.id.browse_gallary);
        Take_Picture = (Button)findViewById(R.id.take_picture);

        Book_Appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleST = titleET.getText().toString();
                descriptionST = descriptionET.getText().toString();
                if(TextUtils.isEmpty(titleST)) {
                    Toast.makeText(getBaseContext(), "Please enter the tile!", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(descriptionST)) {
                    Toast.makeText(getBaseContext(), "Please enter the description!", Toast.LENGTH_SHORT).show();
                }
                else {
                    BackgroundTask backgroundTask = new BackgroundTask();
                    backgroundTask.execute(titleST, descriptionST);
                    finish();
                }
            }
        });

        Take_Picture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        Browse_Gallery.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }

        });

        /*
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                ImageView imageView = (ImageView) findViewById(R.id.imgView);
                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            }


        }
        */
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }

    class BackgroundTask extends AsyncTask<String, Void, String> {

        String add_info_url;

        @Override
        protected void onPreExecute() {
            add_info_url = "http://kedarnadkarny.com/utarepair/book_appointment.php";
//            add_info_url = "localhost/utarepair/book_appointment.php";
        }

        @Override
        protected String doInBackground(String... args) {
            String titleBT, descriptionBT;
            int customerID = 1;
            int professionalID = 1;
            int statusID = 1;
            String datetime = "2016-11-19 13:32:41";

            titleBT = args[0];
            descriptionBT = args[1];
            try {
                URL url = new URL(add_info_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream,"UTF-8")));
                String data_string = URLEncoder.encode("title","UTF-8")+"="+URLEncoder.encode(titleBT,"UTF-8")+"&"+
                        URLEncoder.encode("description","UTF-8")+"="+URLEncoder.encode(descriptionBT,"UTF-8")+"&"+
                        URLEncoder.encode("customerID","UTF-8")+"="+URLEncoder.encode(String.valueOf(customerID),"UTF-8")+"&"+
                        URLEncoder.encode("statusID","UTF-8")+"="+URLEncoder.encode(String.valueOf(statusID),"UTF-8")+"&"+
                        URLEncoder.encode("datetime","UTF-8")+"="+URLEncoder.encode(String.valueOf(datetime),"UTF-8")+"&"+
                        URLEncoder.encode("professionalID","UTF-8")+"="+URLEncoder.encode(String.valueOf(professionalID),"UTF-8");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();
                httpURLConnection.disconnect();
                return "Appointment Booked";
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
            Toast.makeText(BookAppointmentActivity.this, s, Toast.LENGTH_SHORT).show();
        }
    }
}
