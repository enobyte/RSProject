package com.rsproject.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rsproject.R;
import com.rsproject.adapter.ImageViewDetailAdapter;
import com.rsproject.adapter.RoomChatAdapter;
import com.rsproject.encapsule.ListChatItem;
import com.rsproject.encapsule.ListImageDetailArrays;
import com.rsproject.utils.ConnectionDetector;
import com.rsproject.utils.ConnectionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Eno on 8/31/2016.
 */
public class DetailActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private Toolbar tb;
    private SharedPreferences session, sessionLogin;
    private ImageView img;
    private String username, date, description, position, position2, judul, name, id_laporan;
    private List<String> urlImage;
    private ProgressBar pr;
    private TextView date_detail, description_detail, detail_judul;
    private ArrayList<String> listFab;
    private ProgressDialog _progressDialog;
    private boolean isTaskRunning = false;
    private List<ListChatItem> list = new ArrayList<>();
    private RecyclerView recyclerView, recycleImage;
    private RoomChatAdapter adapter;
    private JSONObject jsonData;
    private Boolean isInternetActive = false;
    private ConnectionDetector cd;
    private CoordinatorLayout coordinatorLayout;
    private AppBarLayout appBarLayout;
    private List<ListImageDetailArrays> listImage = new ArrayList<>();
    private ImageViewDetailAdapter imageAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        username = getIntent().getStringExtra("username");
        date = getIntent().getStringExtra("date");
        description = getIntent().getStringExtra("description");
        urlImage = getIntent().getStringArrayListExtra("urlimage");
        position = getIntent().getStringExtra("position");
        position2 = getIntent().getStringExtra("pos2");
        judul = getIntent().getStringExtra("title");
        id_laporan = getIntent().getStringExtra("id_laporan");
        if (position == null && position2 != null) {
            position = position2;
        }
        initView();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailActivity.this, ChatActivity.class);
                i.putExtra("pos", position);
                i.putExtra("fab", listFab);
                i.putExtra("username", username);
                i.putExtra("date", date);

                if (urlImage.size() != 0) {
                    i.putExtra("urlimage", urlImage.get(0));
                }

                i.putExtra("description", description);
                i.putExtra("title", judul);
                i.putExtra("id_laporan", id_laporan);
                startActivity(i);
                finish();
            }
        });


    }

    private void initView() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        tb = (Toolbar) findViewById(R.id.toolbar);
        pr = (ProgressBar) findViewById(R.id.progress_image);
        //img = (ImageView) findViewById(R.id.img_detail_news);
        date_detail = (TextView) findViewById(R.id.date_detail);
        description_detail = (TextView) findViewById(R.id.description);
        detail_judul = (TextView) findViewById(R.id.detail_judul);
        recyclerView = (RecyclerView) findViewById(R.id.par_coment);
        session = getSharedPreferences("active", Context.MODE_PRIVATE);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cordinator);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        recycleImage = (RecyclerView) findViewById(R.id.imagerecycle);
        Set<String> fabActive = session.getStringSet("active", null);
        sessionLogin = getSharedPreferences("isLogin", Context.MODE_PRIVATE);
        name = sessionLogin.getString("nama", null);
        setSupportActionBar(tb);
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        if (fabActive != null) {
            listFab = new ArrayList<>(fabActive);
            Log.d("fab", String.valueOf(fabActive));
            for (int i = 0; i < listFab.size(); i++) {
                String isClose = listFab.get(i);
                if (isClose != null && isClose.equals(position)) {
                    fab.setVisibility(View.INVISIBLE);
                    break;
                } else {
                    fab.setVisibility(View.VISIBLE);
                }
            }
        }
        cd = new ConnectionDetector(DetailActivity.this);
        isInternetActive = cd.isConnectingToInternet();
        if (isInternetActive) {
            new requestGetResponse().execute();
        } else {
            Toast.makeText(DetailActivity.this, "Periksa Koneksi Internet Anda...!",
                    Toast.LENGTH_LONG).show();
        }
        ShowData();
    }

    private void ShowData() {
        urlImage = new ArrayList<>();

        if (date != null) {
            date_detail.setText(date);
        }

        if (description_detail != null) {
            description_detail.setText(description);
        }

        if (detail_judul != null) {
            detail_judul.setText(judul);
        }

        /*if (urlImage.size() > 0) {
            for (String url : urlImage) {
                listImage.add(new ListImageDetailArrays(url));
            }
            LinearLayoutManager layoutParams = new LinearLayoutManager(DetailActivity.this,
                    LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutParams);
            imageAdapter = new ImageViewDetailAdapter(listImage, DetailActivity.this);
            recyclerView.setAdapter(adapter);
        }*/

        listImage.add(new ListImageDetailArrays("http://www.obatasmaanak.com/wp-content/uploads/2016/07/paru-paru-basah-untuk-anak2.jpg"));
        listImage.add(new ListImageDetailArrays("http://1.bp.blogspot.com/-6b1VRd_bJMM/UT-mj7nx_eI/AAAAAAAAA8o/3NVySW20wnA/s200/images.jpg"));
        LinearLayoutManager layoutParams = new LinearLayoutManager(DetailActivity.this,
                LinearLayoutManager.HORIZONTAL, false);
        recycleImage.setLayoutManager(layoutParams);
        imageAdapter = new ImageViewDetailAdapter(listImage, DetailActivity.this);
        recycleImage.setAdapter(imageAdapter);

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class requestGetResponse extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!isTaskRunning) {
                if (_progressDialog != null) {
                    _progressDialog.dismiss();
                }
                isTaskRunning = true;
                _progressDialog = new ProgressDialog(DetailActivity.this);
                _progressDialog.setMessage(getResources().getString(R.string.loading_news));
                _progressDialog.setIndeterminate(false);
                _progressDialog.setCancelable(false);
                _progressDialog.show();
            } else {
                return;
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                jsonData = new JSONObject();
                String url = ConnectionManager.URL_GET_RESPONSE;
                String response = ConnectionManager.requestResponse(url, name, id_laporan, DetailActivity.this);
                if (response != null) {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray jsonArray = jsonObj.getJSONArray("data");
                    int m = 0;
                    if (jsonArray.length() > 0) {
                        if (jsonArray.length() > 5) {
                            m = jsonArray.length() - 5;
                        } else {
                            m = 0;
                        }

                        for (int i = m; i < jsonArray.length(); i++) {
                            jsonData = jsonArray.getJSONObject(i);
                            String id_respon = jsonData.getString("id_response");
                            String id_laporan = jsonData.getString("id_laporan");
                            response = jsonData.getString("response");
                            String username = jsonData.getString("username");
                            String tgl = jsonData.getString("tgl_response");
                            list.add(new ListChatItem(username, tgl, response, "1"));
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
            if (_progressDialog != null) {
                _progressDialog.dismiss();
            }
            isTaskRunning = false;
            showList();
        }
    }

    private void showList() {
        LinearLayoutManager layoutParams = new LinearLayoutManager(DetailActivity.this);
        recyclerView.setLayoutManager(layoutParams);
        adapter = new RoomChatAdapter(list);
        recyclerView.setAdapter(adapter);
    }

           /*main_panel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (slide.getPanelState() != SlidingUpPanelLayout.PanelState.EXPANDED) {
                    slide.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    return true;
                } else if (slide.getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    slide.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                    return true;
                }
                return false;
            }
        });*/
}
