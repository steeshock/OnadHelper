package ru.steeshock.protocols.ui.Records;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import ru.steeshock.protocols.AppDelegate;
import ru.steeshock.protocols.R;
import ru.steeshock.protocols.common.IFilterFields;
import ru.steeshock.protocols.data.database.DBHelper;
import ru.steeshock.protocols.data.database.RecordDao;
import ru.steeshock.protocols.data.model.Record;
import ru.steeshock.protocols.data.model.RecordAdapter;
import ru.steeshock.protocols.ui.AuthActivity;
import ru.steeshock.protocols.ui.Charts.ListViewMultiChartActivity;
import ru.steeshock.protocols.ui.FilterFragment;
import ru.steeshock.protocols.utils.DatabaseServices.BackupDatabaseAsync;
import ru.steeshock.protocols.utils.UserSettings;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IFilterFields {


    private static final String TAG = "MyLog";
    private static final String LIST_STATE_KEY = "LIST_STATE_KEY";

    private RecordAdapter mRecordAdapter;
    private RecordDao recordDao;
    private UserSettings mUserSettings;
    private FragmentManager mFragmentManager;
    private RecyclerView mRecyclerView;
    private TextView tvAllRecords, tvMyRecords;
    private TextView tvProtocols, tvDescription, tvStatus;
    private LinearLayoutManager mLayoutLinearManager;
    private BackupDatabaseAsync backupDatabaseAsync;

    // создаем объект для создания и управления версиями БД через обычный SQL,
    // потому что с помощью Room не удается сделать бэкап БД в файл
    DBHelper dbHelper = new DBHelper (this);
    private Parcelable mListState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recordDao = ((AppDelegate)getApplicationContext()).getRecordDatabase().getRecordDao(); //получаем доступ к экземпляру БД
        mRecordAdapter = new RecordAdapter(recordDao);

        mUserSettings = new UserSettings(getApplicationContext());

        backupDatabaseAsync = new BackupDatabaseAsync(this, recordDao);

        UserSettings.HIDE_RECORDS_FLAG = mUserSettings.mSharedPreferences.getBoolean(UserSettings.HIDE_RECORDS_FLAG_KEY, false);
        UserSettings.HIDE_AUTHOR_FLAG = mUserSettings.mSharedPreferences.getBoolean(UserSettings.HIDE_AUTHOR_FLAG_KEY, false);
        UserSettings.USER_TOKEN = mUserSettings.mSharedPreferences.getString(UserSettings.USER_TOKEN_KEY, "noname");

        switch (UserSettings.USER_TOKEN){

            case "onad015": UserSettings.USER_CREDENTIALS = UserSettings.credentials[0];break;
            case "onad019": UserSettings.USER_CREDENTIALS = UserSettings.credentials[1];break;
            case "onad017": UserSettings.USER_CREDENTIALS = UserSettings.credentials[2];break;
        }

        // Начало Сортировка полей при нажатии на TextView

        tvProtocols = findViewById(R.id.tvProtocolMainPage);
        tvDescription = findViewById(R.id.tvDescMainPage);
        tvStatus = findViewById(R.id.tvStatusMainPage);

        tvProtocols.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserSettings.SORT_ORDER = !UserSettings.SORT_ORDER;
                mRecordAdapter.sortRecordsByProtocolNumber(UserSettings.SORT_ORDER);
            }
        });

        tvDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserSettings.SORT_ORDER = !UserSettings.SORT_ORDER;
                mRecordAdapter.sortRecordsByDescription(UserSettings.SORT_ORDER);
            }
        });

        tvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserSettings.SORT_ORDER = !UserSettings.SORT_ORDER;
                mRecordAdapter.sortRecordsByStatus(UserSettings.SORT_ORDER);
            }
        });

        //Конец Сортировка полей

        mFragmentManager = getSupportFragmentManager();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent startNewRecordActivity = new Intent(MainActivity.this, NewRecordActivity.class);
                startActivity (startNewRecordActivity);
                finish();
            }
        });

        mRecyclerView = findViewById(R.id.recycler);
        mLayoutLinearManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutLinearManager);
        mRecyclerView.setAdapter(mRecordAdapter);

        tvAllRecords = findViewById(R.id.tv_all_protocols);
        tvMyRecords = findViewById(R.id.tv_my_protocols);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerViewCredentials = navigationView.getHeaderView(0);
        TextView navCredentials = headerViewCredentials.findViewById(R.id.tv_credentials);
        TextView navUsername = headerViewCredentials.findViewById(R.id.tv_username);

        navCredentials.setText(UserSettings.USER_CREDENTIALS);
        navUsername.setText(UserSettings.USER_TOKEN);

        onRefresh(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        tvAllRecords.setText("" + recordDao.getRecords().size());
        tvMyRecords.setText("" + recordDao.getRecordsByUsername(UserSettings.USER_TOKEN).size());

        onRefresh(false);

        if (mListState != null) {
            mLayoutLinearManager.onRestoreInstanceState(mListState);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save list state
        mListState = mLayoutLinearManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Retrieve list state and list/item positions
        if(savedInstanceState != null)
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        switch (id){
            case R.id.nav_main: drawer.closeDrawer(GravityCompat.START); break;

            case R.id.nav_exit: {
                mUserSettings.mSharedPreferences.edit().putBoolean(UserSettings.SAVE_USER_AUTH_KEY, false).apply();
                Intent openAuthActivity = new Intent(MainActivity.this, AuthActivity.class);
                startActivity (openAuthActivity);
                finish();
            }   break;

            case R.id.nav_stats: {
                Intent openStatsActivity = new Intent(MainActivity.this, ListViewMultiChartActivity.class);
                startActivity (openStatsActivity);
            }   break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_sort:
                showFilter(FilterFragment.newInstance());
                break;
            case R.id.action_backup:
                backupDatabaseAsync.execute();
                break;
            case R.id.action_fillDB:
                fillDataBaseFromBackupFile();
                break;
            case R.id.action_extraNewRecords:
                showExtraNewRecords();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showExtraNewRecords() {

        Intent showExtraNewRecords = new Intent(MainActivity.this, ExtraNewRecordActivity.class);
        startActivity (showExtraNewRecords);
        finish();
    }

    private void showFilter(Fragment fragment) {

        String fragmentName = fragment.getClass().getName();

        if (!(mFragmentManager.getBackStackEntryCount() == 1)){
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(fragmentName)
                    .commit();
        }
    }

    private void fillDataBaseFromBackupFile() {

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.query("RecordsTable", null, null, null, null, null, null);

        c.moveToFirst();

        String protocolNumber;
        String actNumber;
        String description;
        Long statusNum;
        Long firstDate;
        Long lastDate;
        Long stage;
        Long failureType;
        String userToken;

        for (int i = 0; i < c.getCount(); i++){
            protocolNumber = c.getString(c.getColumnIndex("protocol_number"));
            actNumber = c.getString(c.getColumnIndex("act_number"));
            description = c.getString(c.getColumnIndex("description"));
            statusNum = c.getLong(c.getColumnIndex("status_num"));
            firstDate = c.getLong(c.getColumnIndex("first_date"));
            lastDate = c.getLong(c.getColumnIndex("last_date"));
            userToken = c.getString(c.getColumnIndex("user_token"));
            stage = c.getLong(c.getColumnIndex("stage"));
            failureType = c.getLong(c.getColumnIndex("failure_type"));
            Log.d(TAG, "field: " + firstDate);
            recordDao.insertRecord(new Record(protocolNumber, actNumber, description, statusNum, firstDate, lastDate, stage, failureType, userToken));
            c.moveToNext();
        }

        c.close();

    }

    @Override
    public void onRefresh(boolean isScrollListToEnd) {

        // Сохраняем значения переключателей в файл настроек
        mUserSettings.mSharedPreferences.edit().putBoolean(UserSettings.HIDE_RECORDS_FLAG_KEY, UserSettings.HIDE_RECORDS_FLAG).apply();
        mUserSettings.mSharedPreferences.edit().putBoolean(UserSettings.HIDE_AUTHOR_FLAG_KEY, UserSettings.HIDE_AUTHOR_FLAG).apply();

        mRecordAdapter.addRecords(recordDao.getRecords(),true);
        if (isScrollListToEnd)
            mRecyclerView.scrollToPosition(mRecordAdapter.getItemCount()-1);

    }
}
