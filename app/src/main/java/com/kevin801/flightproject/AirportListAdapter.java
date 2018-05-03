package com.kevin801.flightproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kevin801.flightoptimizer.R;

import java.util.ArrayList;

public class AirportListAdapter extends ArrayAdapter<Airport> {

    private static final String TAG = "AirportListAdapter";
    private Context mContext;
    private int mResource;

    public AirportListAdapter(Context context, int resource, ArrayList<Airport> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = getItem(position).getName();
        String description = getItem(position).getDescription();

        Airport airport = new Airport(name, description);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName = (TextView) convertView.findViewById(R.id.list_airport_name);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.list_airport_description);

        tvName.setText(name);
        tvDescription.setText(description);
        return convertView;
    }
}
