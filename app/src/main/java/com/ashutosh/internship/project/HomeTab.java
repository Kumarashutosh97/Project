package com.ashutosh.internship.project;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class HomeTab extends Fragment implements ApiMethod.CallBackResponse,SwipeRefreshLayout.OnRefreshListener {
    ListView listView;
    String api = "http://52.27.53.102/testapi/post/view";
    ProgressDialog progressDialog;
    CustomBaseAdapter customBaseAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshLayout.setRefreshing(false);
                ApiMethod apiMethod = new ApiMethod(new ApiMethod.CallBackResponse() {
                    @Override
                    public void onCallBack(String response) {
                        parsing(response);
                    }
                });
                apiMethod.getRequest(api);
            }
        });

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        ApiMethod apiMethod = new ApiMethod(new ApiMethod.CallBackResponse() {
            @Override
            public void onCallBack(String response) {
                parsing(response);
            }
        });
        apiMethod.getRequest(api);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Data data = (Data) customBaseAdapter.getItem(position);
                Intent detailsIntent = new Intent(getActivity(), DetailActivity.class);
                detailsIntent.putExtra("Obj", data);
                startActivity(detailsIntent);
            }
        });


        return view;
    }

    private void parsing(String result) {
        if (result != null) {
            String responseCode = result;
            DataModel dataModel = new Gson().fromJson(responseCode, DataModel.class);
            ArrayList<Data> postList = dataModel.getData();
            customBaseAdapter = new CustomBaseAdapter(getActivity(), postList);
            progressDialog.dismiss();
            listView.setAdapter(customBaseAdapter);
        } else {
            Log.e(TAG, "Couldn't get json from server.");
        }
    }

    @Override
    public void onCallBack(String response) {
        parsing(response);
    }

    @Override
    public void onRefresh() {
        ApiMethod apiMethod = new ApiMethod(new ApiMethod.CallBackResponse() {
            @Override
            public void onCallBack(String response) {
                parsing(response);
            }
        });
        apiMethod.getRequest(api);
    }
}
