package com.ashutosh.internship.project;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;


public class SignUpActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, ApiMethod.CallBackResponse {
    private static final String TAG = "Msg:";
    String FileName = "userData", Status = "Status";
    EditText name, mail, phone, pass, rePass;
    Button btnSignUp;
    TextView txtBtn;
    String responseCode;
    String sName, sPass, sRePass, sPhone;
    CharSequence sMail;
    ProgressDialog progressDialog;
    String jName, jEmail, jMobile, jPassword, jId, jCreateOn;
    de.hdodenhof.circleimageview.CircleImageView imageViewPic;
    private static final int READ_EXTERNAL_STORAGE_PERMISSION_CONSTANT = 122, IMG_RESULT = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name = (EditText) findViewById(R.id.txtName);
        imageViewPic = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.imageViewPic);
        mail = (EditText) findViewById(R.id.txtMail);
        txtBtn = (TextView) findViewById(R.id.txtBtn);
        phone = (EditText) findViewById(R.id.txtphone);
        pass = (EditText) findViewById(R.id.txtPass);
        rePass = (EditText) findViewById(R.id.txtRePass);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        imageViewPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                sName = name.getText().toString();
                sMail = mail.getText().toString();
                sPhone = phone.getText().toString();
                sPass = pass.getText().toString();
                sRePass = rePass.getText().toString();

                if (checkValidations(SignUpActivity.this, sName, sMail, sPhone, sPass, sRePass)) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("name", sName);
                    params.put("mobileNumber", sPhone);
                    params.put("email", sMail.toString());
                    params.put("password", sPass);

                    progressDialog = new ProgressDialog(SignUpActivity.this);
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
                    String api = "http://52.27.53.102/testapi/user/create";
                    apiMethod.postRequest(params, api);
                    parsing(responseCode);

                }
            }
        });
        txtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
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
                    jName = jsonObj.getString("name");
                    jEmail = jsonObj.getString("email");
                    jMobile = jsonObj.getString("mobile");
                    jPassword = jsonObj.getString("password");
                    jId = jsonObj.getString("id");
                    jCreateOn = jsonObj.getString("createdOn");

                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Successfully register", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);

                    SharedPreferences sharedPref = getSharedPreferences(FileName, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("name", jName);
                    editor.putString("email", jEmail);
                    editor.putString("mobile", jMobile);
                    editor.putString("id", jId);
                    editor.putString("createOn", jCreateOn);
                    editor.commit();
                    SharedPreferences sharedPreferences = getSharedPreferences(Status, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPreferences.edit();
                    editor1.putBoolean("Log", true);
                    editor1.commit();
                    startActivity(intent);
                    finish();


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Sign up failed", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.e(TAG, "Couldn't get json from server.");
        }
    }


    private boolean checkValidations(Context con, String name, CharSequence email, String phone, String pass, String rePass) {

        String errorMsg = "";

        if (name == null || name.length() == 0) {
            errorMsg = "Please enter you Name.";
            Toast.makeText(con, errorMsg, Toast.LENGTH_LONG).show();
            return false;

        } else if (!(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            errorMsg = "Please enter your  correct Email Id.";
            Toast.makeText(con, errorMsg, Toast.LENGTH_LONG).show();
            return false;
        } else if (pass == null || pass.length() < 8) {
            errorMsg = "Please enter 8 digit password.";
            Toast.makeText(con, errorMsg, Toast.LENGTH_LONG).show();
            return false;
        } else if (rePass == null || rePass.length() < 8) {
            errorMsg = "Please enter 8 digit retype password.";
            Toast.makeText(con, errorMsg, Toast.LENGTH_LONG).show();
            return false;
        } else if (!pass.equalsIgnoreCase(rePass)) {
            errorMsg = "Please enter same Password in both .";
            Toast.makeText(con, errorMsg, Toast.LENGTH_LONG).show();
            return false;

        } else if (phone == null) {
            errorMsg = "Please enter your Phone no.";
            Toast.makeText(con, errorMsg, Toast.LENGTH_LONG).show();
            return false;
        } else if (phone.length() != 10) {
            errorMsg = "Please enter your valid Phone no.";
            Toast.makeText(con, errorMsg, Toast.LENGTH_LONG).show();
            return false;
        }


        return true;
    }

    private void checkPermission() {

        if (ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(SignUpActivity.this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setTitle("Need Read external Storage Permission.");
                builder.setMessage("This app needs Read external Storage permission to pick Image from gallery.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_CONSTANT);
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

                ActivityCompat.requestPermissions(SignUpActivity.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_EXTERNAL_STORAGE_PERMISSION_CONSTANT);

            }
        } else {
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(i, IMG_RESULT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == IMG_RESULT && resultCode == RESULT_OK && data != null) {
                Uri pickedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
                cursor.moveToFirst();
                String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
                imageViewPic.setImageBitmap(bitmap);
                cursor.close();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Please try again", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(SignUpActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setTitle("Need Read external Storage Permission");
                    builder.setMessage("This app needs Read external Storage permission to pick Image from gallery.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();


                            ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_CONSTANT);


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
