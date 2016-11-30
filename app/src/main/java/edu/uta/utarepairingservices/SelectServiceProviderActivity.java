package edu.uta.utarepairingservices;

// This activity is for the customer to select Service Provider

import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class SelectServiceProviderActivity extends AppCompatActivity {

    UserInfo ui;
    TextView tvHeader;
    ListView lv;
    ArrayAdapter<String> adapter;
    String address="";
    InputStream is=null;
    String line=null;
    String result=null;
    HashMap hm;
    String[] data;
    String[] rating;
    String[] spID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service_provider);
        tvHeader = (TextView) findViewById(R.id.tvHeader);
        ui = new UserInfo();
        tvHeader.setText("SELECT SERVICE PROVIDER");

        lv = (ListView) findViewById(R.id.listView3);
        lv.setOnItemClickListener(onListClick);
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        getData();

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        lv.setAdapter(adapter);

    }

    private AdapterView.OnItemClickListener onListClick=new AdapterView.OnItemClickListener(){
        public void onItemClick(AdapterView<?>parent, View view, int position, long id){
            //Toast.makeText(getBaseContext(), spID[position], Toast.LENGTH_LONG).show();
            UserInfo ui = new UserInfo();
            ui.setSpID(Integer.parseInt(spID[position]));
            Intent intent = new Intent(getBaseContext(), ViewProfileActivity.class);
            intent.putExtra("view", "view_sp2");
            startActivity(intent);
        }
    };

    private  void getData(){

        try {
            address = "http://kedarnadkarny.com/utarepair/list_service_providers.php";
            URL url = new URL(address);
            HttpURLConnection con=(HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            OutputStream outputStream = con.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream,"UTF-8")));
            String data_string = URLEncoder.encode("service_id","UTF-8")+"="+URLEncoder.encode(String.valueOf(ui.getServiceId()),"UTF-8");
            bufferedWriter.write(data_string);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            is=new BufferedInputStream(con.getInputStream());
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
            hm=new HashMap();
            data=new String[ja.length()];
            rating = new String[ja.length()];
            spID = new String[ja.length()];
            for(int i=0;i<ja.length();i++){
                jo=ja.getJSONObject(i);
                hm.put("firstname",jo.getString("firstname"));
                hm.put("lastname",jo.getString("lastname"));
                hm.put("rating", jo.getString("rating"));
                data[i]=jo.getString("firstname")+" "+jo.getString("lastname") + "\t\t\t\t\t\tRating: " + jo.getString("rating");
                rating[i] = jo.getString("rating");
                spID[i] = jo.getString("sp_id");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
