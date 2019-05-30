package com.omelchenkoaleks.clubolympus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class AddMemberActivity extends AppCompatActivity {
    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private EditText mGroupEditText;
    private Spinner mGenderSpinner;
    private int mGender = 0;
    private ArrayAdapter mSpinnerAdapter;
    private ArrayList mSpinnerArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        mFirstNameEditText = findViewById(R.id.first_name_edit_text);
        mLastNameEditText = findViewById(R.id.last_name_edit_text);
        mGroupEditText = findViewById(R.id.group_edit_text);
        mGenderSpinner = findViewById(R.id.gender_spinner);

        mSpinnerArrayList = new ArrayList();
        mSpinnerArrayList.add("Unknown");
        mSpinnerArrayList.add("Male");
        mSpinnerArrayList.add("Female");

        mSpinnerAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, mSpinnerArrayList);
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGenderSpinner.setAdapter(mSpinnerAdapter);
    }
}
