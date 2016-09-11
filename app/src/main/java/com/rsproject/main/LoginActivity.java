package com.rsproject.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rsproject.R;
import com.rsproject.utils.ConnectionDetector;
import com.rsproject.utils.ConnectionManager;

import org.json.JSONObject;

/**
 * Created by Eno on 8/23/2016.
 */
public class LoginActivity extends AppCompatActivity {
    private TextView btn;
    private EditText user, pass;
    private Boolean isLogin;
    private ProgressDialog _progressDialog;
    private boolean isTaskRunning = false;
    private String response, data, username, password, email, nama, level, nik, message;
    private JSONObject jsonObject, jsonData;
    private Boolean isInternetActive = false;
    private ConnectionDetector cd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUserSession();
        setContentView(R.layout.activity_login);
        btn = (TextView) findViewById(R.id.btn_login);
        user = (EditText) findViewById(R.id.email_login);
        pass = (EditText) findViewById(R.id.pass_login);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cd = new ConnectionDetector(LoginActivity.this);
                isInternetActive = cd.isConnectingToInternet();
                if (isInternetActive){
                    new RequestLogin().execute();
                }else {
                    Toast.makeText(LoginActivity.this, "Periksa Koneksi Internet Anda...!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    class RequestLogin extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            username = user.getText().toString();
            password = pass.getText().toString();
            if (!isTaskRunning) {
                if (_progressDialog != null) {
                    _progressDialog.dismiss();
                }
                isTaskRunning = true;
                _progressDialog = new ProgressDialog(LoginActivity.this);
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
                String url = ConnectionManager.URL_LOGIN;
                response = ConnectionManager.requestLogin(url, username, password, LoginActivity.this);
                if (response != null) {
                    jsonObject = new JSONObject(response);
                    message = jsonObject.getString("message");
                    data = jsonObject.getString("user");
                    jsonData = new JSONObject(data);
                    username = jsonData.getString("username");
                    nama = jsonData.getString("nama_lengkap");
                    email = jsonData.getString("email");
                    level = jsonData.getString("level");
                    nik = jsonData.getString("nik");
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
            if (message.equalsIgnoreCase("User tidak terdaftar.")) {
                Toast.makeText(LoginActivity.this, "Akun tidak terdafter", Toast.LENGTH_SHORT).show();
            } else {
                setUserSesson();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.putExtra("level", level);
                i.putExtra("user", user.getText().toString());
                startActivity(i);
                finish();
            }
        }


    }

    private void setUserSesson() {
        SharedPreferences shered = getSharedPreferences("isLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor sheredEdit = shered.edit();
        sheredEdit.putBoolean("isLogin", true);
        sheredEdit.putString("username", username);
        sheredEdit.putString("nama", nama);
        sheredEdit.putString("email", email);
        sheredEdit.putString("nik", nik);
        sheredEdit.putString("level", level);
        sheredEdit.commit();
    }

    private void getUserSession() {
        String admin, user;
        SharedPreferences session;
        session = getSharedPreferences("isLogin", Context.MODE_PRIVATE);
        isLogin = session.getBoolean("isLogin", false);
        admin = session.getString("level", null);
        user = session.getString("username", null);
        if (isLogin) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            i.putExtra("level", admin);
            i.putExtra("user", user);
            startActivity(i);
            finish();
        }
    }
}
