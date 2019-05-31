package com.omelchenkoaleks.clubolympus.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class OlympusContentProvider extends ContentProvider {
    OlympusDbOpenHelper mOlympusDbOpenHelper;

    private static final int MEMBERS = 111;
    private static final int MEMBER_ID = 222;

    /*
        Зачем нужен объект этого класса? его можно назвать сопоставитель uri

        В этом приложении мы будем использовать такие uri
            content://com.omelchenkoaleks.clubolympus/memebers - к таблице members
            content://com.omelchenkoaleks.clubolympus/memebers/34 - к конкретной строке в таблице по id - 34
        при разных видах uri наши методы должны вести себя по разному - ВОТ, именно это и
        решает UriMatcher

        Как он это делает? сопоставляет целое число и uri, поэтому для каждого вида uri мы
        создаем отдельный код (например, 1001, 1002) и вот по этим числам UriMatcher будет знать
        какой способ работы нужно выбирать (работать с отдельной строкой или со всей таблицей)
     */
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // в статическом блоке код выполняется до создания объекта нашего класса
    static {
        uriMatcher.addURI(ClubOlympusContract.AUTHORITY,
                ClubOlympusContract.PATH_MEMBERS, MEMBERS);
        uriMatcher.addURI(ClubOlympusContract.AUTHORITY,
                ClubOlympusContract.PATH_MEMBERS + "/#", MEMBER_ID);
    }

    @Override
    public boolean onCreate() {
        mOlympusDbOpenHelper = new OlympusDbOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}
