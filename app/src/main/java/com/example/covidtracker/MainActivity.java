package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.AsynchronousChannelGroup;
import java.util.ArrayList;

import hotchemi.android.rate.AppRate;

public class MainActivity extends AppCompatActivity {

    private LinearLayout emptyLayout;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        emptyLayout = (LinearLayout) findViewById(R.id.empty_layout);
        emptyLayout.setVisibility(View.INVISIBLE);

        AppRate.with(this)
        .setInstallDays(0)
        .setLaunchTimes(4)
        .setRemindInterval(2)
        .monitor();
        AppRate.showRateDialogIfMeetsConditions(this);
//        AppRate.with(this).showRateDialog(this);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        URL url = null;
        try {
            url = new URL("https://api.covid19india.org/v2/state_district_wise.json");
        } catch (MalformedURLException e) {
            Log.e(e.getMessage(),"enter a valid url");
        }

        if(networkInfo!=null && networkInfo.isConnected()) {
            CovidTask covidTask = new CovidTask();
            covidTask.execute(url);
        }else{
            progressBar.setVisibility(View.GONE);
            TextView textView = (TextView) findViewById(R.id.state_selection);
            textView.setVisibility(View.INVISIBLE);
            emptyLayout.setVisibility(View.VISIBLE);
        }

    }

    public class CovidTask extends AsyncTask<URL,Void, ArrayList<String>>{

        String jsonResponse = null;
        @Override
        protected ArrayList<String> doInBackground(URL... urls) {
            ArrayList<String> arr = new ArrayList<String>();
            MyConnectionHelper connectionHelper = new MyConnectionHelper();
            try {
                jsonResponse = connectionHelper.makeHttpRequest(urls[0]);
                arr = connectionHelper.parseJsonState(jsonResponse);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return arr;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            progressBar.setVisibility(View.GONE);
            ListView listView = (ListView) findViewById(R.id.list_1);
            CovidAdapter adapter = new CovidAdapter(MainActivity.this,strings);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this,DistrictActivity.class);
                    Bundle extras = new Bundle();
                    extras.putInt("position",position);
                    extras.putString("state",strings.get(position));
                    extras.putString("response",jsonResponse);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}