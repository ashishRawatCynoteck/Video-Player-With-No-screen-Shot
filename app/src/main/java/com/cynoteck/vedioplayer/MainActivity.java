package com.cynoteck.vedioplayer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

public class MainActivity extends FragmentActivity {
    Button button;
    String filemanagerstring,selectedImagePath;
    Activity activity;
    String[] storagePermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        activity = MainActivity.this;

        button = findViewById(R.id.selectBT);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        VedioPlayer.class);
//                intent.putExtra("path", selectedImagePath);
                startActivity(intent);
//                if (checkPermission()) {
//                    Intent intent = new Intent();
//                    intent.setType("video/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(intent,"Select Video"),1);
//                } else {
//                    requestPermission();
//                }



            }
        });
    }
    private boolean checkPermission() {


        return ActivityCompat.checkSelfPermission(activity, storagePermissions[0]) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, storagePermissions[1]) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        try {
            if (ActivityCompat.checkSelfPermission(activity, storagePermissions[0]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(activity, storagePermissions[1]) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(activity, storagePermissions, PERMISSION_REQUEST_CODE);
            } else {
                requestPermission();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri selectedImageUri = data.getData();

                // OI FILE Manager
                filemanagerstring = selectedImageUri.getPath();

                // MEDIA GALLERY
                selectedImagePath = getPath(selectedImageUri);
                if (selectedImagePath != null) {

//                    Intent intent = new Intent(MainActivity.this,
//                            Preview_Video_A.class);
//                    intent.putExtra("path", selectedImagePath);
//                    startActivity(intent);
                }
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }
}