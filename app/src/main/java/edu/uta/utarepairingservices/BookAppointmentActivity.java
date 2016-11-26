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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.params.HttpParams;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import okhttp3.Headers;
import okhttp3.Response;

import static android.R.attr.data;

public class BookAppointmentActivity extends AppCompatActivity {

    EditText titleET, descriptionET;
    String titleST, descriptionST;
    Button Book_Appointment, Browse_Gallery, Take_Picture;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_REQUEST = 1888;

    private  Context context;

    private ImageView imageToUpload;
    //private String imagePathTpUpload;

    /*
    private BaseHttpRequestCallback callback = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {
            Toast.makeText(context,response,Toast.LENGTH_SHORT).show();
        }};
    */

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
            /*
                File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS);
                String pictureDirectoryPath = pictureDirectory.getPath();
                Uri data = Uri.parse(pictureDirectoryPath);
                galleryIntent.setDataAndType(data, "image/*");
            */
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
                    BackgroundTask backgroundTask = new BackgroundTask();
                    backgroundTask.execute(titleST, descriptionST);
                    finish();
                }
                //Bitmap image = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();

                /*
                 new HttpUtil(HttpUtil.FILE_PARAMS).addBody(
                       new File(imagePathTpUpload)
                 ).post(
                        "save_picture.php",callback
                 );

                 */


                //nevigate to next activity
                startActivity(new Intent(BookAppointmentActivity.this, ViewAppointmentStatusActivity.class));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //try {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {

                Uri selectedImage = data.getData();
                imageToUpload.setImageURI(selectedImage);
                //imagePathTpUpload = selectedImage.getPath();

                /*
                InputStream inputStream;

                try {
                    inputStream = getContentResolver().openInputStream(selectedImage);

                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                    imageToUpload.setImageBitmap(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_SHORT).show();
                }



                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

         //     ImageView imageView = (ImageView) findViewById(R.id.imageToUpload);
                imageToUpload.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                String fileNameSegments[] = picturePath.split("/");
                // fileName = fileNameSegments[fileNameSegments.length - 1];
                // Put file name in Async Http Post Param which will used in Php web app
                //params.put("filename", fileName);
            */
            }
            if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageToUpload.setImageBitmap(photo);
            }
        //} catch (Exception e) {
         //   Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
    }

    /*
    private class UploadImage extends AsyncTask<Void, Void, Void>{

        Bitmap image;
        String name;

        public UploadImage(Bitmap image, String name){
            this.image = image;
            this.name = name;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("image", encodedImage));
            dataToSend.add(new BasicNameValuePair("name", name));

            return null;
        }
    }
    */

    class BackgroundTask extends AsyncTask<String, Void, String> {

        String add_info_url;
        //String save_pic_url;

        @Override
        protected void onPreExecute() {
            add_info_url = "http://kedarnadkarny.com/utarepair/book_appointment.php";
            //save_pic_url = "http://kedarnadkarny.com/utarepair/save_picture.php";
        }

        @Override
        protected String doInBackground(String... args) {
            String titleBT, descriptionBT;
            int customerID = 1;
            int professionalID = 5;
            int statusID = 1;

            Calendar c = Calendar.getInstance();

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String datetime = df.format(c.getTime());
            //String datetime = "2016-11-19 13:32:41";

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
