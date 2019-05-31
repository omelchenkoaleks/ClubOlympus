package com.omelchenkoaleks.clubolympus.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.omelchenkoaleks.clubolympus.data.ClubOlympusContract.MemberEntry;

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
        SQLiteDatabase db = mOlympusDbOpenHelper.getReadableDatabase();
        Cursor cursor;

        int match = uriMatcher.match(uri); // match - соответствие

        /*
            ПАРАМЕТРЫ:
                1. один из видов реализованных uri:
                    например: content://com.omelchenkoaleks.clubolympus/memebers/34
                2. projection - имена столбцов (пример, если нам нужно вывести COLUMN_LAST_NAME и
                COLUMN_GENDER, то массив projection будет такого вида:
                    projection = {"lastName", "gender",}
                3. selection = "_id=?" в SQL коде вместо ? будут подставленны аргументы, которые
                мы указываем в selectionArgs (a в selection указывается способ отбора)
                4. selectionArgs = 34 (та, последняя часть uri будет преобразована в число (long)

         */
        switch (match) {
            case MEMBERS:
                cursor = db.query(MemberEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case MEMBER_ID:
                selection = MemberEntry._ID + "=?"; // selection (отбор) - указываем параметр отбора (по столбцу id)
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(MemberEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Can`t query incorrect URI " + uri);
        }

        return cursor;
    }

    /*
        1. параметр - куда нужно вставить
        2. параметр - данные сохраняются в объект ContentValues (ключ - значение)

        Возвращен будет объект uri уже с id новой созданной строки
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mOlympusDbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);
        switch (match) {
            case MEMBERS:
                long id = db.insert(MemberEntry.TABLE_NAME, null, values);
                if (id == -1) {
                    Log.e("insertMethod", "Insertion of data in the table for " + uri);
                    return null;
                }
                // возвращаем с добавленным id
                return ContentUris.withAppendedId(uri, id);
            default:
                throw new IllegalArgumentException("Insertion of data in the table for " + uri);
        }
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
