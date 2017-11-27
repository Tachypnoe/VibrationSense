package com.tachypnoe.vibrationsense;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatatypeMismatchException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import data.DatabaseHelper;
import data.DatabaseContract;

import static data.DatabaseContract.Measures.TABLE_NAME;

public class Database extends AppCompatActivity {


    SQLiteDatabase db;
    private String m_Text = "";
    private int pickedId[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_database);

        Toolbar toolbar = (Toolbar) findViewById(R.id.dbToolbar);
        setSupportActionBar(toolbar);

        try {
            displayDatabaseInfo();
        } catch (SQLException mSQLException) {
            if(mSQLException instanceof SQLiteConstraintException){
                Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show();
            }else if(mSQLException instanceof SQLiteDatatypeMismatchException) {
                Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show();
            }else{
                throw mSQLException;
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.db_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_multiple:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.choose_multiple));
                builder.setMessage(getResources().getString(R.string.popupDelete));
                // Set up the input
                final EditText input = new EditText(this);

                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        String[] parts = m_Text.split(",");
                        int[] n1 = new int[parts.length];
                        for(int n = 0; n < parts.length; n++) {
                            try {
                                n1[n] = Integer.parseInt(parts[n]);
                                db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE _id IN (" + n1[n] + ")");

                            } catch (NumberFormatException e) {
                                Toast warning = Toast.makeText(getApplicationContext(),
                                        getString(R.string.toaster_deleteWarning), Toast.LENGTH_LONG);
                                dialog.cancel();
                                warning.show();
                            }

                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                return true;
            case R.id.delete_all:
                db.execSQL("DELETE FROM " + TABLE_NAME);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void displayDatabaseInfo() {
        DatabaseHelper mDbHelper = DatabaseHelper.getInstance(this);
        db = mDbHelper.getReadableDatabase(); // open database
        db.beginTransaction();

        String[] projection = {
                DatabaseContract.Measures._ID,
                DatabaseContract.Measures.COLUMN_NAME,
                DatabaseContract.Measures.COLUMN_DISEASE,
                DatabaseContract.Measures.COLUMN_GENDER,
                DatabaseContract.Measures.COLUMN_AGE,
                DatabaseContract.Measures.COLUMN_MISC,
                DatabaseContract.Measures.COLUMN_MEASURE,
                DatabaseContract.Measures.COLUMN_AREA,
                DatabaseContract.Measures.COLUMN_COMPARE};

        /*Обращаемся к таблице в лейауте и создаем строку заголовка */
        TableLayout dataTable = (TableLayout)findViewById(R.id.dataTable);
        TableRow rowHeader = new TableRow(this);
        rowHeader.setBackgroundColor(Color.parseColor("#c0c0c0"));
        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT));
        for(String c:projection) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(18);
            tv.setPadding(5, 5, 5, 5);
            tv.setText(c);
            rowHeader.addView(tv);
        }
        dataTable.addView(rowHeader);

        //TextView displayTextView = (TextView) findViewById(R.id.table);

        Cursor cursor = db.query(
                DatabaseContract.Measures.TABLE_NAME,   // таблица
                projection,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки
        try {
            int idColumnIndex = cursor.getColumnIndex(DatabaseContract.Measures._ID);
            int nameColumnIndex = cursor.getColumnIndex(DatabaseContract.Measures.COLUMN_NAME);
            int diseaseColumnIndex = cursor.getColumnIndex(DatabaseContract.Measures.COLUMN_DISEASE);
            int genderColumnIndex = cursor.getColumnIndex(DatabaseContract.Measures.COLUMN_GENDER);
            int ageColumnIndex = cursor.getColumnIndex(DatabaseContract.Measures.COLUMN_AGE);
            int miscColumnIndex = cursor.getColumnIndex(DatabaseContract.Measures.COLUMN_MISC);
            int measureColumnIndex = cursor.getColumnIndex(DatabaseContract.Measures.COLUMN_MEASURE);
            int areaColumnIndex = cursor.getColumnIndex(DatabaseContract.Measures.COLUMN_AREA);
            int compareColumnIndex = cursor.getColumnIndex(DatabaseContract.Measures.COLUMN_COMPARE);

            // Проходим через все ряды
            while (cursor.moveToNext()) {
                // Используем индекс для получения строки или числа
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentDisease = cursor.getString(diseaseColumnIndex);
                int currentGender = cursor.getInt(genderColumnIndex);
                int currentAge = cursor.getInt(ageColumnIndex);
                String currentMisc = cursor.getString(miscColumnIndex);
                int currentMeasure = cursor.getInt(measureColumnIndex);
                String currentArea = cursor.getString(areaColumnIndex);
                int currentCompare = cursor.getInt(compareColumnIndex);

                // Строим строку
                TableRow row = new TableRow(this);
                row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
                // Выводим значения каждого столбца
                String[] colText={currentID+"", currentName, currentDisease, Integer.toString(currentGender),
                        Integer.toString(currentAge),  currentMisc, Integer.toString(currentMeasure),
                        currentArea, Integer.toString(currentCompare)};
                for(String text:colText) {
                    TextView tv = new TextView(this);
                    tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextSize(16);
                    tv.setPadding(5, 5, 5, 5);
                    tv.setText(text);
                    row.addView(tv);
                }
                dataTable.addView(row);
            }
        }finally {
        // Всегда закрываем курсор после чтения
        cursor.close();
    }

    }
}
