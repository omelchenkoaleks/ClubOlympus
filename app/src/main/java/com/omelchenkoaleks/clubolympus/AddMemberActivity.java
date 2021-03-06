package com.omelchenkoaleks.clubolympus;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.omelchenkoaleks.clubolympus.data.ClubOlympusContract.MemberEntry;

public class AddMemberActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EDIT_MEMBER_LADER = 111;
    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private EditText mSportEditText;
    private Spinner mGenderSpinner;
    private int mGender = 0;
    private ArrayAdapter mSpinnerAdapter;
    private Uri mCurrentMemberUri;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        Intent intent = getIntent();
        mCurrentMemberUri = intent.getData();
        if (mCurrentMemberUri == null) {
            // если равен null - значит запуск произошел с помощью кнопки добавления
            setTitle("Add a Member");
            /*
                вызываем этот метод, чтобы вызываемым им методом onPrepareOptionsMenu()
                    скрыть опцию удаления,
                        так как это нелогично при создании еще не существующего члена
            */
            invalidateOptionsMenu();
        } else {
            // елси не null, значит содержит тот uri, который указывает на нужную запись в db
            setTitle("Edit a Member");
            /* нужно инициализировать loader = он здесь находится, потому-что он должен
               создаваться только в том случае, если редактируется уже существующий член клуба */
            getSupportLoaderManager().initLoader(EDIT_MEMBER_LADER, null, this);
        }

        mFirstNameEditText = findViewById(R.id.first_name_edit_text);
        mLastNameEditText = findViewById(R.id.last_name_edit_text);
        mSportEditText = findViewById(R.id.sport_edit_text);
        mGenderSpinner = findViewById(R.id.gender_spinner);

        mSpinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.array_gender, android.R.layout.simple_spinner_item);
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGenderSpinner.setAdapter(mSpinnerAdapter);

        /*
            для того, чтобы потом сохранять значения spinner нужно привязать какое-то
            числовое значение для каждой из опций:
         */
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGender = (String) parent.getItemAtPosition(position);

                /*
                    класс TextUtils дает возможность проверить пуста ли строка
                    метод возвращает true, если строка пуста, поэтому мы указываем ! не пуста
                 */
                if (!(TextUtils.isEmpty(selectedGender))) {
                    if (selectedGender.equals("Male")) {
                        mGender = MemberEntry.GENDER_MALE;
                    } else if (selectedGender.equals("Female")) {
                        mGender = MemberEntry.GENDER_FEMALE;
                    } else {
                        mGender = MemberEntry.GENDER_UNKNOWN;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 0;
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (mCurrentMemberUri == null) {
            MenuItem menuItem = menu.findItem(R.id.delete_member);
            menuItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_member_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_member:
                saveMember();
                return true;
            case R.id.delete_member:
                showDeleteMemberDialog();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDeleteMemberDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want delete the member?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteMember();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteMember() {
        if (mCurrentMemberUri != null) {
            int rowsDeleted = getContentResolver()
                    .delete(mCurrentMemberUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this,
                        "Deleting of data from the table failed", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,
                        "Member is deleted", Toast.LENGTH_LONG).show();
            }
            finish();
        }
    }

    private void saveMember() {
        // помещаем извлеченные данные в наши переменные
        String firstName = mFirstNameEditText.getText().toString().trim();
        String lastName = mLastNameEditText.getText().toString().trim();
        String sport = mSportEditText.getText().toString().trim();

        /*
            теперь можно проверить заполнены они или нет ... *
            isEmpty возвращает true, если нет никаких символов в строке (пустая)
         */
        if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(this, "Input the first name",
                    Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(lastName)) {
            Toast.makeText(this, "Input the last name",
                    Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(sport)) {
            Toast.makeText(this, "Input the sport",
                    Toast.LENGTH_LONG).show();
            return;
        } else if (mGender == MemberEntry.GENDER_UNKNOWN) {
            Toast.makeText(this, "Choose the gender",
                    Toast.LENGTH_LONG).show();
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MemberEntry.COLUMN_FIRST_NAME, firstName);
        contentValues.put(MemberEntry.COLUMN_LAST_NAME, lastName);
        contentValues.put(MemberEntry.COLUMN_SPORT, sport);
        contentValues.put(MemberEntry.COLUMN_GENDER, mGender);

        /*
            используем переменную mCurrentMemberUri, чтобы либо просто сохранять новые данные,
            либо сохранять уже отредактированные данные = если null - первое, если нет - второе
         */
        if (mCurrentMemberUri == null) {

            /* этот класс определяет - разрешает какой ContentProvider использовать
                                  в зависимости от authority */
            ContentResolver contentResolver = getContentResolver();
        /*
            здесь объект ContentResolver  по authority (указанному в параметрах)
            определяет какого именно ContentProvider метод insert использовать

            а т.к. в том методе может при ошибке вернуться null, то нужна проверка
         */
            Uri uri = contentResolver.insert(MemberEntry.CONTENT_URI, contentValues);

            if (uri == null) {
                Toast.makeText(this, "Insertion of data in the table failed",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Data saved",
                        Toast.LENGTH_LONG).show();
            }

        } else {
            int rowsChanged = getContentResolver()
                    .update(mCurrentMemberUri, contentValues, null, null);

            if (rowsChanged == 0) {
                Toast.makeText(this,
                        "Saving of data in the table failed", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Member updated", Toast.LENGTH_LONG).show();
            }
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                MemberEntry._ID,
                MemberEntry.COLUMN_FIRST_NAME,
                MemberEntry.COLUMN_LAST_NAME,
                MemberEntry.COLUMN_GENDER,
                MemberEntry.COLUMN_SPORT,
        };

        return new CursorLoader(this,
                mCurrentMemberUri, // указываем uri нашей конктретной записи (который получаем)
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        // теперь здесь нужно извлечь данные, которые были возвращены из предыдущего метода
        if (data.moveToNext()) {
            int firstNameColumnIndex = data.getColumnIndex(MemberEntry.COLUMN_FIRST_NAME);
            int lastNameColumnIndex = data.getColumnIndex(MemberEntry.COLUMN_LAST_NAME);
            int genderColumnIndex = data.getColumnIndex(MemberEntry.COLUMN_GENDER);
            int sportColumnIndex = data.getColumnIndex(MemberEntry.COLUMN_SPORT);

            /*
                получив индексы столбцоа можно теперь извлекать данные по ним, а
                потом присвоить эти данные соответствующим переменным
             */
            String firstName = data.getString(firstNameColumnIndex);
            String lastName = data.getString(lastNameColumnIndex);
            int gender = data.getInt(genderColumnIndex);
            String sport = data.getString(sportColumnIndex);

            mFirstNameEditText.setText(firstName);
            mLastNameEditText.setText(lastName);
            mSportEditText.setText(sport);

            switch (gender) {
                case MemberEntry.GENDER_MALE:
                    mGenderSpinner.setSelection(1);
                    break;
                case MemberEntry.GENDER_FEMALE:
                    mGenderSpinner.setSelection(2);
                    break;
                case MemberEntry.GENDER_UNKNOWN:
                    mGenderSpinner.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
