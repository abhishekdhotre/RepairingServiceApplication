package edu.uta.utarepairingservices;

// This activity is for the customer to book an appointment

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static android.R.attr.data;

public class BookAppointmentActivity extends AppCompatActivity {

    EditText titleET, descriptionET;
    String titleST, descriptionST;
    Button Book_Appointment, Browse_Gallery, Take_Picture;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_REQUEST = 1888;
    InputStream is=null;
    String line=null;
    String result=null;
    UserInfo ui;

    private  Context context;

    private ImageView imageToUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_book_appointment);

            context = this;

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
        ui = new UserInfo();
        titleET = (EditText)findViewById(R.id.title);
        descriptionET = (EditText)findViewById(R.id.describe_problem);
        Book_Appointment = (Button)findViewById(R.id.book_appointment);
        Browse_Gallery = (Button)findViewById(R.id.browse_gallary);
        Take_Picture = (Button)findViewById(R.id.take_picture);
        imageToUpload = (ImageView) findViewById(R.id.imageToUpload);

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

                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }

        });


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
                    bookAppointment();
                }
            }
        });

    }

    public void bookAppointment() {
        try {
            Calendar c = Calendar.getInstance();

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String datetime = df.format(c.getTime());

            String address = "http://kedarnadkarny.com/utarepair/book_appointment.php";
            URL url = new URL(address);
            HttpURLConnection con=(HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            OutputStream outputStream = con.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream,"UTF-8")));
            String data_string = URLEncoder.encode("title","UTF-8")+"="+URLEncoder.encode(titleST,"UTF-8")+"&"+
                    URLEncoder.encode("description","UTF-8")+"="+URLEncoder.encode(descriptionST,"UTF-8")+"&"+
                    URLEncoder.encode("uta_net_id","UTF-8")+"="+URLEncoder.encode(ui.getUta_net_id(),"UTF-8")+"&"+
                    URLEncoder.encode("date_time","UTF-8")+"="+URLEncoder.encode(datetime,"UTF-8")+"&"+
                    URLEncoder.encode("professional_id","UTF-8")+"="+URLEncoder.encode(String.valueOf(ui.getSpID()),"UTF-8");
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
            if(result.equals("BOOKED")) {
                ui.setMessage("Appointment Booked!");
                Intent i = new Intent(getBaseContext(), CustomerHomeActivity.class);
                startActivity(i);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
