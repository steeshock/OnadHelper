package ru.steeshock.protocols.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ru.steeshock.protocols.model.Record;

@Database(entities = {Record.class}, version = 1)
public abstract class RecordDatabase extends RoomDatabase {

    public abstract RecordDao getRecordDao();
}
