package com.kevin801.flightproject.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.kevin801.flightoptimizer.R;
import com.kevin801.flightproject.flightoptimizer.BestPath;
import com.kevin801.flightproject.flightoptimizer.FlightCriteria;
import com.kevin801.flightproject.flightoptimizer.NetworkGraph;

public class BestPathButtonClicked implements View.OnClickListener{
    private Activity activity;
    private NetworkGraph airportGraph;
    
    
    public BestPathButtonClicked(Activity activity, NetworkGraph airportGraph) {
        this.activity = activity;
        this.airportGraph = airportGraph;
    }
    
    @Override
    public void onClick(View v) {
        String origin = "", destination = "", carrier = "";
        EditText input;
        int criteriaSelected = 0;
    
        // hide keyboard
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    
        // getting data from origin and destination
        input = activity.findViewById(R.id.user_input_origin);
        origin = input.getText().toString().toUpperCase();
    
        input = activity.findViewById(R.id.user_input_destination);
        destination = input.getText().toString().toUpperCase();
    
        // getting data from spinner
        Spinner spinner = activity.findViewById(R.id.criteria_spinner);
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
    
        // find carrier selected
        spinner = activity.findViewById(R.id.carrier_spinner);
        if (spinner.getSelectedItemPosition() == 0) {
            carrier = "";
        } else {
            carrier = spinner.getSelectedItem().toString();
        }
        
        BestPath cheapestPath = airportGraph.getBestPath(origin, destination, criteria, carrier.toUpperCase());
        TextView bestPathOutput = activity.findViewById(R.id.bestpath_output);
        bestPathOutput.setText(cheapestPath.toString());
    }
}
