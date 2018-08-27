package ru.steeshock.protocols.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Toast;

import ru.steeshock.protocols.AppDelegate;
import ru.steeshock.protocols.R;
import ru.steeshock.protocols.database.RecordDao;
import ru.steeshock.protocols.model.RecordAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static boolean HIDE_RECORDS_FLAG = false;
    public static String USER_TOKEN = "USER_TOKEN";

    private FloatingActionButton mFloatingActionButton;
    private RecordAdapter mRecordAdapter;
    private RecordDao recordDao;

    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recordDao = ((AppDelegate)getApplicationContext()).getRecordDatabase().getRecordDao(); //получаем доступ к экземпляру БД
        mRecordAdapter = new RecordAdapter(recordDao);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mFloatingActionButton = findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
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

        registerForContextMenu(mRecyclerView);

        onRefresh(HIDE_RECORDS_FLAG);

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

        if(HIDE_RECORDS_FLAG) {
            show_item.setVisible(true);
            hide_item.setVisible(false);

        }
        if(!HIDE_RECORDS_FLAG) {
            show_item.setVisible(false);
            hide_item.setVisible(true);
        }

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_sort:
                Toast.makeText(this, ""+ "Сортировка еще не работает", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_hide:
                HIDE_RECORDS_FLAG = true;
                onRefresh(HIDE_RECORDS_FLAG);
                break;
            case R.id.action_show:
                HIDE_RECORDS_FLAG = false;
                onRefresh(HIDE_RECORDS_FLAG);
                break;
        }


        return super.onOptionsItemSelected(item);
    }

}
