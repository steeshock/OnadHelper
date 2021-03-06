package ru.steeshock.protocols.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import java.util.List;

import ru.steeshock.protocols.data.model.Record;

@Dao
public interface RecordDao {

    //Добавляем записи пачкой
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecords(List<Record> records);

    //Добавляем одну запись
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecord(Record record);

    //Извлекаем записи
    @Query("select * from record")
    List<Record> getRecords();

    //Извлекаем запись по ID
    @Query("select * from record where _id = :recordId")
    Record getRecordById(int recordId);

    //Извлекаем записи по Username
    @Query("select * from record where user_token = :username")
    List<Record> getRecordsByUsername(String username);

    //Извлекаем записи по Stage
    @Query("select * from record where stage = :stage")
    List<Record> getRecordsByStage(int stage);

    //Извлекаем записи по FailureType
    @Query("select * from record where failureType = :failureType")
    List<Record> getRecordsByFailureType(int failureType);

    //извлекаем записи в виде курсора
    @Query("select * from record")
    Cursor getRecordsCursor();

    //Удаляем запись
    @Delete
    void deleteRecord(Record record);
}
