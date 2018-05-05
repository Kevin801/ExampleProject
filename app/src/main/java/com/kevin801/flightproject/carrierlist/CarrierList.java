package com.kevin801.flightproject.carrierlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.kevin801.flightoptimizer.R;
import com.kevin801.flightproject.airportsearch.Airport;
import com.kevin801.flightproject.airportsearch.AirportListAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CarrierList extends AppCompatActivity {
    ArrayList<Airport> carrierList;
    ListView lvCarriers;
    AirportListAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrier_list);
        carrierList = new ArrayList<>();
        
        init();
        
        lvCarriers = (ListView) findViewById(R.id.list_of_carriers);
        adapter = new AirportListAdapter(this, R.layout.item_layout, carrierList);
        lvCarriers.setAdapter(adapter);
    }
    
    private void init() {
        InputStream csvFile = getResources().openRawResource(R.raw.airlinecarriers);
        String splitBy = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        String line;
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(csvFile))) {
            br.readLine(); // Skips first line
            while ((line = br.readLine()) != null) {
                String[] file = line.split(splitBy);
                String carrierCode = file[0];
                String carrierName = file[1];
                
                Airport airport = new Airport(carrierCode, carrierName);
                carrierList.add(airport);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
