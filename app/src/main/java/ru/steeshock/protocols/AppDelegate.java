package ru.steeshock.protocols;

import android.app.Application;
import android.arch.persistence.room.Room;

import ru.steeshock.protocols.database.RecordDatabase;

public class AppDelegate extends Application {

        private RecordDatabase mRecordDatabase;

        @Override
        public void onCreate() {
            super.onCreate();

            mRecordDatabase = Room.databaseBuilder(getApplicationContext(), RecordDatabase.class, "record_database")
                    .allowMainThreadQueries()
                    .build();
        }

        public RecordDatabase getRecordDatabase () {return mRecordDatabase;}
    }
