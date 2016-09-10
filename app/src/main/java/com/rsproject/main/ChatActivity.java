package com.rsproject.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.rsproject.R;
import com.rsproject.adapter.RoomChatAdapter;
import com.rsproject.encapsule.ListChatItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eno on 9/7/2016.
 */
public class ChatActivity extends AppCompatActivity {
    private ImageView sendChat;
    private EditText inputText;
    private RecyclerView recyclerView;
    private RoomChatAdapter adapter;
    private List<ListChatItem> list = new ArrayList<>();
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);
        sendChat = (ImageView) findViewById(R.id.send_chat);
        inputText = (EditText) findViewById(R.id.input_chat);
        recyclerView = (RecyclerView) findViewById(R.id.recycleChat);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Budi Gunawan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        sendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showList();
            }
        });

    }

    private void showList() {
        list.add(new ListChatItem("Eno", "12-01-2016", inputText.getText().toString(), "1"));
        LinearLayoutManager layoutParams = new LinearLayoutManager(ChatActivity.this);
        recyclerView.setLayoutManager(layoutParams);
        adapter = new RoomChatAdapter(list);
        recyclerView.setAdapter(adapter);

        inputText.setText("");
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
                SharedPreferences shered = getSharedPreferences("active", Context.MODE_PRIVATE);
                SharedPreferences.Editor sheredEdit = shered.edit();
                sheredEdit.putString("active", "true");
                sheredEdit.commit();
                startActivity(new Intent(ChatActivity.this, DetailActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

