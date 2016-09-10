package com.rsproject.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rsproject.R;

/**
 * Created by Eno on 8/23/2016.
 */
public class LoginActivity extends AppCompatActivity {
    private TextView btn;
    private EditText user, pass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn = (TextView) findViewById(R.id.btn_login);
        user = (EditText) findViewById(R.id.email_login);
        pass = (EditText) findViewById(R.id.pass_login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!user.getText().toString().equalsIgnoreCase("user")
                        && !user.getText().toString().equalsIgnoreCase("admin")) {
                    Toast.makeText(LoginActivity.this, "Akun tidak terdafter", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.putExtra("user", user.getText().toString());
                    startActivity(i);
                    finish();
                }
            }
        });
    }
}
