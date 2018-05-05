package com.kevin801.flightproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.kevin801.flightoptimizer.R;
import com.kevin801.flightproject.carrierlist.CarrierList;
import com.kevin801.flightproject.flightoptimizer.BestPath;
import com.kevin801.flightproject.flightoptimizer.FlightCriteria;
import com.kevin801.flightproject.flightoptimizer.NetworkGraph;
import com.kevin801.flightproject.airportsearch.AirportList;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Where the xml and java files make connections
 */
public class MainActivity extends Activity {
    private HashMap<String, String> airportsMap;
    private NetworkGraph airportGraph;
    private String[] AIRPORTS;
    private String origin, destination, carrier;
    private int criteriaSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        airportsMap = new HashMap<>();
        airportGraph = null;
        origin = destination = carrier = "";

        graphInit();
        readInCSVAirports();
        setUpButton();
        setUpSpinners();
        setUpAutoComplete();
    }

     private void readInCSVAirports() {
        InputStream csvFile = getResources().openRawResource(R.raw.airports);

        ArrayList<String> list = new ArrayList<>();
        String splitBy = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        String line = "";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(csvFile))) {
            br.readLine(); // Skips first line
            while ((line = br.readLine()) != null) {
                String[] file = line.split(splitBy);
                String airportFile = file[0];
                String descriptionFile = file[1];

                airportsMap.put(airportFile, descriptionFile);
                list.add(airportFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        AIRPORTS = list.toArray(new String[list.size()]);
    }

    private void setUpButton() {
        setContentView(R.layout.activity_main);
        Button buttonBestPath = findViewById(R.id.bestpath_button);
        buttonBestPath.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // getting data from origin and destination
                EditText originInput = findViewById(R.id.user_input_origin);
                origin = originInput.getText().toString().toUpperCase();

                EditText destInput = findViewById(R.id.user_input_destination);
                destination = destInput.getText().toString().toUpperCase();

                // hide keyboard
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                // getting data from spinner
                Spinner spinner = findViewById(R.id.criteria_spinner);
                criteriaSelected = spinner.getSelectedItemPosition();

                // find criteria selected.
                FlightCriteria criteria;
                switch (criteriaSelected) {
                    case 0:
                        criteria = FlightCriteria.PRICE;
                        break;
                    case 1:
                        criteria = FlightCriteria.DELAY;
                        break;
                    case 2:
                        criteria = FlightCriteria.DISTANCE;
                        break;
                    case 3:
                        criteria = FlightCriteria.CANCELED;
                        break;
                    case 4:
                        criteria = FlightCriteria.TIME;
                        break;
                    default:
                        criteria = FlightCriteria.PRICE;
                }

                spinner = findViewById(R.id.carrier_spinner);
                if (spinner.getSelectedItemPosition() == 0) {
                    carrier = "";
                } else {
                    carrier = spinner.getSelectedItem().toString();
                }

                BestPath cheapestPath = airportGraph.getBestPath(origin, destination, criteria, carrier.toUpperCase());
                TextView bestPathOutput = findViewById(R.id.bestpath_output);
                bestPathOutput.setText(cheapestPath.toString());
            }
        });
    }

    private void graphInit() {
        InputStream filePath = getResources().openRawResource(R.raw.flight_data);
    
        try (BufferedInputStream bufferedInput = new BufferedInputStream(filePath)) {
            airportGraph = new NetworkGraph(bufferedInput);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

     private void setUpSpinners() {
        // setup criteria spinner
        Spinner criteriaSpinner = findViewById(R.id.criteria_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter =
                ArrayAdapter.createFromResource(this, R.array.criteria_array,
                        android.R.layout.simple_spinner_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        criteriaSpinner.setAdapter(spinnerAdapter);

        // setup carrier spinner
        Spinner carrierSpinner = findViewById(R.id.carrier_spinner);
        ArrayAdapter<CharSequence> carrierAdapter =
                ArrayAdapter.createFromResource(this, R.array.carriers_array,
                        android.R.layout.simple_spinner_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carrierSpinner.setAdapter(carrierAdapter);
    }

    private void setUpAutoComplete() {
        // Setting up autocomplete for origin and destination
        AutoCompleteTextView oriAutoComp = findViewById(R.id.user_input_origin);
        AutoCompleteTextView destAutoComp = findViewById(R.id.user_input_destination);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, AIRPORTS);
        oriAutoComp.setAdapter(adapter);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, AIRPORTS);
        destAutoComp.setAdapter(adapter);
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