/**
 * IMPORTANT: Make sure you are using the correct package name.
 * This example uses the package name:
 * package com.example.android.justjava
 * If you get an error when copying this code into Android studio, update it to match the package name found
 * in the project's AndroidManifest.xml file.
 **/

package com.example.android.flightoptimizer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This app displays the cheapest path between two airports
 */
public class MainActivity extends Activity {
    private String[] AIRPORTS;
    private HashMap<String, String> airportsMap = new HashMap<>();
    private NetworkGraph airportGraph = null;
    private ArrayAdapter<String> adapter;
    private String criteriaSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputStream airportStream = getResources().openRawResource(R.raw.airports);
        readInCSVAirports(airportStream);



        setUpButton();
        setUpListView();
        setUpSpinners();
        setUpAutoComplete();
    }

    private void setUpButton() {

        InputStream zipFilePath = getResources().openRawResource(R.raw.flight_data_csv);
        try (BufferedInputStream bufferedInput = new BufferedInputStream(zipFilePath)) {
            airportGraph = new NetworkGraph(bufferedInput);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        setContentView(R.layout.activity_main);
        Button buttonBestPath = findViewById(R.id.bestpath_button);
        buttonBestPath.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                InputStream zipFilePath = getResources().openRawResource(R.raw.flight_data_csv);
//                try (BufferedInputStream bufferedInput = new BufferedInputStream(zipFilePath)) {
//                    airportGraph = new NetworkGraph(bufferedInput);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
                EditText originInput = (EditText) findViewById(R.id.user_input_origin);
                String origin = originInput.getText().toString().toUpperCase();

                EditText destInput = (EditText) findViewById(R.id.user_input_destination);
                String destination = destInput.getText().toString().toUpperCase();

                Spinner spinner = (Spinner) findViewById(R.id.criteria_spinner);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String selectedItemText = (String) parent.getItemAtPosition(position);
                        String selectedItemText = (String) parent.getSelectedItem();

                        criteriaSelected = selectedItemText.toUpperCase();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                FlightCriteria criteria = FlightCriteria.PRICE;
                if ("PRICE".equalsIgnoreCase(criteriaSelected)) {
                    criteria = FlightCriteria.PRICE;
                } else if ("DISTANCE".equalsIgnoreCase(criteriaSelected)) {
                    criteria = FlightCriteria.DISTANCE;
                } else if ("CANCELED".equalsIgnoreCase(criteriaSelected)) {
                    criteria = FlightCriteria.CANCELED;
                } else if ("DELAY".equalsIgnoreCase(criteriaSelected)) {
                    criteria = FlightCriteria.DELAY;
                } else if ("TIME".equalsIgnoreCase(criteriaSelected)) {
                    criteria = FlightCriteria.TIME;
                }

                BestPath shortestDistancePath = airportGraph.getBestPath(origin, destination, criteria); // TODO: Add ability to chagne airliner.
                TextView bestPathOutput = findViewById(R.id.bestpath_output);
                bestPathOutput.setText(shortestDistancePath.toString());
            }
        });
    }

    private void setUpListView() {
        ListView airportListView = (ListView) findViewById(R.id.airport_list);

        List<HashMap<String, String>> listAirportMap = new ArrayList<>();

        Iterator it = airportsMap.entrySet().iterator();
        while (it.hasNext()) {
            HashMap<String, String> resultsMap = new HashMap<>();
            Map.Entry pair = (Map.Entry) it.next();
            resultsMap.put("First Line", pair.getKey().toString());
            resultsMap.put("Second Line", pair.getValue().toString());
            listAirportMap.add(resultsMap);
        }

        SimpleAdapter airportAdapter = new SimpleAdapter(this, listAirportMap, R.layout.item_layout,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.Airport_Name, R.id.Airport_description});

        airportListView.setAdapter(airportAdapter);
    }

    private void setUpSpinners() {
        Spinner criteriaSpinner = (Spinner) findViewById(R.id.criteria_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter =
                ArrayAdapter.createFromResource(this, R.array.criteria_array,
                        android.R.layout.simple_spinner_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        criteriaSpinner.setAdapter(spinnerAdapter);

        Spinner carrierSpinner = (Spinner) findViewById(R.id.carrier_spinner);
        ArrayAdapter<CharSequence> carrierAdapter =
                ArrayAdapter.createFromResource(this, R.array.carriers_array,
                        android.R.layout.simple_spinner_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carrierSpinner.setAdapter(carrierAdapter);
    }

    private void setUpAutoComplete() {
        // Setting up autocomplete for origin and destination
        AutoCompleteTextView oriAutoComp = (AutoCompleteTextView) findViewById(R.id.user_input_origin);
        AutoCompleteTextView destAutoComp = (AutoCompleteTextView) findViewById(R.id.user_input_destination);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, AIRPORTS);
        oriAutoComp.setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, AIRPORTS);
        destAutoComp.setAdapter(adapter);
    }

    private void readInCSVAirports(InputStream csvFile) {
        ArrayList<String> list = new ArrayList<String>();
        String splitBy = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        String line = "";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(csvFile))) {

            br.readLine(); // Skips first line
            while ((line = br.readLine()) != null) {
                String[] file = line.split(splitBy);
                String airport = file[0];
                String description = file[1];

                list.add(airport);
                airportsMap.put(airport, description);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        AIRPORTS = list.toArray(new String[list.size()]);
    }

    public void findBestPath(View view) {

    }
}