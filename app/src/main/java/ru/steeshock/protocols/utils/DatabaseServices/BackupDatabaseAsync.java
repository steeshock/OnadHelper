package ru.steeshock.protocols.utils.DatabaseServices;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.nio.channels.FileChannel;
import java.util.List;

import ru.steeshock.protocols.data.database.DBHelper;
import ru.steeshock.protocols.data.database.RecordDao;
import ru.steeshock.protocols.data.model.Record;
import ru.steeshock.protocols.ui.Records.MainActivity;

public class BackupDatabaseAsync extends AsyncTask<Void, Void, Void> {

    private WeakReference<MainActivity> mActivityWeakReference;
    private RecordDao recordDao;
    private DBHelper dbHelper;

    public BackupDatabaseAsync(MainActivity activity, RecordDao recordDao){
        this.mActivityWeakReference = new WeakReference<>(activity);
        this.recordDao = recordDao;
        this.dbHelper = new DBHelper (activity);
    }

    @Override
    protected Void doInBackground(Void... voids) {

        mActivityWeakReference.get().getApplicationContext().deleteDatabase("backup_record_database");

        List<Record> mRecords = recordDao.getRecords();

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for(Record record:mRecords) {

            // создаем объект для данных
            ContentValues cv = new ContentValues();

            String protocol_number = record.getProtocolNumber();
            String act_number = record.getActNumber();
            String description = record.getDescription();
            Long status_num = record.getStatusNum();
            Long firstDate = record.getFirstDate();
            Long lastDate = record.getLastDate();
            Long stage = record.getStage();
            Long failureType = record.getFailureType();
            String user_token = record.getUserToken();

            cv.put("protocol_number", protocol_number);
            cv.put("act_number", act_number);
            cv.put("description", description);
            cv.put("status_num", status_num);
            cv.put("first_date", firstDate);
            cv.put("last_date", lastDate);
            cv.put("stage", stage);
            cv.put("failure_type", failureType);
            cv.put("user_token", user_token);

            db.insert("RecordsTable", null, cv);
        }

        db.close();

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite())
            {
                String currentDBPath = "//data//ru.steeshock.protocols//databases//backup_record_database";
                String backupDBPath = "backup_record_database";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);
                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            } else Toast.makeText(mActivityWeakReference.get(), "Вставьте SD карту!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(mActivityWeakReference.get(), "Error: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        MainActivity activity = mActivityWeakReference.get();
        Toast.makeText(activity, "Backup done", Toast.LENGTH_LONG).show();
    }
}
