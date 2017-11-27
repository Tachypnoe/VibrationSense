package com.tachypnoe.vibrationsense;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Vibrating extends AppCompatActivity {
    Vibrator vibrator;
    Button onStartBtn;
    Button toEvaluation;
    ProgressBar beautifulBar;


    String name;
    String misc;
    String disease;
    String ageString;
    String gender;

    public long VIBRATION_TIME = 7000; //I want my device to vibrate for 7 seconds
    public TextView displayCountdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_vibrating);


        gender = getIntent().getStringExtra("passGender");

        displayCountdown = (TextView)findViewById(R.id.countdown);
        onStartBtn = (Button)findViewById(R.id.start);
        toEvaluation = (Button)findViewById(R.id.toEvaluation);
        beautifulBar = (ProgressBar)findViewById(R.id.BeautifulBar);

        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
    }

    public void onStartClick(View view) {
        onStartBtn.setVisibility(View.GONE);

        displayCountdown.setText(" ");
        displayCountdown.setTextSize(48);
        beautifulBar.setVisibility(View.VISIBLE);
        new CountDownTimer(VIBRATION_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                displayCountdown.setText(" "+ millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                beautifulBar.setVisibility(View.GONE);
                displayCountdown.setVisibility(View.GONE);
                toEvaluation.setVisibility(View.VISIBLE);
            }

        }.start();

        vibrator.vibrate(VIBRATION_TIME);
    }

    public void toEvaluation(View view) {
        // получаем переданные данные с прошлой активности
        name = getIntent().getStringExtra("passName");
        misc = getIntent().getStringExtra("passMisc");
        disease = getIntent().getStringExtra("passDisease");
        ageString = getIntent().getStringExtra("passAge");
        gender = getIntent().getStringExtra("passGender");
        // начинаем новую активность
        Intent evaluation = new Intent(this, Evaluation.class);
        evaluation.putExtra("passName", name);
        evaluation.putExtra("passDisease", disease);
        evaluation.putExtra("passMisc", misc);
        evaluation.putExtra("passGender", gender);
        evaluation.putExtra("passAge", ageString);
        startActivity(evaluation);
    }
}
