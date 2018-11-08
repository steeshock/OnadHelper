package ru.steeshock.protocols.ui.Records;

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
import ru.steeshock.protocols.data.database.RecordDao;
import ru.steeshock.protocols.data.model.Record;
import ru.steeshock.protocols.utils.UserSettings;


public class NewRecordActivity extends AppCompatActivity {

    private TextView mProtocolNumber;
    private TextView mActNumber;
    private TextView mDescription;
    private Spinner mStatus;
    private Button btnAdd, btnCancel;

    // Extra fields
    private TextView mFirstDate, mLastDate;
    private Spinner mAuthor;

    private RecordDao recordDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_record_activity);

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

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Record record = new Record(mProtocolNumber.getText().toString(), mActNumber.getText().toString(), mDescription.getText().toString(), mStatus.getSelectedItemId(), System.currentTimeMillis(), UserSettings.USER_TOKEN);
                recordDao.insertRecord(record);
                Intent startMainActivity = new Intent(NewRecordActivity.this, MainActivity.class);
                startActivity (startMainActivity);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startMainActivity = new Intent(NewRecordActivity.this, MainActivity.class);
                startActivity (startMainActivity);
                finish();
            }
        });
    }
}
