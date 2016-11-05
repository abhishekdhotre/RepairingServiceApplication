package edu.uta.utarepairingservices;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import edu.uta.utarepairingservices.DBAccess.AdminOperations;

public class AddUserActivity extends AppCompatActivity {

    Spinner roleSpinner;
    EditText firstNameET, lastNameET;
    String first_name, last_name;
    Button REG;
    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        roleSpinner = (Spinner)findViewById(R.id.roleSpinner);
        String[] items = new String[]{"Customer", "Service Provider"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, items);
        roleSpinner.setAdapter(adapter);
        firstNameET = (EditText)findViewById(R.id.firstNameET);
        lastNameET = (EditText)findViewById(R.id.lastNameET);
        REG = (Button)findViewById(R.id.registerUser);
        REG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first_name = firstNameET.getText().toString();
                last_name = lastNameET.getText().toString();
                if(TextUtils.isEmpty(first_name)) {
                    Toast.makeText(getBaseContext(), "Please enter First Name!", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(last_name)) {
                    Toast.makeText(getBaseContext(), "Please enter Last Name!", Toast.LENGTH_SHORT).show();
                }
                else {
                    AdminOperations db = new AdminOperations(ctx);
                    db.putInformation(db, first_name, last_name);
                    Toast.makeText(getBaseContext(), "Registration is successful!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    public void selectGender(View v) {
        boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId())
        {
            case R.id.radio_female:
                Toast.makeText(this, "FEMALE", Toast.LENGTH_SHORT).show();
                break;
            case R.id.radio_male:
                Toast.makeText(this, "MALE", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
