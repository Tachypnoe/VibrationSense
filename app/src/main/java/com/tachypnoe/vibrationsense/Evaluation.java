package com.tachypnoe.vibrationsense;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import data.DatabaseContract;
import data.DatabaseHelper;

public class Evaluation extends AppCompatActivity {

    public int sensitivity = 0;
    TextView evaluationWritten;
    SeekBar evaluationBar;
    EditText mArea;
    EditText mCompare;
    Button btnSave;

    //String passedData[];
    String name;
    String misc;
    String disease;
    String ageString;
    String strGender;
    String area;
    int compare;
    int age;
    int gender;
    DatabaseHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_evaluation);

        mArea = (EditText)findViewById(R.id.insertArea);
        mCompare = (EditText)findViewById(R.id.insertCompare);
        evaluationWritten = (TextView)findViewById(R.id.evaluationWritten);
        btnSave = (Button)findViewById(R.id.btnSave);



        evaluationBar = (SeekBar)findViewById(R.id.evaluationBar);
        evaluationBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar evaluationBar, int progressValue, boolean fromUser) {
                sensitivity = progressValue;
            }
            @Override
            public void onStartTrackingTouch(SeekBar evaluationBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {
                // значение прогресса отображаем в TextView
                evaluationWritten.setText(" "+sensitivity+" ");
            }
        });

    }

    public boolean validate(EditText[] fields){
        for(int i=0; i<fields.length; i++){
            EditText currentField=fields[i];
            if(currentField.getText().toString().length()<=0){
                return false;
            }
        }
        return true;
    }


    public void onRetryBtnClick(View view) {
        Intent toVibrating = new Intent(Evaluation.this, Vibrating.class);
        startActivity(toVibrating);
    }


    public void onSaveClick(View view) {
        boolean fieldsNotEmpty = validate(new EditText[]{mArea, mCompare});
        if ((fieldsNotEmpty) & (mArea.getText().toString().matches("[" +                   //начало списка допустимых символов
                                                                   "а-яА-ЯёЁ" +            //буквы русского алфавита
                                                                   "[A-Za-zÀ-ÿ]" +         //латиница и акцентированные символы
                                                                   "\\d" +                 //цифры
                                                                   "\\s" +                 //знаки-разделители (пробел, табуляция и т.д.)
                                                                   "\\p{Punct}" +          //знаки пунктуации
                                                                   "]" +                   //конец списка допустимых символов
                "*"))) {
            name = getIntent().getStringExtra("passName");
            misc = getIntent().getStringExtra("passMisc");
            disease = getIntent().getStringExtra("passDisease");
            ageString = getIntent().getStringExtra("passAge");
            strGender = getIntent().getStringExtra("passGender");

            age = Integer.parseInt(ageString);
            gender = Integer.parseInt(strGender);
            area = mArea.getText().toString().trim();
            compare = Integer.parseInt(mCompare.getText().toString().trim());

            DatabaseHelper mDbHelper = DatabaseHelper.getInstance(this);

            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DatabaseContract.Measures.COLUMN_NAME, name);
            values.put(DatabaseContract.Measures.COLUMN_MISC, misc);
            values.put(DatabaseContract.Measures.COLUMN_DISEASE, disease);
            values.put(DatabaseContract.Measures.COLUMN_GENDER, gender);
            values.put(DatabaseContract.Measures.COLUMN_AGE, age);
            values.put(DatabaseContract.Measures.COLUMN_AREA, area);
            values.put(DatabaseContract.Measures.COLUMN_COMPARE, compare);
            values.put(DatabaseContract.Measures.COLUMN_MEASURE, sensitivity);

            // Вставляем новый ряд в базу данных и запоминаем его идентификатор
            long newRowId = db.insert(DatabaseContract.Measures.TABLE_NAME, null, values);

            // Выводим сообщение в успешном случае или при ошибке
            if (newRowId == -1) {
                // Если ID  -1, значит произошла ошибка
                Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, getString(R.string.done), Toast.LENGTH_SHORT).show();
            }
            Intent toMainMenu = new Intent(this, MainActivity.class);
            startActivity(toMainMenu);
        } else {
            // Если нет, не показываем кнопку и выдаем предупреждение
            Toast warning = Toast.makeText(getApplicationContext(), getString(R.string.toasterWarning), Toast.LENGTH_LONG);
            warning.show();
        }

    }

    public void onCancelClick(View view) {
        Intent Cancel = new Intent(Evaluation.this, MainActivity.class);
        startActivity(Cancel);
    }
}
