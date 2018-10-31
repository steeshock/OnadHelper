package ru.steeshock.protocols.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import ru.steeshock.protocols.utils.UserSettings;


public class ExtraNewRecordActivity extends AppCompatActivity {

    private TextView mProtocolNumber;
    private TextView mActNumber;
    private TextView mDescription;
    private Spinner mStatus;
    private Button btnAdd, btnCancel;

    // Extra fields
    private TextView mFirstDate, mLastDate;
    private Spinner mAuthor;
    private Calendar dateAndTime = Calendar.getInstance();
    private Long mFirstDateLong;
    private Long mLastDateLong;

    private RecordDao recordDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.extra_new_record_activity);

        recordDao = ((AppDelegate)getApplicationContext()).getRecordDatabase().getRecordDao(); //получаем доступ к экземпляру БД

        mProtocolNumber = findViewById(R.id.etProtocolNumber);
        mActNumber = findViewById(R.id.etActNumber);
        mDescription = findViewById(R.id.etDescription);
        mStatus = findViewById(R.id.spinner);
        btnAdd = findViewById(R.id.btn_add);
        btnCancel = findViewById(R.id.btn_cancel);


        mFirstDate = findViewById(R.id.textView);
        mLastDate = findViewById(R.id.textView2);
        mAuthor = findViewById(R.id.spinnerAuthor);

        final DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateAndTime.set(Calendar.YEAR, year);
                dateAndTime.set(Calendar.MONTH, monthOfYear);
                dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                mFirstDateLong = dateAndTime.getTimeInMillis();
                mFirstDate.setText("" + dateAndTime.getTimeInMillis());
            }
        };

        final DatePickerDialog.OnDateSetListener b = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateAndTime.set(Calendar.YEAR, year);
                dateAndTime.set(Calendar.MONTH, monthOfYear);
                dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                mLastDateLong = dateAndTime.getTimeInMillis();
                mLastDate.setText("" + dateAndTime.getTimeInMillis());
            }
        };

        mFirstDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ExtraNewRecordActivity.this, d,
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        mLastDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ExtraNewRecordActivity.this, b,
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Record record = new Record(mProtocolNumber.getText().toString(), mActNumber.getText().toString(), mDescription.getText().toString(), mStatus.getSelectedItem().toString(),  mStatus.getSelectedItemId(), mFirstDateLong, mLastDateLong, mAuthor.getSelectedItem().toString());
                recordDao.insertRecord(record);
                Intent startMainActivity = new Intent(ExtraNewRecordActivity.this, MainActivity.class);
                startActivity (startMainActivity);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startMainActivity = new Intent(ExtraNewRecordActivity.this, MainActivity.class);
                startActivity (startMainActivity);
                finish();
            }
        });
    }
}
