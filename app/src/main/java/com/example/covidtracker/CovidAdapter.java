package com.example.covidtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CovidAdapter extends ArrayAdapter<String> {
    public CovidAdapter(@NonNull Context context, ArrayList<String> list) {
        super(context, 0,list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(convertView==null)
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.list_item_1,parent,false);

        TextView textView = (TextView) listItem.findViewById(R.id.state_text_view);
        String state = getItem(position);
        textView.setText(state);
        return listItem;
    }


}
