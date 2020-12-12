package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.AsynchronousChannelGroup;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        URL url = null;
        try {
            url = new URL("https://api.covid19india.org/v2/state_district_wise.json");
        } catch (MalformedURLException e) {
            Log.e(e.getMessage(),"enter a valid url");
        }

        CovidTask covidTask = new CovidTask();
        covidTask.execute(url);

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
            ListView listView = (ListView) findViewById(R.id.list_1);
            CovidAdapter adapter = new CovidAdapter(MainActivity.this,strings);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this,DistrictActivity.class);
                    Bundle extras = new Bundle();
                    extras.putInt("position",position);
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