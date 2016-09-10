package com.rsproject.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.rsproject.R;
import com.rsproject.adapter.ListMainAdapter;
import com.rsproject.encapsule.ListItems;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<ListItems> list;
    private ListMainAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private String user;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.list_recycle);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        user = getIntent().getStringExtra("user");
        if (user != null){
            if (user.equalsIgnoreCase("admin")) {
                floatingActionButton.setVisibility(View.GONE);
            } else {
                floatingActionButton.setVisibility(View.VISIBLE);
            }
        }


        PopulateData();

        ShowData();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddIssueActivity.class));
            }
        });
    }

    private void ShowData() {
        LinearLayoutManager layoutParams = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutParams);
        adapter = new ListMainAdapter(MainActivity.this, list);
        recyclerView.setAdapter(adapter);
    }

    private void PopulateData() {
        int x = 0;
        list = new ArrayList<>();
        do {
            list.add(new ListItems("Eno", "12-08-2016", "ini adalah budi", "https://lampukecildotcom.files.wordpress.com/2014/12/gambar-wallpaper-uzumaki-naruto.jpg?w=600&h=426"));
            x++;
        } while (x < 10);

    }
}
