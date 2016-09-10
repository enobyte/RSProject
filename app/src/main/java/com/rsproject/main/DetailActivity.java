package com.rsproject.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.rsproject.R;

/**
 * Created by Eno on 8/31/2016.
 */
public class DetailActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private Toolbar tb;
    private String fabActive;
    private SharedPreferences session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setTitle("Budi Gunawan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        session = getSharedPreferences("active", Context.MODE_PRIVATE);
        fabActive = session.getString("active", null);
        //Log.d("active", fabActive);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (fabActive != null){
                    fab.setVisibility(View.INVISIBLE);
                }else {
                    fab.setVisibility(View.VISIBLE);
                }
            }
        });



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailActivity.this, ChatActivity.class));
                finish();
            }
        });

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


        /*sendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showList();
            }
        });

    }*/
    }

    /*private void showList() {
        list.add(new ListChatItem("Eno", "12-01-2016",inputText.getText().toString(), "1"));
        LinearLayoutManager layoutParams = new LinearLayoutManager(DetailActivity.this);
        recyclerView.setLayoutManager(layoutParams);
        adapter = new RoomChatAdapter(list);
        recyclerView.setAdapter(adapter);

        inputText.setText("");
    }*/
        @Override
        public boolean dispatchTouchEvent (MotionEvent ev){
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
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
