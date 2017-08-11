package com.ashutosh.internship.project;

import android.*;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class CreatePostActivity extends AppCompatActivity implements ApiMethod.CallBackResponse{
    private static final String TAG = "Error msg";
    String FileName="userData";
    private FusedLocationProviderClient fusedLocationProviderClient;
    String latitude;
    String longitude;
    String responseCode;
    ProgressDialog progressDialog;
    Button btnSubmit;
    EditText editTitle, editDescription, editAddress;
    private String title, description, address,userId;
    private static final int ACCESS_COARSE_LOCATION_PERMISSION_CONSTANT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        editTitle = (EditText) findViewById(R.id.editTitle);
        editDescription = (EditText) findViewById(R.id.editDescription);
        editAddress = (EditText) findViewById(R.id.editAddress);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        SharedPreferences sharedPref=getSharedPreferences(FileName, Context.MODE_PRIVATE);
        String defaultValue="";
        sharedPref.getString("name",defaultValue);
        userId = sharedPref.getString("id",defaultValue);

        checkPermission();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   title=editTitle.getText().toString();
                   description=editDescription.getText().toString();
                   address=editAddress.getText().toString();
                if (checkValidations(CreatePostActivity.this,title,description,address,latitude,longitude)) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("userId",userId);
                    params.put("postTitle",title);
                    params.put("description",description);
                    params.put("latLng", latitude+","+longitude);
                    params.put("address",address);

                    progressDialog = new ProgressDialog(CreatePostActivity.this);
                    progressDialog.setCancelable(true);
                    progressDialog.setMessage("Loading....");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();

                    ApiMethod apiMethod=new ApiMethod(new ApiMethod.CallBackResponse() {
                        @Override
                        public void onCallBack(String response) {
                                parsing(response);
                        }
                    });
                    String api="http://52.27.53.102/testapi/post/create";
                    apiMethod.postRequest(params,api);
                    parsing(responseCode);
                }
            }
        });


    }

    private void parsing(String result){
        if (result != null) {
            String response = result;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String error_string=jsonObject.getString("error_string");
                if(error_string.equals("success.")){
                    progressDialog.dismiss();
                    Toast.makeText(CreatePostActivity.this,"Post Submitted Success",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(CreatePostActivity.this,"Post Submitting Failed ",Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.e(TAG, "Couldn't get json from server.");
        }
    }

    private void checkPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(CreatePostActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(CreatePostActivity.this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder builder = new AlertDialog.Builder(CreatePostActivity.this);
                builder.setTitle("Need Location Permission");
                builder.setMessage("This app needs location permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(CreatePostActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_COARSE_LOCATION_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(CreatePostActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        ACCESS_COARSE_LOCATION_PERMISSION_CONSTANT);

            }
        } else {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                              double   latitude1 = location.getLatitude();
                              double  longitude1 = location.getLongitude();
                                latitude = new Double(latitude1).toString();
                                longitude=new Double(longitude1).toString();

                            } else {
                                Toast.makeText(CreatePostActivity.this,"Plaese Enable GPS",Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
        }
    }

    private boolean checkValidations(Context con, String title, String postDescription,String address,String lat,String longi) {

        String errorMsg = "";

        if (title == null || title.length() == 0) {
            errorMsg = "Please enter Post title.";
            Toast.makeText(con, errorMsg, Toast.LENGTH_SHORT).show();
            return false;
        } else if (postDescription == null || postDescription.length() == 0) {
            errorMsg = "Please enter your Post Description.";
            Toast.makeText(con, errorMsg, Toast.LENGTH_SHORT).show();
            return false;}
            else if (address == null || address.length() == 0) {
                errorMsg = "Please enter your address.";
                Toast.makeText(con, errorMsg, Toast.LENGTH_SHORT).show();
                return false;
        } else if (lat == null||longi==null ) {
            errorMsg = "Unable to get Location";
            Toast.makeText(con, errorMsg, Toast.LENGTH_SHORT).show();
            Toast.makeText(con, "You can't submit your post", Toast.LENGTH_LONG).show();
            return false;
        }


        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == ACCESS_COARSE_LOCATION_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...


            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(CreatePostActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreatePostActivity.this);
                    builder.setTitle("Need Location Permission");
                    builder.setMessage("This app needs location permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();


                            ActivityCompat.requestPermissions(CreatePostActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_COARSE_LOCATION_PERMISSION_CONSTANT);


                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onCallBack(String response) {
        parsing(response);
    }
}
