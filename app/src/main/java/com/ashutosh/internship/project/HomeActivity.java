package com.ashutosh.internship.project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String FileName = "userData", Status = "Status";
    String name, email, mobile_no, id, createOn;
    TextView txtName;


    de.hdodenhof.circleimageview.CircleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPref = getSharedPreferences(FileName, Context.MODE_PRIVATE);
        String defaultValue = "";
        name = sharedPref.getString("name", defaultValue);
        email = sharedPref.getString("email", defaultValue);
        mobile_no = sharedPref.getString("mobile", defaultValue);
        id = sharedPref.getString("id", defaultValue);
        createOn = sharedPref.getString("createOn", defaultValue);


        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createPost = new Intent(HomeActivity.this, CreatePostActivity.class);
                startActivity(createPost);
            }
        });


        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) findViewById(R.id.pager);
        PageAdapter adapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        txtName = (TextView) header.findViewById(R.id.txtName);
        imageView = (de.hdodenhof.circleimageview.CircleImageView) header.findViewById(R.id.imageViewPic);
        txtName.setText(name);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent intent = getIntent();
            startActivity(intent);
            finish();
        } else if (id == R.id.profile) {
            Intent profileIntent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
        } else if (id == R.id.createNewPost) {
            Intent createPostIntent = new Intent(HomeActivity.this, CreatePostActivity.class);
            startActivity(createPostIntent);
        } else if (id == R.id.logout) {
            Intent logoutIntent = new Intent(HomeActivity.this, MainActivity.class);
            SharedPreferences sharedPreferences = getSharedPreferences(Status, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sharedPreferences.edit();
            editor1.putBoolean("Log", false);
            editor1.commit();
            startActivity(logoutIntent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            AlertDialog alertBox = new AlertDialog.Builder(this)
                    .setMessage("Do you want to exit application?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {


                        public void onClick(DialogInterface arg0, int arg1) {

                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {

                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    })
                    .show();

            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


}
