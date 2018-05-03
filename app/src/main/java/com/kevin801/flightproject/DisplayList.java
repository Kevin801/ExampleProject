package com.kevin801.flightproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.kevin801.flightoptimizer.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class DisplayList extends AppCompatActivity {
    private HashMap<String, String> airportsMap;
    private String[] AIRPORTS;
    private ArrayList<Airport> airportList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list);
        airportList = new ArrayList();
        airportsMap = new HashMap<>();

        InputStream inputStream = getResources().openRawResource(R.raw.airports);
        readInCSVAirports(inputStream);

        ListView mListView = (ListView) findViewById(R.id.list_of_airport);
        AirportListAdapter adapter = new AirportListAdapter(this, R.layout.item_layout, airportList);
        mListView.setAdapter(adapter);
    }


    private void readInCSVAirports(InputStream csvFile) {
        ArrayList<String> list = new ArrayList<>();
        String splitBy = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        String line = "";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(csvFile))) {
            br.readLine(); // Skips first line
            while ((line = br.readLine()) != null) {
                String[] file = line.split(splitBy);
                String airportFile = file[0];
                String descriptionFile = file[1];

                Airport airport = new Airport(airportFile, descriptionFile);
                airportList.add(airport);
//                airportsMap.put(airportFile, descriptionFile);
                list.add(airportFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        AIRPORTS = list.toArray(new String[list.size()]);
    }
}
