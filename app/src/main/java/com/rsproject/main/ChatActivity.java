package com.rsproject.main;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.rsproject.R;
import com.rsproject.adapter.RoomChatAdapter;
import com.rsproject.encapsule.ListChatItem;
import com.rsproject.utils.ConnectionManager;
import com.rsproject.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Eno on 9/7/2016.
 */
public class ChatActivity extends AppCompatActivity {
    private ImageView sendChat;
    private EditText inputText;
    private RecyclerView recyclerView;
    private RoomChatAdapter adapter;
    private List<ListChatItem> list = new ArrayList<>();
    private List<ListChatItem> listCurrent = new ArrayList<>();
    private Toolbar toolbar;
    private String position, urlImage, name, date, description, response, message, judul, id_laporan;
    private Set<String> set = new HashSet<String>();
    private List<String> listClose = new ArrayList<String>();
    private List<String> listCloseCurrent = new ArrayList<String>();
    private ProgressDialog _progressDialog;
    private boolean isTaskRunning = false;
    private JSONObject jsonObj, jsonData;
    private JSONArray jsonArray;
    private Utils utils;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);
        sendChat = (ImageView) findViewById(R.id.send_chat);
        inputText = (EditText) findViewById(R.id.input_chat);
        recyclerView = (RecyclerView) findViewById(R.id.recycleChat);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        sharedPreferences = getSharedPreferences("isLogin", Context.MODE_PRIVATE);
        name = sharedPreferences.getString("nama", null);

        //name = getIntent().getStringExtra("name");
        date = getIntent().getStringExtra("date");
        description = getIntent().getStringExtra("description");
        urlImage = getIntent().getStringExtra("urlimage");
        description = getIntent().getStringExtra("description");
        position = getIntent().getStringExtra("pos");
        judul = getIntent().getStringExtra("title");
        listCloseCurrent = getIntent().getStringArrayListExtra("fab");
        id_laporan = getIntent().getStringExtra("id_laporan");

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        sendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputText.getText().toString().trim().length() > 0){
                    new requestPostList().execute();
                }
                //showList();
            }
        });

        utils = new Utils(ChatActivity.this);

        new requestGetResponse().execute();

    }

    private void showList() {
        LinearLayoutManager layoutParams = new LinearLayoutManager(ChatActivity.this);
        layoutParams.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutParams);
        adapter = new RoomChatAdapter(list);
        recyclerView.setAdapter(adapter);
        inputText.setText("");
    }


    class requestPostList extends AsyncTask<String, String, String> {
        String textChat = inputText.getText().toString();
        String date = utils.getCurrentDateandTime();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listCurrent.add(new ListChatItem(name, date, textChat, "1"));
            //showList();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = ConnectionManager.URL_SAVE_RESPONSE;
                response = ConnectionManager.requestSaveResponse(url, name, textChat,
                        date, id_laporan, ChatActivity.this);
                jsonObj = new JSONObject(response);
                if (jsonObj != null) {
                    message = jsonObj.getString("message");
                }


            } catch (Exception e) {
                message = "failed";
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (jsonObj != null) {
                if (message.equalsIgnoreCase("Data berhasil disimpan")) {
                    message = "";
                    list.add(listCurrent.get(listCurrent.size() - 1));
                    showList();

                } else {
                    listCurrent = new ArrayList<>();
                    /*Iterator<ListChatItem> i = listCurrent.iterator();
                    while (i.hasNext()){
                        ListChatItem item = i.next();
                        i.remove();
                    }*/
                    message = "";
                    Toast.makeText(ChatActivity.this, "Gagal mengirim..!", Toast.LENGTH_SHORT).show();
                }
            } else {
                listCurrent = new ArrayList<>();
                message = "";
                Toast.makeText(ChatActivity.this, "Gagal mengirim..!", Toast.LENGTH_SHORT).show();
            }

        }
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
                _progressDialog = new ProgressDialog(ChatActivity.this);
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
                response = ConnectionManager.requestResponse(url, name, id_laporan, ChatActivity.this);
                if (response != null) {
                    jsonObj = new JSONObject(response);
                    jsonArray = jsonObj.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonData = jsonArray.getJSONObject(i);
                            String id_respon = jsonData.getString("id_response");
                            String id_laporan = jsonData.getString("id_laporan");
                            String response = jsonData.getString("response");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_close:
                showsAlert();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChatActivity.this);
        alertDialog.setTitle("Close Issue");
        alertDialog.setMessage("Apakah anda ingin menutup isu ini.?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listClose.add(position);
                if (listCloseCurrent != null) {
                    listClose.addAll(listCloseCurrent);
                }
                set.addAll(listClose);
                SharedPreferences shered = getSharedPreferences("active", Context.MODE_PRIVATE);
                SharedPreferences.Editor sheredEdit = shered.edit();
                sheredEdit.putStringSet("active", set);
                sheredEdit.commit();
                Intent i = new Intent(ChatActivity.this, DetailActivity.class);
                i.putExtra("pos2", position);
                i.putExtra("name", name);
                i.putExtra("date", date);
                i.putExtra("urlimage", urlImage);
                i.putExtra("description", description);
                i.putExtra("title", judul);
                startActivity(i);
                finish();

            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}

