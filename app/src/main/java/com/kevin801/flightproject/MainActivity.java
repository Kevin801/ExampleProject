package com.kevin801.flightproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.kevin801.flightoptimizer.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Where the xml and java files make connections
 */
public class MainActivity extends Activity {
    private String[] AIRPORTS;
    public HashMap<String, String> airportsMap = new HashMap<>();
    private NetworkGraph airportGraph = null;
    private int criteriaSelected;
    private String origin, destination, carrier = "";
    private boolean graphInitDidntRun = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputStream inputStream = getResources().openRawResource(R.raw.airports);
        readInCSVAirports(inputStream);
        setUpButton();
        setUpSpinners();
        setUpAutoComplete();
    }

    private void graphInit() {
        InputStream filePath = getResources().openRawResource(R.raw.flight_data_csv);
        try (BufferedInputStream bufferedInput = new BufferedInputStream(filePath)) {
            airportGraph = new NetworkGraph(bufferedInput);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        graphInitDidntRun = false;
    }

    private void setUpButton() {
        setContentView(R.layout.activity_main);
        Button buttonBestPath = findViewById(R.id.bestpath_button);
        buttonBestPath.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (graphInitDidntRun) {
                    graphInit();
                }
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

    public void displayBestPath(View view) {
        // TODO put button info here.
    }

    public void displayList(View view) {
        Intent startAirportListView = new Intent(this, DisplayList.class);
        startActivity(startAirportListView);
    }

    public HashMap<String, String> getAirportsMap() {
        return airportsMap;
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

                airportsMap.put(airportFile, descriptionFile);
                list.add(airportFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        AIRPORTS = list.toArray(new String[list.size()]);
    }
}