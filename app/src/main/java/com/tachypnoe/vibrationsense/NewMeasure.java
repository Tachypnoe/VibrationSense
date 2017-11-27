package com.tachypnoe.vibrationsense;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import data.DatabaseContract;
import data.DatabaseHelper;

import static com.tachypnoe.vibrationsense.R.id.new_id;

public class NewMeasure extends AppCompatActivity {
    private EditText mName;
    private EditText mDisease;
    private EditText mAge;
    private EditText mMisc;

    String name;
    String misc;
    String disease;
    String ageString;
    String genderString;

    Button next;

    Spinner spinnerGender;
    public int gender = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_new_measure);

        // Обращаемся к текстовым полям, спиннеру и кнопке
        mName = (EditText) findViewById(R.id.new_id);
        mDisease = (EditText) findViewById(R.id.disease);
        mAge = (EditText) findViewById(R.id.age);
        mMisc = (EditText) findViewById(R.id.miscData);
        spinnerGender= (Spinner)findViewById(R.id.spinnerGender);
        next = (Button)findViewById(R.id.activity_new_measure);


        // адаптер
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinnerGender.setAdapter(adapter);
        // заголовок
        spinnerGender.setPrompt(getString(R.string.gender_male));
        // выделяем элемент
        spinnerGender.setSelection(1);
        // устанавливаем обработчик нажатия
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                String selection =  spinnerGender.getSelectedItem().toString();
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_female))) {
                        gender =  DatabaseContract.Measures.GENDER_FEMALE; // женщина
                    } else if (selection.equals(getString(R.string.gender_male))) {  //selection.equals(getString(R.string.gender_male))
                        gender = DatabaseContract.Measures.GENDER_MALE; // мужчина
                    } else {
                        gender = DatabaseContract.Measures.GENDER_UNKNOWN; // unknown
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) { gender = 2;
            }
        });
/*
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        spinnerGender.setAdapter(genderSpinnerAdapter);
        spinnerGender.setPrompt(getString(R.string.gender_male));
        spinnerGender.setSelection(1);

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection =  spinnerGender.getSelectedItem().toString();
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_female))) {
                        gender =  DatabaseContract.Measures.GENDER_FEMALE; // female DatabaseContract.Measures.GENDER_FEMALE
                    } else if (selection.equals(getString(R.string.gender_male))) {  //selection.equals(getString(R.string.gender_male))
                        gender = 1; // male DatabaseContract.Measures.GENDER_MALE
                    } else {
                        gender = DatabaseContract.Measures.GENDER_UNKNOWN; // unknown
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                gender = 2; // Unknown
            }
        });
        //setupSpinner();
*/
    }
    // Метод для проверки данных полей
    public boolean validate(EditText[] fields){
        for(int i=0; i<fields.length; i++){
            EditText currentField=fields[i];
            if(currentField.getText().toString().length()<=0){
                return false;
            }
        }
        return true;
    }
    // Настраиваем спиннер
    /*
    private void setupSpinner() {

        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        spinnerGender.setAdapter(genderSpinnerAdapter);
        spinnerGender.setPrompt(getString(R.string.gender_male));
        spinnerGender.setSelection(1);

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection =  spinnerGender.getSelectedItem().toString();
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_female))) {
                        gender =  DatabaseContract.Measures.GENDER_FEMALE; // female DatabaseContract.Measures.GENDER_FEMALE
                    } else if (selection.equals(getString(R.string.gender_male))) {  //selection.equals(getString(R.string.gender_male))
                        gender = 1; // male DatabaseContract.Measures.GENDER_MALE
                    } else {
                        gender = DatabaseContract.Measures.GENDER_UNKNOWN; // unknown
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                gender = 2; // Unknown
            }
        });
    }         */

    // Кнопка для перехода на вибрацию
        public void onExactMeasureClick(View view) {


                // Считываем данные из текстовых полей
                name = mName.getText().toString();
                misc = mMisc.getText().toString();
                disease = mDisease.getText().toString();
                ageString = mAge.getText().toString();
                genderString = Integer.toString(gender);

            // Проверяем, пусты ли поля
            boolean fieldsNotEmpty = validate(new EditText[]{
                    mName, mMisc, mDisease, mAge
            });

            if ((fieldsNotEmpty) & (name.matches("[" +                   //начало списка допустимых символов
                                                 "а-яА-ЯёЁ" +            //буквы русского алфавита
                                                 "[A-Za-zÀ-ÿ]" +         //латиница и акцентированные символы
                                                 "\\d" +                 //цифры
                                                 "\\s" +                 //знаки-разделители (пробел, табуляция и т.д.)
                                                 "\\p{Punct}" +          //знаки пунктуации
                                                 "]" +                   //конец списка допустимых символов
                    "*"))) {
                Intent toVibrating = new Intent(this, Vibrating.class);
                toVibrating.putExtra("passName", name);
                toVibrating.putExtra("passDisease", disease);
                toVibrating.putExtra("passMisc", misc);
                toVibrating.putExtra("passGender", genderString);
                toVibrating.putExtra("passAge", ageString);
                startActivity(toVibrating);
            } else {
                // Если нет, не показываем кнопку и выдаем предупреждение
                Toast warning = Toast.makeText(getApplicationContext(), getString(R.string.toasterWarning), Toast.LENGTH_LONG);
                warning.show();
            }

    }

}
