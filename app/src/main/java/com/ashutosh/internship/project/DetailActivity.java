package com.ashutosh.internship.project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback{
GoogleMap googleMap;
String userId,postId,postedOn,postTitle,postStatus,description,latLng;
    double latitude,longitude;
    TextView title,des,date,status,post;
    Data data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Data dataObj = getIntent().getExtras().getParcelable("Obj");
        userId=dataObj.getUserId();
        postId=dataObj.getPostId();
        postedOn=dataObj.getPostedOn();
        postTitle=dataObj.getPostTitle();
        postStatus=dataObj.getPostStatus();
        description=dataObj.getDescription();
        latLng=dataObj.getLatLng();

        if(latLng!=null && latLng.length()>0 && !latLng.equalsIgnoreCase("")) {
            String[] latLng1 = latLng.split(",");
            latitude = Double.parseDouble((latLng1[0]!=null && !latLng1[0].equalsIgnoreCase("null")) ? latLng1[0]:"");
            longitude = Double.parseDouble((latLng1[1]!=null && !latLng1[0].equalsIgnoreCase("null")) ? latLng1[1]:"");
        }

        title=(TextView)findViewById(R.id.title) ;
        des=(TextView)findViewById(R.id.dis) ;
        date=(TextView)findViewById(R.id.date) ;
        status=(TextView)findViewById(R.id.status) ;
        post=(TextView)findViewById(R.id.postId) ;

        title.setText("Post Title : "+postTitle);
        des.setText("Description : "+description);
        date.setText("Posted On : "+postedOn);
        status.setText("Status : "+postStatus);
        post.setText("PostId : "+postId);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(latLng!=null&&!latLng.equalsIgnoreCase("")) {
            LatLng postLocation = new LatLng(latitude, longitude);
            googleMap.addMarker(new MarkerOptions().position(postLocation).title("Post Location"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(postLocation));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(13);
            //googleMap.setMyLocationEnabled(true);
            googleMap.animateCamera(zoom);
        }
            else{
            Toast.makeText(DetailActivity.this,"Location not Found",Toast.LENGTH_SHORT).show();
            }

    }
}
