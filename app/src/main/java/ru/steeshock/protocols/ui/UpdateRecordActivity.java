package ru.steeshock.protocols.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import ru.steeshock.protocols.AppDelegate;
import ru.steeshock.protocols.R;
import ru.steeshock.protocols.database.RecordDao;
import ru.steeshock.protocols.model.Record;
import ru.steeshock.protocols.model.RecordAdapter;
import ru.steeshock.protocols.utils.UserSettings;


public class UpdateRecordActivity extends AppCompatActivity {

    private TextView mProtocolNumber;
    private TextView mActNumber;
    private TextView mDescription;
    private Spinner mStatus;
    private Button btnUpd, btnCancel;

    private RecordDao mRecordDao;
    private Record mRecord;

    private int id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.update_record_activity);

        mRecordDao = ((AppDelegate)getApplicationContext()).getRecordDatabase().getRecordDao(); //получаем доступ к экземпляру БД

        id = getIntent().getIntExtra(RecordAdapter.RECORD_ID, 0);
        mRecord = mRecordDao.getRecordById(id);


        mProtocolNumber = findViewById(R.id.etProtocolNumber);
        mActNumber = findViewById(R.id.etActNumber);
        mDescription = findViewById(R.id.etDescription);
        mStatus = findViewById(R.id.spinner);
        btnUpd = findViewById(R.id.btn_add);
        btnCancel = findViewById(R.id.btn_cancel);

        mProtocolNumber.setText(mRecord.getProtocolNumber());
        mActNumber.setText(mRecord.getActNumber());
        mDescription.setText(mRecord.getDescription());
        mStatus.setSelection((int)mRecord.getStatusNum());

        btnUpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Record record = new Record(id, mProtocolNumber.getText().toString(), mActNumber.getText().toString(), mDescription.getText().toString(), mStatus.getSelectedItem().toString(),  mStatus.getSelectedItemId(), System.currentTimeMillis(), UserSettings.USER_TOKEN);
                mRecordDao.insertRecord(record);
                Intent startMainActivity = new Intent(UpdateRecordActivity.this, MainActivity.class);
                startActivity (startMainActivity);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startMainActivity = new Intent(UpdateRecordActivity.this, MainActivity.class);
                startActivity (startMainActivity);
                finish();
            }
        });
    }
}
