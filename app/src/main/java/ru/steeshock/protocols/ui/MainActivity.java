package ru.steeshock.protocols.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import ru.steeshock.protocols.AppDelegate;
import ru.steeshock.protocols.R;
import ru.steeshock.protocols.database.RecordDao;
import ru.steeshock.protocols.model.RecordAdapter;
import ru.steeshock.protocols.utils.UserSettings;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private RecordAdapter mRecordAdapter;
    private RecordDao recordDao;
    private UserSettings mUserSettings;
    private FragmentManager mFragmentManager;

    private RecyclerView mRecyclerView;




    private TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recordDao = ((AppDelegate)getApplicationContext()).getRecordDatabase().getRecordDao(); //получаем доступ к экземпляру БД
        mRecordAdapter = new RecordAdapter(recordDao);

        mUserSettings = new UserSettings(getApplicationContext());

        UserSettings.HIDE_RECORDS_FLAG = mUserSettings.mSharedPreferences.getBoolean(UserSettings.HIDE_RECORDS_FLAG_KEY, false);
        UserSettings.USER_TOKEN = mUserSettings.mSharedPreferences.getString(UserSettings.USER_TOKEN_KEY, "noname");

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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mRecordAdapter);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        tv = findViewById(R.id.tv_credentials);
//        tv.setText(R.string.app_name);

        onRefresh(UserSettings.HIDE_RECORDS_FLAG);

    }


    private void onRefresh(boolean hide) {

        if(!hide){
            mRecordAdapter.addRecords(recordDao.getRecords(),true);
            mRecyclerView.scrollToPosition(mRecordAdapter.getItemCount()-1);
            Toast.makeText(this, "Список обновлен", Toast.LENGTH_SHORT).show();
        }
        if(hide){
            mRecordAdapter.addFilteredRecords(recordDao.getRecords(),true);
            //mRecyclerView.scrollToPosition(mRecordAdapter.getItemCount()-1);
            Toast.makeText(this, "Готовые протоколы скрыты", Toast.LENGTH_SHORT).show();
        }
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

        if (id == R.id.nav_main) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_exit) {
            mUserSettings.mSharedPreferences.edit().putBoolean(UserSettings.SAVE_USER_AUTH_KEY, false).apply();
            Intent openAuthActivity = new Intent(MainActivity.this, AuthActivity.class);
            startActivity (openAuthActivity);
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem hide_item = menu.findItem(R.id.action_hide);
        MenuItem show_item = menu.findItem(R.id.action_show);

        if(UserSettings.HIDE_RECORDS_FLAG) {
            show_item.setVisible(true);
            hide_item.setVisible(false);


        }
        if(!UserSettings.HIDE_RECORDS_FLAG) {
            show_item.setVisible(false);
            hide_item.setVisible(true);
        }

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_sort:
                showFilter(FilterFragment.newInstance());
                //Toast.makeText(this, ""+ "Сортировка еще не работает", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_hide:
                UserSettings.HIDE_RECORDS_FLAG = true;
                mUserSettings.mSharedPreferences.edit().putBoolean(UserSettings.HIDE_RECORDS_FLAG_KEY, UserSettings.HIDE_RECORDS_FLAG).apply();
                onRefresh(UserSettings.HIDE_RECORDS_FLAG);
                break;
            case R.id.action_show:
                UserSettings.HIDE_RECORDS_FLAG = false;
                mUserSettings.mSharedPreferences.edit().putBoolean(UserSettings.HIDE_RECORDS_FLAG_KEY, UserSettings.HIDE_RECORDS_FLAG).apply();
                onRefresh(UserSettings.HIDE_RECORDS_FLAG);
                break;
        }


        return super.onOptionsItemSelected(item);
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

    public void backupDataBase () {

        // делаем резервную копию БД и сохраняем на флэшку
        // на реальном устройстве почему-то сохраняет в память утройства

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite())
            {
                String currentDBPath = "//data//ru.steeshock.protocols//databases//record_database";
                String backupDBPath = "backupDataBase";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);
                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Toast.makeText(this, "DONE", Toast.LENGTH_LONG).show();
                }
            } else Toast.makeText(this, "Вставьте SD карту!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {

            Toast.makeText(this, "Error: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

        }

    }

}
