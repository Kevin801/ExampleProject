package com.kevin801.flightproject.airportsearch;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.kevin801.flightoptimizer.R;
import com.kevin801.flightproject.util.DataUtility;

import java.util.ArrayList;

public class AirportList extends AppCompatActivity {
    private ArrayList<Row> airportData;
    private AirportListAdapter adapter;
    private Activity activity;
    private ListView lvAirports;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airport_list);
        
        activity = this;
        airportData = new DataUtility(this).csvToArrayList(R.raw.airports);
        adapter = new AirportListAdapter(this, R.layout.item_layout, airportData);
        
        lvAirports = findViewById(R.id.list_of_airport);
        lvAirports.setAdapter(adapter);
        
        EditText inputSearch = findViewById(R.id.search_box);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textLength = s.length();
                ArrayList<Row> updatedList = new ArrayList<>();
                for (Row c : airportData) {
                    if (textLength <= c.getName().length() || textLength <= c.getDescription().length()) { // user input matches Airport code or description
                        if (c.getName().toLowerCase().contains(s.toString().toLowerCase()) || c.getDescription().toLowerCase().contains(s.toString().toLowerCase())) {
                            updatedList.add(c);
                        }
                    }
                }
                adapter = new AirportListAdapter(activity, R.layout.item_layout, updatedList);
                lvAirports.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
