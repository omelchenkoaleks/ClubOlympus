package com.omelchenkoaleks.clubolympus;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class AddMemberActivity extends AppCompatActivity {
    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private EditText mGroupEditText;
    private Spinner mGenderSpinner;
    private int mGender = 0;
    private ArrayAdapter mSpinnerAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        mFirstNameEditText = findViewById(R.id.first_name_edit_text);
        mLastNameEditText = findViewById(R.id.last_name_edit_text);
        mGroupEditText = findViewById(R.id.group_edit_text);
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
                        mGender = 1;
                    } else if (selectedGender.equals("Female")) {
                        mGender = 2;
                    } else {
                        mGender = 0;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 0;
            }
        });
    }
}
