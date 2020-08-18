package com.example.blizzard;

import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.blizzard.util.CheckNetworkUtil;
import com.example.blizzard.workers.DataUpdateWorker;
import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements CheckNetworkUtil.ConnectivityListener{
    private static final String TAG = "MainActivity";
    public static final String WEATHER_UPDATE_CHECKER = "Weather_Update_Checker";
    private CheckNetworkUtil mCheckNetworkUtil;

    @Override
    protected void onResume() {
        super.onResume();
        //register Intentfilter
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        mCheckNetworkUtil = new CheckNetworkUtil();
        registerReceiver(mCheckNetworkUtil, intentFilter);
        //Set listener
        mCheckNetworkUtil.setConnectivityListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startWorker();
    }

    private void startWorker() {
        Log.d(TAG, "startWorker: Worker Manager initializing work");
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest request =
                new PeriodicWorkRequest
                        .Builder(DataUpdateWorker.class, 1, TimeUnit.HOURS)
                        .setConstraints(constraints)
                        .setInitialDelay(15, TimeUnit.SECONDS)
                        .build();

        WorkManager.getInstance(getApplicationContext())
                .enqueueUniquePeriodicWork(WEATHER_UPDATE_CHECKER, ExistingPeriodicWorkPolicy.KEEP, request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isNetworkAvailable) {
        showSnackBar(isNetworkAvailable);
    }

    private void showSnackBar(boolean isConnected) {
        String message;
        int color;

        if (isConnected) {
            message = "You are Online";
            color = Color.WHITE;

            Snackbar snackbar = Snackbar.make(findViewById(R.id.main_layout), message, Snackbar.LENGTH_LONG);
            snackbar.setBackgroundTint(Color.GREEN);
            View view = snackbar.getView();
            TextView textView = view.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(color);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            snackbar.show();
        }else {
            message = "You are Offline";
            color = Color.WHITE;

            Snackbar snackbar = Snackbar.make(findViewById(R.id.main_layout), message, Snackbar.LENGTH_LONG);
            snackbar.setBackgroundTint(Color.LTGRAY);
            View view = snackbar.getView();
            TextView textView = view.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(color);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            snackbar.show();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCheckNetworkUtil != null)
            unregisterReceiver(mCheckNetworkUtil);
    }
}