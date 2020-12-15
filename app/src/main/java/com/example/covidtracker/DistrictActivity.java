package com.example.covidtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;

public class DistrictActivity extends AppCompatActivity {

    String state;
    ArrayList<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setElevation(0);

        Bundle bundle = getIntent().getExtras();
        int position1 = bundle.getInt("position");
        state = bundle.getString("state");
        String jsonResponse = bundle.getString("response");
        list = new ArrayList<String>();

        MyConnectionHelper connectionHelper = new MyConnectionHelper();
        try {
            list = connectionHelper.parseJsonDistrict(jsonResponse,position1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TextView stateView1 = (TextView) findViewById(R.id.state_view_1);
        stateView1.setText(state);

        ListView listView = (ListView) findViewById(R.id.list_2);
        DistrictAdapter districtAdapter = new DistrictAdapter(this,list);
        listView.setAdapter(districtAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DistrictActivity.this,DetailsActivity.class);
                Bundle extra1 = new Bundle();
                extra1.putInt("statePosition",position1);
                extra1.putInt("districtPosition",position);
                extra1.putString("state",state);
                extra1.putString("district",list.get(position));
                extra1.putString("response",jsonResponse);
                intent.putExtras(extra1);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        }
        return true;
    }
}