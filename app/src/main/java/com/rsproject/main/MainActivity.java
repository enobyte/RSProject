package com.rsproject.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.rsproject.R;
import com.rsproject.adapter.ListMainAdapter;
import com.rsproject.encapsule.ListItems;
import com.rsproject.utils.ConnectionDetector;
import com.rsproject.utils.ConnectionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<ListItems> list;
    private ListMainAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private String user, level, response, url;
    private Toolbar toolbar;
    private SwipeRefreshLayout swp;
    private ProgressDialog _progressDialog;
    private boolean isTaskRunning = false;
    private JSONObject jsonObject, jsonObj;
    private JSONArray jsonData;
    private SharedPreferences session;
    //private ListItems items;
    private Boolean isInternetActive = false;
    private ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.list_recycle);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        swp = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        session = getSharedPreferences("isLogin", Context.MODE_PRIVATE);
        level = session.getString("level", null);
        user = session.getString("username", null);
        if (level != null) {
            if (level.equalsIgnoreCase("admin")) {
                floatingActionButton.setVisibility(View.GONE);
            } else {
                floatingActionButton.setVisibility(View.VISIBLE);
            }
        }

        list = new ArrayList<>();
        new getListAll().execute();
        //PopulateData();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cd = new ConnectionDetector(MainActivity.this);
                isInternetActive = cd.isConnectingToInternet();
                if (isInternetActive) {
                    startActivity(new Intent(MainActivity.this, AddIssueActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Periksa Koneksi Internet Anda...!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        swp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cd = new ConnectionDetector(MainActivity.this);
                isInternetActive = cd.isConnectingToInternet();
                if (isInternetActive) {
                    new getListAll().execute();
                } else {
                    Toast.makeText(MainActivity.this, "Periksa Koneksi Internet Anda...!", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    private void ShowData() {
        LinearLayoutManager layoutParams = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutParams);
        adapter = new ListMainAdapter(MainActivity.this, list);
        recyclerView.setAdapter(adapter);
    }

    /*private void PopulateData() {
        int x = 0;
        list = new ArrayList<>();
        do {
            list.add(new ListItems("Eno", "12-08-2016 09:12", "ini adalah budi", "https://lampukecildotcom.files.wordpress.com/2014/12/gambar-wallpaper-uzumaki-naruto.jpg?w=600&h=426", "ini judul"));
            x++;
        } while (x < 10);

        ShowData();

    }*/

    class getListAll extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (swp.isRefreshing()) {
                swp.setRefreshing(true);
            } else {
                if (!isTaskRunning) {
                    if (_progressDialog != null) {
                        _progressDialog.dismiss();
                    }
                    isTaskRunning = true;
                    _progressDialog = new ProgressDialog(MainActivity.this);
                    _progressDialog.setMessage(getResources().getString(R.string.loading_news));
                    _progressDialog.setIndeterminate(false);
                    _progressDialog.setCancelable(false);
                    _progressDialog.show();
                } else {
                    return;
                }
            }

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (level.equalsIgnoreCase("admin")) {
                    url = ConnectionManager.URL_GETALL_LAPORAN;
                    response = ConnectionManager.requestAllLaporan(url, MainActivity.this);
                } else {
                    url = ConnectionManager.URL_GET_LAPORANBYNAME;
                    response = ConnectionManager.requestLaporanByName(url, user, MainActivity.this);
                }

                if (response != null) {
                    jsonObject = new JSONObject(response);
                    jsonData = jsonObject.getJSONArray("laporan");
                    if (jsonData.length() > 0) {
                        for (int i = jsonData.length() - 1; i < jsonData.length(); i--) {
                            jsonObj = jsonData.getJSONObject(i);
                            String id_laporan = jsonObj.getString("id_laporan");
                            String id_kategori = jsonObj.getString("id_kategori_laporan");
                            String judul = jsonObj.getString("judul_laporan");
                            String keterangan = jsonObj.getString("keterangan");
                            String latitude = jsonObj.getString("latitude");
                            String longitude = jsonObj.getString("longitude");
                            String tgl_laporan = jsonObj.getString("tgl_laporan");
                            String username = jsonObj.getString("username");
                            String status_urgensi = jsonObj.getString("status_urgensi");
                            String status_laporan = jsonObj.getString("status_laporan");
                            String gambar = jsonObj.getString("gambar");
                            list.add(new ListItems(id_laporan, username, tgl_laporan, keterangan, gambar, judul));
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (swp.isRefreshing()) {
                swp.setRefreshing(false);
            } else {
                if (_progressDialog != null) {
                    _progressDialog.dismiss();
                }
                isTaskRunning = false;
            }
           /* if (jsonData.length() > 0) {
                ShowData();
            }*/
            ShowData();

        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            return false;
        }
    }
}
