package com.tachypnoe.vibrationsense;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import data.DatabaseHelper;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

    }

    public void onNewBtnClick(View view) {
        Intent toNewMeasure = new Intent(MainActivity.this, NewMeasure.class);
        startActivity(toNewMeasure);
    }

    public void onDataBtnClick(View view) {
        Intent toDatabase = new Intent(MainActivity.this, Database.class);
        startActivity(toDatabase);
    }
}
