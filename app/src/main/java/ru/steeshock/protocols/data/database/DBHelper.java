package ru.steeshock.protocols.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    final String LOG_TAG = "myLogs";
    final String DB_NAME = "myDataBase";


    public DBHelper(Context context) {
        super(context, "backup_record_database", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("PRAGMA foreign_keys = 0");

        // создаем таблицу с полями

        db.execSQL("create table RecordsTable ("
                + "_id integer primary key autoincrement,"
                + "protocol_number text,"
                + "act_number text,"
                + "description text,"
                + "status_str text,"
                + "status_num integer,"
                + "first_date integer,"
                + "last_date integer,"
                + "user_token text" + ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
