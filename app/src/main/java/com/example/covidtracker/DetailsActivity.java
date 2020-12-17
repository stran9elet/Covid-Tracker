package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.BulletSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import org.json.JSONException;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setElevation(0);


        Bundle b = getIntent().getExtras();
        int statePosition = b.getInt("statePosition");
        int districtPosition = b.getInt("districtPosition");
        String state = b.getString("state");
        String district = b.getString("district");
        String jsonResponse = b.getString("response");

        TextView confirmed = (TextView) findViewById(R.id.confirmed);
        TextView active = (TextView) findViewById(R.id.active);
        TextView recovered = (TextView) findViewById(R.id.recovered);
        TextView deceased = (TextView) findViewById(R.id.deceased);

        MyConnectionHelper connectionHelper = new MyConnectionHelper();
        String[] arr = new String[4];

        try {
            arr = connectionHelper.parseJsonDistrictDetails(jsonResponse,statePosition,districtPosition);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView stateTextView = (TextView) findViewById(R.id.state_view_2);
        TextView cityTextView = (TextView) findViewById(R.id.city_view);

        stateTextView.setText(state);
        cityTextView.setText(district);

        confirmed.setText("Confirmed: "+arr[0]);
        active.setText("Active: "+arr[1]);
        recovered.setText("Recovered: "+arr[2]);
        deceased.setText("Deceased: "+arr[3]);

        String moreInfoString;
        SpannableString moreInfoSpannable;
        if(Integer.parseInt(arr[1])>500){
            moreInfoString = "Whoa! A lot of active cases are present here. Looks like you need to follow a few precautions to keep yourself and your family safe-\n1.Avoid going out- Literally, the best thing you can do is don't go out unless absolute necessity.\n2. Maintain at least 1 metre distance from others.\n3. Avoid going to crowded areas.\n4. Wear a mask.\n";
            moreInfoSpannable = new SpannableString(moreInfoString);
            moreInfoSpannable.setSpan(new ForegroundColorSpan(Color.RED),132,moreInfoSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            moreInfoSpannable.setSpan(new StyleSpan(Typeface.BOLD),132,moreInfoSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else if(Integer.parseInt(arr[1])<=500 && Integer.parseInt(arr[1])>=100){
            moreInfoString ="Yikes! " +arr[1]+" active cases, but the situation here is still not so bad! You might wanna take a few precautions to prevent the further spread of covid, and act as a responsible citizen- \n1.Avoid going out- Literally, the best thing you can do is don't go out unless absolute necessity.\n2. Maintain at least 1 metre distance from others.\n3. Avoid going to crowded areas.\n4. Wear a mask.\n";
            moreInfoSpannable = new SpannableString(moreInfoString);
            moreInfoSpannable.setSpan(new ForegroundColorSpan(Color.RED),181,moreInfoSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            moreInfoSpannable.setSpan(new StyleSpan(Typeface.BOLD),181,moreInfoSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else if(Integer.parseInt(arr[1])<=100 && Integer.parseInt(arr[1])>0){
            int x = 100;int i = 0;
            if(Integer.parseInt(arr[1])<50  && Integer.parseInt(arr[1])>=20){
                x = 50;i = 1;
            }else if(Integer.parseInt(arr[1])<20 && Integer.parseInt(arr[1])>10){
                x = 20;i = 1;
            }else if(Integer.parseInt(arr[1])<10 && Integer.parseInt(arr[1])>0){
                x = 10;i = 1;
            }
            moreInfoString = "Wowee! This place doesn't even has "+ x +" active cases at present. We can defeat covid if we take some simple precautions to prevent the further spread- \n1.Avoid going out- Literally, the best thing you can do is don't go out unless absolute necessity.\n2. Maintain at least 1 metre distance from others.\n3. Avoid going to crowded areas.\n4. Wear a mask.\n";
            moreInfoSpannable = new SpannableString(moreInfoString);
            moreInfoSpannable.setSpan(new ForegroundColorSpan(Color.RED),149-i,moreInfoSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            moreInfoSpannable.setSpan(new StyleSpan(Typeface.BOLD),149-i,moreInfoSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else{
            moreInfoString = "Awesome! This place has 0 active cases right now. But still, it is advised to take some precautionary measures so that covid doesn't enter this place- \n1.Avoid going out- Literally, the best thing you can do is don't go out unless absolute necessity.\n2. Maintain at least 1 metre distance from others.\n3. Avoid going to crowded areas.\n4. Wear a mask.\n";
            moreInfoSpannable = new SpannableString(moreInfoString);
            moreInfoSpannable.setSpan(new ForegroundColorSpan(Color.RED),150,moreInfoSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            moreInfoSpannable.setSpan(new StyleSpan(Typeface.BOLD),150,moreInfoSpannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }


        //to set up the link in the more_info view
        String hyperlink = "For more information, visit the website <a href=https://www.who.int/emergencies/diseases/novel-coronavirus-2019/advice-for-public>World Health Organisation</a>.";
        TextView moreInfoView = (TextView) findViewById(R.id.more_info);
        moreInfoView.setLinksClickable(true);
        moreInfoView.setMovementMethod(LinkMovementMethod.getInstance());
        moreInfoView.setText(moreInfoSpannable);
        moreInfoView.append(Html.fromHtml(hyperlink));

    }
}