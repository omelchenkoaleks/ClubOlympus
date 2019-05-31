package com.omelchenkoaleks.clubolympus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.omelchenkoaleks.clubolympus.data.ClubOlympusContract.MemberEntry;

public class MainActivity extends AppCompatActivity {
    TextView mDataTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataTextView = findViewById(R.id.data_text_view);

        FloatingActionButton floatingActionButton = findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddMemberActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayData();
    }

    private void displayData() {
        String[] projection = {
                MemberEntry._ID,
                MemberEntry.COLUMN_FIRST_NAME,
                MemberEntry.COLUMN_LAST_NAME,
                MemberEntry.COLUMN_GENDER,
                MemberEntry.COLUMN_SPORT,
        };

        // нужно сделать теперь запрос к db - этот запрос возвращает объект Cursor
        Cursor cursor = getContentResolver().query(
                MemberEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );

        mDataTextView.setText("All members\n\n");
        mDataTextView.append(MemberEntry._ID + " "
                + MemberEntry.COLUMN_FIRST_NAME + " "
                + MemberEntry.COLUMN_LAST_NAME + " "
                + MemberEntry.COLUMN_GENDER + " "
                + MemberEntry.COLUMN_SPORT);

        int idIndex = cursor.getColumnIndex(MemberEntry._ID);
        int idFirstName = cursor.getColumnIndex(MemberEntry.COLUMN_FIRST_NAME);
        int idLastName = cursor.getColumnIndex(MemberEntry.COLUMN_LAST_NAME);
        int idGender = cursor.getColumnIndex(MemberEntry.COLUMN_GENDER);
        int idSport = cursor.getColumnIndex(MemberEntry.COLUMN_SPORT);

        while (cursor.moveToNext()) {
            int currentId = cursor.getInt(idIndex);
            String currentFirstName = cursor.getString(idFirstName);
            String currentLastName = cursor.getString(idLastName);
            int currentGender = cursor.getInt(idGender);
            String currentSport = cursor.getString(idSport);

            mDataTextView.append("\n"
                    + currentId + " "
                    + currentFirstName + " "
                    + currentLastName + " "
                    + currentGender + " "
                    + currentSport);
        }

        cursor.close();
    }
}
