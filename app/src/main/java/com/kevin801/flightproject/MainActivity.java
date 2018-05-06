package com.kevin801.flightproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;

import com.kevin801.flightoptimizer.R;
import com.kevin801.flightproject.carrierlist.CarrierList;
import com.kevin801.flightproject.flightoptimizer.NetworkGraph;
import com.kevin801.flightproject.airportsearch.AirportList;
import com.kevin801.flightproject.util.BestPathButtonClicked;
import com.kevin801.flightproject.util.DataUtility;

/**
 * Where the xml and java files make connections
 */
public class MainActivity extends Activity {
    private NetworkGraph airportGraph;
    private String[] AIRPORTS;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        AIRPORTS = new DataUtility(this).csvToStringArray(R.raw.airports);
        airportGraph = new DataUtility(this).airportGraphInit(R.raw.flight_data);
    
        setUpButton();
        setUpSpinners();
        setUpAutoComplete();
    }
    
    private void setUpButton() {
        setContentView(R.layout.activity_main);
        Button buttonBestPath = findViewById(R.id.bestpath_button);
        buttonBestPath.setOnClickListener(new BestPathButtonClicked(this, airportGraph));
    }
    
    private void setUpSpinners() {
        // setup criteria spinner
        Spinner spinner = findViewById(R.id.criteria_spinner);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.criteria_array,
                        android.R.layout.simple_spinner_item);
        
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        
        // setup carrier spinner
        spinner = findViewById(R.id.carrier_spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.carriers_array,
                android.R.layout.simple_spinner_item);
        
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    
    private void setUpAutoComplete() {
        // Setting up autocomplete for origin
        AutoCompleteTextView autoComp = findViewById(R.id.user_input_origin);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, AIRPORTS);
        autoComp.setAdapter(adapter);
    
        // Setting up autocomplete for destination
        autoComp = findViewById(R.id.user_input_destination);
        autoComp.setAdapter(adapter);
    }
    
    public void displayAirportSearch(View view) {
        Intent startAirportListView = new Intent(this, AirportList.class);
        startActivity(startAirportListView);
    }
    
    public void displayCarrierList(View view) {
        Intent startCarrierListView = new Intent(this, CarrierList.class);
        startActivity(startCarrierListView);
    }
}