package ru.steeshock.protocols.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSettings {

    public SharedPreferences mSharedPreferences;

    private static final String PREFERENCES_FILE_NAME = "PREFERENCES_FILE_NAME";

    public static boolean HIDE_RECORDS_FLAG = false; // готовые записи
    public static String HIDE_RECORDS_FLAG_KEY = "HIDE_RECORDS_FLAG_KEY";

    public static boolean HIDE_AUTHOR_FLAG = false; // записи текущего пользователя
    public static String HIDE_AUTHOR_FLAG_KEY = "HIDE_AUTHOR_FLAG_KEY";

    public static String USER_TOKEN = "USER_TOKEN";// username авторизовавшегося пользователя
    public static String USER_TOKEN_KEY = "USER_TOKEN_KEY";

    public static boolean SAVE_USER_AUTH_FLAG = false;// запоминание авторизованных пользователей
    public static String SAVE_USER_AUTH_KEY = "SAVE_USER_AUTH_KEY";

    public static String USER_CREDENTIALS = "Тестовый пользователь...";
    public static String credentials[] = {"Евгений Николаев", "Иван Луговский", "Дмитрий Капаницкий"};

    // флаги сортировок, если флаг установлен - значит пользователь уже выполнял такую сортировку

    public static boolean SORT_RECORDS_BY_PROTOCOL = false;
    public static boolean SORT_RECORDS_BY_DESCRIPTION = false;
    public static boolean SORT_RECORDS_BY_STATUS = false;

    public static boolean SORT_ORDER = true;

    public UserSettings(Context context){
        mSharedPreferences = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }
}
