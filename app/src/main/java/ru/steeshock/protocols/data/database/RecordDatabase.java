package ru.steeshock.protocols.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ru.steeshock.protocols.data.model.Record;

@Database(entities = {Record.class}, version = 1)
public abstract class RecordDatabase extends RoomDatabase {

    public abstract RecordDao getRecordDao();
}
