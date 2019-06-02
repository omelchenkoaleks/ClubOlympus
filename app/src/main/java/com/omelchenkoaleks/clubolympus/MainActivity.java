package com.omelchenkoaleks.clubolympus;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.omelchenkoaleks.clubolympus.data.ClubOlympusContract.MemberEntry;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {
    // константа для идентификации CursorLoader
    private static final int MEMBER_LOADER = 123;
    // адаптер для нашего ListView
    MemberCursorAdapter mMemberCursorAdapter;

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = findViewById(R.id.data_list_view);

        FloatingActionButton floatingActionButton = findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddMemberActivity.class);
                startActivity(intent);
            }
        });

        mMemberCursorAdapter = new MemberCursorAdapter(this, null, false);
        mListView.setAdapter(mMemberCursorAdapter);
        getSupportLoaderManager().initLoader(MEMBER_LOADER, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        /*
            в этом методе будут происходит все ресурсно-затратные операции с данными
            и, после того как они будут выполнены будет возвращаться объект Loader<Cursor> -
            сразу запустится следующий метод onLoadFinished который примет его (этот объект) в качестве параметра
            Поэтому, в том методе уже будем эти данные располагать в нашем интерфейсе - т.е.
            передадим нашему адаптеру, чтобы они отобразились в нашем ListView
         */
        String[] projection = {
                MemberEntry._ID,
                MemberEntry.COLUMN_FIRST_NAME,
                MemberEntry.COLUMN_LAST_NAME,
                MemberEntry.COLUMN_SPORT,
        };

        CursorLoader cursorLoader = new CursorLoader(this,
                MemberEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mMemberCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        /*
            этот метод используется для того, чтобы удалять невилдные cursor
            это нужно чтобы не было утечек памяти ...
         */
        mMemberCursorAdapter.swapCursor(null);
    }
}
