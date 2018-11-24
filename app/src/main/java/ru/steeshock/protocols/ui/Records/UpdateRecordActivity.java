package ru.steeshock.protocols.ui.Records;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

import ru.steeshock.protocols.AppDelegate;
import ru.steeshock.protocols.R;
import ru.steeshock.protocols.data.database.RecordDao;
import ru.steeshock.protocols.data.model.Record;
import ru.steeshock.protocols.data.model.RecordAdapter;
import ru.steeshock.protocols.utils.DateUtils;


public class UpdateRecordActivity extends AppCompatActivity {

    private TextView mProtocolNumber;
    private TextView mActNumber;
    private TextView mDescription;
    private TextView mUsername;
    private Spinner mStatus, mStage, mFailureType;
    private Button btnUpd, btnCancel;
    private TextView mFirstDate, mLastDate;

    private RecordDao mRecordDao;
    private Record mRecord;
    private Calendar dateAndTime = Calendar.getInstance();

    private int id;
    private Long mFirstDateLong;
    private Long mLastDateLong;

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
        mStatus = findViewById(R.id.spinnerStatus);
        mStage = findViewById(R.id.spinnerStage);
        mFailureType = findViewById(R.id.spinnerFailureType);
        mUsername = findViewById(R.id.tvUsername);
        btnUpd = findViewById(R.id.btn_add);
        btnCancel = findViewById(R.id.btn_cancel);
        mFirstDate = findViewById(R.id.tvFirstDate);
        mLastDate = findViewById(R.id.tvLastDate);

        mProtocolNumber.setText(mRecord.getProtocolNumber());
        mActNumber.setText(mRecord.getActNumber());
        mDescription.setText(mRecord.getDescription());
        mStatus.setSelection((int)mRecord.getStatusNum());
        mStage.setSelection((int)mRecord.getStage());
        mFailureType.setSelection((int)mRecord.getFailureType());
        mFirstDate.setText(DateUtils.format(mRecord.getFirstDate()));
        mLastDate.setText(DateUtils.format(mRecord.getLastDate()));

        mFirstDateLong = mRecord.getFirstDate();
        mLastDateLong = mRecord.getLastDate();

        mUsername.setText("Пользователь: " + mRecord.getUserToken());

        btnUpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Record record = new Record(id, mProtocolNumber.getText().toString(), mActNumber.getText().toString(), mDescription.getText().toString(),  mStatus.getSelectedItemId(), mStage.getSelectedItemId(), mFailureType.getSelectedItemId(), mFirstDateLong, mLastDateLong, mRecord.getUserToken());
                mRecordDao.insertRecord(record);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateAndTime.set(Calendar.YEAR, year);
                dateAndTime.set(Calendar.MONTH, monthOfYear);
                dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                mFirstDateLong = dateAndTime.getTimeInMillis();
                mFirstDate.setText(DateUtils.format(mFirstDateLong));
            }
        };

        final DatePickerDialog.OnDateSetListener b = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateAndTime.set(Calendar.YEAR, year);
                dateAndTime.set(Calendar.MONTH, monthOfYear);
                dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                mLastDateLong = dateAndTime.getTimeInMillis();
                mLastDate.setText(DateUtils.format(mLastDateLong));
            }
        };

        mFirstDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(UpdateRecordActivity.this, d,
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        mLastDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(UpdateRecordActivity.this, b,
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });
    }
}
