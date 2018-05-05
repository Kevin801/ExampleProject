package com.kevin801.flightproject.airportsearch;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.kevin801.flightoptimizer.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AirportList extends AppCompatActivity {
    private ArrayList<Airport> airportList;
    private AirportListAdapter adapter;
    ArrayList<String> input;
    ListView lvAirports;
    EditText inputSearch;
    Activity activity;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airport_list);
        airportList = new ArrayList<>();
        input = new ArrayList<>();
        activity = this;
        
        init();
        
        lvAirports = (ListView) findViewById(R.id.list_of_airport);
        inputSearch = (EditText) findViewById(R.id.search_box);
        adapter = new AirportListAdapter(this, R.layout.item_layout, airportList);
        lvAirports.setAdapter(adapter);
        
        inputSearch.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            
            }
    
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textlength = s.length();
                ArrayList<Airport> tempArrayList = new ArrayList<>();
                for(Airport c : airportList){
                    if (textlength <= c.getName().length() || textlength <= c.getDescription().length()) {
                        if (c.getName().toLowerCase().contains(s.toString().toLowerCase()) || c.getDescription().toLowerCase().contains(s.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }
                adapter = new AirportListAdapter(activity,  R.layout.item_layout, tempArrayList);
                lvAirports.setAdapter(adapter);
            }
    
            @Override
            public void afterTextChanged(Editable s) {
        
            }
        });
    }
    
    private void init() {
        InputStream csvFile = getResources().openRawResource(R.raw.airports);
        String splitBy = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        String line;
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(csvFile))) {
            br.readLine(); // Skips first line
            while ((line = br.readLine()) != null) {
                String[] file = line.split(splitBy);
                String airportFile = file[0];
                String descriptionFile = file[1];
                
                Airport airport = new Airport(airportFile, descriptionFile);
                airportList.add(airport);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
