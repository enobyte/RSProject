package com.rsproject.main;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rsproject.R;
import com.rsproject.utils.AlbumStorageDirFactory;
import com.rsproject.utils.BaseAlbumDirFactory;
import com.rsproject.utils.ConnectionDetector;
import com.rsproject.utils.ConnectionManager;
import com.rsproject.utils.GPSTracker;
import com.rsproject.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eno on 8/31/2016.
 */
public class AddIssueActivity extends AppCompatActivity {
    private FloatingActionButton save;
    private Spinner level, category;
    private Toolbar toolbar;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private File f;
    private String picturePath, response, user, urgensi, title, ket, latitude, longitude, imageName,
            message, id_kategori_laporan, kategori_laporan, keterangan_kategori, id_category;
    private ImageView img, zoom, take, close_prev, prev;
    private Bitmap bitmap;
    private Dialog alertDialog;
    private TextView lat, lon;
    private GPSTracker gps;
    private EditText judul, keterangan;
    private Utils utils;
    private ProgressDialog _progressDialog;
    private boolean isTaskRunning = false;
    private JSONObject jsonObj;
    private SharedPreferences sharedPreferences;
    private List<String> kategori = new ArrayList<String>();
    private Boolean isInternetActive = false;
    private ConnectionDetector cd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addissue);
        gps = new GPSTracker(this);
        save = (FloatingActionButton) findViewById(R.id.save);
        level = (Spinner) findViewById(R.id.level);
        category = (Spinner) findViewById(R.id.kategori);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        img = (ImageView) findViewById(R.id.img_issue);
        zoom = (ImageView) findViewById(R.id.zoom);
        take = (ImageView) findViewById(R.id.take);
        lat = (TextView) findViewById(R.id.lat);
        lon = (TextView) findViewById(R.id.lon);
        judul = (EditText) findViewById(R.id.judul);
        keterangan = (EditText) findViewById(R.id.ringkasan);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Form Keluhan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        new requestCategory().execute();
        getCordinate();
        takeImage();

        zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowImage();
            }
        });

        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeImage();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (level.getSelectedItemPosition() == 0) {
                    Toast.makeText(AddIssueActivity.this, "Select One", Toast.LENGTH_SHORT).show();
                } else {
                    urgensi = level.getSelectedItem().toString();
                    title = judul.getText().toString();
                    ket = keterangan.getText().toString();
                    latitude = lat.getText().toString();
                    longitude = lon.getText().toString();
                    imageName = f.getName();
                    cd = new ConnectionDetector(AddIssueActivity.this);
                    isInternetActive = cd.isConnectingToInternet();
                    if (isInternetActive) {
                        new requestSaveLaporan().execute();
                    } else {
                        Toast.makeText(AddIssueActivity.this, "Periksa Koneksi Internet Anda...!",
                                Toast.LENGTH_LONG).show();
                    }

                }


            }
        });


        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_category = category.getItemAtPosition(position).toString().substring(0, 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        utils = new Utils(AddIssueActivity.this);

        sharedPreferences = getSharedPreferences("isLogin", Context.MODE_PRIVATE);

        user = sharedPreferences.getString("username", null);

    }

    public void takeImage() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            f = null;
            f = createImageFile();
            picturePath = f.getAbsolutePath();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            startActivityForResult(takePictureIntent, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private File createImageFile() throws IOException {
        String imageFileName = String.valueOf(System.currentTimeMillis());
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, ".jpg", albumF);
        return imageF;
    }

    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir("RS");
            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            {
                setPic();
                galleryAddPic();
            }
        }

    }

    private void setPic() {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, bmOptions);
        bmOptions.inJustDecodeBounds = false;
        final int REQUIRED_SIZE = 1024;
        int width_tmp = bmOptions.outWidth, height_tmp = bmOptions.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        bmOptions.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(picturePath, bmOptions);
        img.setImageBitmap(bitmap);
        img.setVisibility(View.VISIBLE);
        zoom.setVisibility(View.VISIBLE);
        take.setImageResource(R.drawable.tk_owhite);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(picturePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
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

    private void ShowImage() {
        alertDialog = new Dialog(AddIssueActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        LayoutInflater inflater = getLayoutInflater();
        final View root = (View) inflater.inflate(R.layout.image_preview, null);
        prev = (ImageView) root.findViewById(R.id.imagepreview);
        close_prev = (ImageView) root.findViewById(R.id.close_preview);
        close_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        //alertDialog.setView(root);
        alertDialog.setContentView(root);
        prev.setImageBitmap(bitmap);
        prev.setVisibility(View.VISIBLE);
        alertDialog.show();
    }

    private void getCordinate() {
        lat.setText(String.valueOf(gps.getLatitude()));
        lon.setText(String.valueOf(gps.getLongitude()));
    }


    class requestSaveLaporan extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            if (!isTaskRunning) {
                if (_progressDialog != null) {
                    _progressDialog.dismiss();
                }
                isTaskRunning = true;
                _progressDialog = new ProgressDialog(AddIssueActivity.this);
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
                String url = ConnectionManager.URL_SAVE_LAPORAN;
                response = ConnectionManager.requestSaveLaporan(
                        url, id_category, title, ket, latitude, longitude, utils.getCurrentDateandTime(),
                        user, urgensi, "proses", imageName, AddIssueActivity.this);
                if (response != null) {
                    jsonObj = new JSONObject(response);
                    message = jsonObj.getString("message");
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
            if (message.equalsIgnoreCase("Data berhasil disimpan")) {
                startActivity(new Intent(AddIssueActivity.this, MainActivity.class));
                finish();
            }
        }
    }

    class requestCategory extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                JSONObject catObj, catObject;
                JSONArray catArray;
                String url = ConnectionManager.URL_GET_CATEGORY;
                response = ConnectionManager.requestCategory(url, AddIssueActivity.this);
                catObj = new JSONObject(response);
                if (catObj != null) {
                    catArray = catObj.getJSONArray("category");
                    if (catArray.length() > 0) {
                        for (int i = 0; i < catArray.length(); i++) {
                            catObject = catArray.getJSONObject(i);
                            id_kategori_laporan = catObject.getString("id_kategori_laporan");
                            kategori_laporan = catObject.getString("kategori_laporan");
                            keterangan_kategori = catObject.getString("keterangan");
                            kategori.add(id_kategori_laporan + "  " + kategori_laporan);
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
            ArrayAdapter<String> catAdapter = new ArrayAdapter<String>(AddIssueActivity.this,
                    android.R.layout.simple_spinner_item, kategori);
            catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            category.setAdapter(catAdapter);
        }
    }
}
