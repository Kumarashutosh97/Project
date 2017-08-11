package com.ashutosh.internship.project;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements ApiMethod.CallBackResponse {
    private static final String TAG = "Msg";
    String password;
    ProgressDialog progressDialog;
    CharSequence email;
    String FileName = "userData", Status = "Status";
    String Data = "Data";
    CheckBox checkBox;
    Button btnSignIn;
    TextView signUp;
    EditText user, pass;
    String logName, logEmail, logMobile, logPassword, logCreateOn, logId, logStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(Status, Context.MODE_PRIVATE);


        checkBox = (CheckBox) findViewById(R.id.checkBox);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        signUp = (TextView) findViewById(R.id.textView);
        user = (EditText) findViewById(R.id.editTextUser);
        pass = (EditText) findViewById(R.id.editTextPass);

        SharedPreferences rememberPassword = getSharedPreferences(Data, Context.MODE_PRIVATE);
        user.setText(rememberPassword.getString("email", ""));
        pass.setText(rememberPassword.getString("Password", ""));

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SignUpIntent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(SignUpIntent);
                finish();
            }
        });


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                email = user.getText().toString();
                password = pass.getText().toString();
                if (checkBox.isChecked()) {
                    SharedPreferences rememberPassword = getSharedPreferences(Data, Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = rememberPassword.edit();
                    edit.putString("email", email.toString());
                    edit.putString("Password", password);
                    edit.commit();
                }


                if (checkValidations(MainActivity.this, email, password)) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("email", email.toString());
                    params.put("password", password);

                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setCancelable(true);
                    progressDialog.setMessage("Loading....");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();

                    ApiMethod apiMethod = new ApiMethod(new ApiMethod.CallBackResponse() {
                        @Override
                        public void onCallBack(String response) {
                            parsing(response);
                        }
                    });
                    String api = "http://52.27.53.102/testapi/user/login";
                    apiMethod.postRequest(params, api);


                }
            }
        });
    }

    private void parsing(String result) {
        if (result != null) {
            String response = result;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String error_string = jsonObject.getString("error_string");
                int error_code = jsonObject.getInt("error_code");
                if (error_string.equals("success.")) {
                    String result1 = jsonObject.getString("result");
                    JSONObject jsonObj = jsonObject.getJSONObject("data");
                    logName = jsonObj.getString("name");
                    logEmail = jsonObj.getString("email");
                    logMobile = jsonObj.getString("mobile");
                    logPassword = jsonObj.getString("password");
                    logId = jsonObj.getString("id");
                    logCreateOn = jsonObj.getString("createdOn");
                    logStatus = jsonObj.getString("status");

                    progressDialog.dismiss();

                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    SharedPreferences sharedPref = getSharedPreferences(FileName, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("name", logName);
                    editor.putString("email", logEmail);
                    editor.putString("mobile", logMobile);
                    editor.putString("id", logId);
                    editor.putString("createOn", logCreateOn);
                    editor.putString("status", logStatus);
                    editor.commit();
                    SharedPreferences sharedPreferences = getSharedPreferences(Status, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPreferences.edit();
                    editor1.putBoolean("Log", true);
                    editor1.commit();
                    startActivity(intent);
                    finish();


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Please enter correct email or password", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.e(TAG, "Couldn't get json from server.");
        }
    }


    private boolean checkValidations(Context con, CharSequence username, String password) {

        String errorMsg = "";

        if (!(android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches())) {
            errorMsg = "Please enter correct email Id";

            Toast.makeText(con, errorMsg, Toast.LENGTH_LONG).show();
            return false;

        } else if (password == null || password.length() < 8) {
            errorMsg = "Please enter correct password.";
            Toast.makeText(con, errorMsg, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    @Override
    public void onCallBack(String response) {
        parsing(response);
    }
}
