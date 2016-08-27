package com.siblea.runtimepermission;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @BindView(R.id.files_recycler)
    RecyclerView filesRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            askForStoragePermission();
        } else {
            setAdapter();
        }
    }

    private void askForStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setAdapter();
                } else {
                    displayStoragePermissionSnack();
                }
            }
        }
    }

    private void setAdapter() {
        List<String> minhaLista = new ArrayList<>();
        File list[] = Environment.getExternalStorageDirectory().listFiles();

        for (File aList : list) {
            minhaLista.add(aList.getName());
        }

        filesRecycler.setHasFixedSize(true);
        filesRecycler.setLayoutManager(new LinearLayoutManager(filesRecycler.getContext()));
        filesRecycler.addItemDecoration(new DividerItemDecoration(getBaseContext()));
        filesRecycler.setAdapter(new SimpleStringAdapter<>(minhaLista));
    }

    private void displayStoragePermissionSnack() {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_main), R.string.storage_permission_message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.storage_permission_action, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForStoragePermission();
            }
        });
        snackbar.show();
    }
}
