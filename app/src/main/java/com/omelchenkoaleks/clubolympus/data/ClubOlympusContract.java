package com.omelchenkoaleks.clubolympus.data;

import android.provider.BaseColumns;

/*
    final - значит, что от этого класса нельзя будет унаследоваться (это чисто
        вспомогательный класс, который используется как какой-то контейнер,
        чтобы легко можно было получить доступ к информации в одном месте

        внутри этого класса можно создать несколько внутренних классов для каждой из
        таблиц в нашем приложении

    название Contract используется в Андроид для сохранения данных в db
    в этом классе будет храниться различная информация - версия db, название db,
    название таблиц db и т.п.
 */
public final class ClubOlympusContract {

    /*
        этот конструктор - гарантия, что никто не сможет создать объект этого класса
     */
    private ClubOlympusContract() { }

    /*
        т.к. база данных для всего приложения, а не только для
        одной таблицы - ее название указываем здесь
     */
    public static final String DATABASE_NAME = "membersDB";
    public static final int DATABASE_VERSION = 1;

    public static final class MemberEntry implements BaseColumns {
        public static final String TABLE_NAME = "members";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_FIRST_NAME = "firstName";
        public static final String COLUMN_LAST_NAME = "lastName";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_SPORT = "sport";

        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;
    }
}
