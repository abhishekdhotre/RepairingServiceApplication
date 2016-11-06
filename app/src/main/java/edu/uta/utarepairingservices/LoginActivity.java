package edu.uta.utarepairingservices;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import edu.uta.utarepairingservices.DBAccess.LoginOperations;
import edu.uta.utarepairingservices.TableInfo.UserDetails;

public class LoginActivity extends AppCompatActivity {

    private LoginOperations loginOps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void checkLogin(View view) {
        //retrieve login details from UI.
        String netID = ((EditText) findViewById(R.id.editTextNetID)).getText().toString();
        String pwd   = ((EditText) findViewById(R.id.editText2Pwd)).getText().toString();

        //check role of the user. Invalid login has not been handled assuming best case scenario.
        loginOps = new LoginOperations(LoginActivity.this);
        loginOps.open();
        String role = loginOps.getRole(netID);
        loginOps.close();

        //navigate to respective home page based on role.
        if(role.equals("Customer"))
            startActivity(new Intent(LoginActivity.this, CustomerHomeActivity.class));
        else if(role.equals("Service Provider"))
            startActivity(new Intent(LoginActivity.this, ServiceProviderHomeActivity.class));
        else
            startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
    }

    public void addUser(View view) {
        loginOps = new LoginOperations(LoginActivity.this);
        UserDetails user = new UserDetails();
        user.setNetID("abc2@mavs.uta.edu");
        user.setPwd("abcd");
        user.setRole("Admin");
        loginOps.open();
        //add user details to login table
        loginOps.addUser(user);
        loginOps.close();
    }
}
