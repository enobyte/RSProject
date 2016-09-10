package com.rsproject.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.Spinner;

import com.rsproject.R;
import com.rsproject.utils.AlbumStorageDirFactory;
import com.rsproject.utils.BaseAlbumDirFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by Eno on 8/31/2016.
 */
public class AddIssueActivity extends AppCompatActivity {
    private FloatingActionButton save;
    private Spinner level;
    private Toolbar toolbar;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private File f;
    private String picturePath;
    private ImageView img, zoom, take, close_prev, prev;
    private Bitmap bitmap;
    private Dialog alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addissue);
        save = (FloatingActionButton) findViewById(R.id.save);
        level = (Spinner) findViewById(R.id.level);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        img = (ImageView) findViewById(R.id.img_issue);
        zoom = (ImageView) findViewById(R.id.zoom);
        take = (ImageView) findViewById(R.id.take);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Form Keluhan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mAlbumStorageDirFactory = new BaseAlbumDirFactory();
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


}
