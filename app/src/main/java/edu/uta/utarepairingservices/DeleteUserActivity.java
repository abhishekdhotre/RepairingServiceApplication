package edu.uta.utarepairingservices;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.util.HashMap;

public class DeleteUserActivity extends AppCompatActivity {

    Button btnCustomers, btnSPs;
    TextView txtMessage;
    ListView lv;
    ArrayAdapter<String> adapter;
    String address="";
    InputStream is=null;
    String line=null;
    String result=null;
    String[] data;
    String[] netIdList;
    UserInfo ui;
    AlertDialog.Builder builder;
    String uta_net_id, userType, response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);
        ui = new UserInfo();
        lv = (ListView) findViewById(R.id.listView);
        lv.setOnItemClickListener(onListClick);
        btnCustomers = (Button) findViewById(R.id.btnCustomers);
        btnSPs = (Button) findViewById(R.id.btnSPs);
        txtMessage = (TextView) findViewById(R.id.txtMessage);
        txtMessage.setText("Tap on type of user");

        builder = new AlertDialog.Builder(this);

        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        btnCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewCustomers();
            }
        });

        btnSPs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSPs();
            }
        });
    }

    public void viewCustomers() {
        userType = "1";
        address = "http://kedarnadkarny.com/utarepair/listAll_customers.php";
        getData();
        adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1,data);
        lv.setAdapter(adapter);
    }

    public void viewSPs() {
        userType = "2";
        address = "http://kedarnadkarny.com/utarepair/listAll_service_providers.php";
        getData();
        adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1,data);
        lv.setAdapter(adapter);
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    deleteUser();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    private AdapterView.OnItemClickListener onListClick=new AdapterView.OnItemClickListener(){
        public void onItemClick(AdapterView<?>parent, View view, int position, long id){
            //Toast.makeText(getBaseContext(), netIdList[position], Toast.LENGTH_LONG).show();
            uta_net_id = netIdList[position];
            builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
    };

    public void getData() {
        try {

            URL url = new URL(address);
            HttpURLConnection con=(HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);

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
            data=new String[ja.length()];
            netIdList=new String[ja.length()];
            for(int i=0;i<ja.length();i++){
                jo=ja.getJSONObject(i);;
                data[i]=jo.getString("firstname")+" "+jo.getString("lastname");
                netIdList[i]=jo.getString("uta_net_id");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteUser() {
        try {
            String address = "http://kedarnadkarny.com/utarepair/delete_user.php";
            URL url = new URL(address);
            HttpURLConnection con=(HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            OutputStream outputStream = con.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(outputStream,"UTF-8")));
            String data_string = URLEncoder.encode("uta_net_id","UTF-8")+"="+URLEncoder.encode(uta_net_id,"UTF-8")+"&"+
                                 URLEncoder.encode("usertype","UTF-8")+"="+URLEncoder.encode(userType,"UTF-8");
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
                Toast.makeText(getBaseContext(), "User Deleted Successfully!", Toast.LENGTH_LONG).show();
                if(userType.equals("1")) {
                    viewCustomers();
                }
                else {
                    viewSPs();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
