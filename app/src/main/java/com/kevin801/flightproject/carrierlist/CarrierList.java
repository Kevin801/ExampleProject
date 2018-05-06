package com.kevin801.flightproject.carrierlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.kevin801.flightoptimizer.R;
import com.kevin801.flightproject.airportsearch.Row;
import com.kevin801.flightproject.airportsearch.AirportListAdapter;
import com.kevin801.flightproject.util.DataUtility;

import java.util.ArrayList;

public class CarrierList extends AppCompatActivity {
    ArrayList<Row> carrierData;
    AirportListAdapter adapter;
    ListView lvCarriers;
    DataUtility utility;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrier_list);
        utility = new DataUtility(this);
        carrierData = utility.csvToArrayList(R.raw.airlinecarriers);
        
        lvCarriers = (ListView) findViewById(R.id.list_of_carriers);
        adapter = new AirportListAdapter(this, R.layout.item_layout, carrierData);
        lvCarriers.setAdapter(adapter);
    }
}
