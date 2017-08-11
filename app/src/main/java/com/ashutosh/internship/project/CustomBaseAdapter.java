package com.ashutosh.internship.project;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomBaseAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Data> item;


    public CustomBaseAdapter(Context context, ArrayList<Data> items) {
        this.context = context;
        this.item = items;
    }

    private class ViewHolder {
        TextView userTitle, postTitle, postDis, address, dateOfPost;


    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return item.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater Inflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = Inflater.inflate(R.layout.custome_list, null);
            holder = new ViewHolder();
            holder.userTitle = (TextView) convertView.findViewById(R.id.userTitle);
            holder.postTitle = (TextView) convertView.findViewById(R.id.postTitle);
            holder.postDis = (TextView) convertView.findViewById(R.id.postDis);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            holder.dateOfPost = (TextView) convertView.findViewById(R.id.dateOfPost);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Data data = (Data) getItem(position);
        holder.userTitle.setText(data.getUserId());
        holder.postTitle.setText(data.getPostTitle());
        holder.postDis.setText(data.getDescription());
        holder.address.setText(data.getAddress());
        holder.dateOfPost.setText(data.getPostedOn());


        return convertView;
    }

}
