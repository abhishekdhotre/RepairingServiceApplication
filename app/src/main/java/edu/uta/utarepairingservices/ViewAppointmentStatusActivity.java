package edu.uta.utarepairingservices;

// This Activity is for the Customer to see history of appointments and their status

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

public class ViewAppointmentStatusActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment_status);

        String[] foods={"Bacon","Tuna","Candy"};
        ListAdapter buckyAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,foods);
        ListView buckyListView =(ListView) findViewById(R.id.buckysListView);
        buckyListView.setAdapter(buckyAdapter);
        buckyListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){

                  public void onItemClick(AdapterView<?>parent, View view, int position, long id) {
                      String food = String.valueOf(parent.getItemAtPosition(position));
                      Toast.makeText(ViewAppointmentStatusActivity.this,food,Toast.LENGTH_LONG).show();
                  }

                  }

        );
        String sp_id="1";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(sp_id);
        finish();

    }

    class BackgroundTask extends AsyncTask<String, Void, String> {
        Context ctx;
        String add_info_url;

        public BackgroundTask(Context ctx) {
            this.ctx = ctx;
        }

        public BackgroundTask() {

        }

        @Override
        protected void onPreExecute() {
            add_info_url = "http://kedarnadkarny.com/utarepair/appointment_status_spv.php";
        }

        @Override
        protected String doInBackground(String... args) {
            String sp_id=args[0];
            try {
                URL url = new URL(add_info_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream,"UTF-8")));
                String data_string = URLEncoder.encode("sp_id","UTF-8")+"="+URLEncoder.encode(sp_id,"UTF-8");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String response="";
                String line="";
                while((line=bufferedReader.readLine())!=null){
                    response+=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;
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
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("response", s);
            Toast.makeText(ViewAppointmentStatusActivity.this, s, Toast.LENGTH_SHORT).show();
        }
    }
}
