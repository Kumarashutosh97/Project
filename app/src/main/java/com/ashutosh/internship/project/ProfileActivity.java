package com.ashutosh.internship.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
    String name,email,mobile_no,id,createOn,status;
    String FileName="userData";
    TextView textViewName,textViewEmail,textViewMobile,textViewId,textViewCreateOn,textViewStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        textViewName=(TextView)findViewById(R.id.textViewName);
        textViewEmail=(TextView)findViewById(R.id.textViewEmail);
        textViewMobile=(TextView)findViewById(R.id.textViewMobile);
        textViewId=(TextView)findViewById(R.id.textViewId);
        textViewCreateOn=(TextView)findViewById(R.id.textViewCreateOn);
        textViewStatus=(TextView)findViewById(R.id.textViewStatus);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SharedPreferences sharedPref=getSharedPreferences(FileName, Context.MODE_PRIVATE);
        String defaultValue="Not available";
        name = sharedPref.getString("name",defaultValue);
        email= sharedPref.getString("email",defaultValue);
        mobile_no = sharedPref.getString("mobile",defaultValue);
        id = sharedPref.getString("id",defaultValue);
        createOn = sharedPref.getString("createOn",defaultValue);
        status = sharedPref.getString("status",defaultValue);
        textViewName.setText("Name : "+name);
        textViewEmail.setText("Email : "+email);
        textViewMobile.setText("Mobile no. : "+mobile_no);
        textViewCreateOn.setText("CreateOn : "+createOn);
        textViewId.setText("UserId : "+id);
        textViewStatus.setText("Status : "+status);

    }
}
