package com.example.covidtracker;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public final class MyConnectionHelper{

    public String makeHttpRequest(URL url) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
        int responseCode = urlConnection.getResponseCode();

        if(responseCode==200)
        {
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String s;
            while((s=bufferedReader.readLine())!=null)
            {
                stringBuilder.append(s);
            }
        }
        return stringBuilder.toString();
    }

    public ArrayList<String> parseJsonState(String jsonResponse) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonResponse);
        ArrayList<String> jsonStateList = new ArrayList<String>();
        for(int i=1;i<jsonArray.length();i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String s = jsonObject.getString("state");
            jsonStateList.add(s);
        }
        return jsonStateList;
    }

    public ArrayList<String> parseJsonDistrict(String jsonResponse,int statePosition) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonResponse);
        ArrayList<String> jsonDistrictList = new ArrayList<String>();
            JSONObject jsonObject = jsonArray.getJSONObject(statePosition+1);
            JSONArray jsonArray1 = jsonObject.getJSONArray("districtData");
            for(int j=0;j<jsonArray1.length();j++)
            {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                String s1 = jsonObject1.getString("district");
                if(s1.equals("Unknown"))
                    jsonDistrictList.add("Other Regions");
                else
                    jsonDistrictList.add(s1);
            }
        return jsonDistrictList;
    }

    public String[] parseJsonDistrictDetails(String jsonResponse,int statePosition, int districtPosition) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonResponse);
        String[] arr = new String[4];
        JSONObject jsonObject = jsonArray.getJSONObject(statePosition+1);
        JSONArray jsonArray1 = jsonObject.getJSONArray("districtData");

            JSONObject jsonObject1 = jsonArray1.getJSONObject(districtPosition);
            arr[0] = jsonObject1.getString("confirmed");
            arr[1] = jsonObject1.getString("active");
            arr[2] = jsonObject1.getString("recovered");
            arr[3] = jsonObject1.getString("deceased");

        return arr;
    }

}
