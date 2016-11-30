package edu.uta.utarepairingservices;

// This activity for selecting Service Category
// Refer Figure 8 in exp uc

import android.content.Intent;
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
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class ServiceCategoryActivity extends AppCompatActivity {

    ListView lv;
    ArrayAdapter<String> adapter;
    String address="http://kedarnadkarny.com/utarepair/list_service_category.php";
    InputStream is=null;
    String line=null;
    String result=null;
    HashMap hm;
    public static String accept;
    String[] data;
    String[] serviceIDList;
    String[] serviceNameList;
    UserInfo ui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_category);
        lv = (ListView) findViewById(R.id.listView2);
        lv.setOnItemClickListener(onListClick);
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        getData();

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        lv.setAdapter(adapter);
    }

    private AdapterView.OnItemClickListener onListClick=new AdapterView.OnItemClickListener(){
        public void onItemClick(AdapterView<?>parent, View view, int position, long id){
            Toast.makeText(getBaseContext(), serviceIDList[position] + " " + serviceNameList[position], Toast.LENGTH_LONG).show();
            UserInfo ui = new UserInfo();
            ui.setServiceId(Integer.parseInt(serviceIDList[position]));
            startActivity(new Intent(ServiceCategoryActivity.this, SelectServiceProviderActivity.class));
        }
    };

    private  void getData(){

        try {
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
            hm=new HashMap();
            data=new String[ja.length()];
            serviceIDList = new String[ja.length()];
            serviceNameList = new String[ja.length()];
            for(int i=0;i<ja.length();i++){
                jo=ja.getJSONObject(i);
                hm.put("service_id",jo.getString("service_id"));
                hm.put("service_name",jo.getString("service_name"));
                data[i]=jo.getString("service_id")+" | "+jo.getString("service_name");
                serviceIDList[i]=jo.getString("service_id");
                serviceNameList[i]=jo.getString("service_name");
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
