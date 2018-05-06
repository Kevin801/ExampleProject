package com.kevin801.flightproject.util;

import android.app.Activity;

import com.kevin801.flightproject.airportsearch.Row;
import com.kevin801.flightproject.flightoptimizer.NetworkGraph;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DataUtility extends Activity {
    Activity activity;
    
    public DataUtility(Activity activity) {
        this.activity = activity;
    }
    
    /**
     * Converts the Resource path of a CSV file to an ArrayList of type Row.
     * @param filePath The Resource path.
     * @return An ArrayList of Type Row.
     */
    public ArrayList<Row> csvToArrayList(int filePath) {
        ArrayList<Row> returnData = new ArrayList<>();
        InputStream csvFile = activity.getResources().openRawResource(filePath);
        
        String splitBy = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        String line;
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(csvFile))) {
            br.readLine(); // Skips first line
            while ((line = br.readLine()) != null) {
                String[] file = line.split(splitBy);
                String colZero = file[0];
                String colOne = file[1];
                
                Row row = new Row(colZero, colOne);
                returnData.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnData;
    }
    
    /**
     * Converts the Resource path of a CSV file to an Array of type String.
     * @param filePath the Resource path of the CSV.
     * @return A String array of type String.
     */
    public String[] csvToStringArray(int filePath) {
        
        String[] returnData;
        ArrayList<String> data = new ArrayList<>();
        InputStream csvFile = activity.getResources().openRawResource(filePath);
        
        String splitBy = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        String line;
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(csvFile))) {
            br.readLine(); // Skips first line
            while ((line = br.readLine()) != null) {
                String[] file = line.split(splitBy);
                String colZero = file[0];
   
                data.add(colZero);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        returnData = data.toArray(new String[data.size()]);
        return returnData;
    }
    
    /**
     * Creates the Airport graph used for determining the best path. This is a timely operation, and
     * should be used sparingly.
     * @return a Network graph that has been initialized with the given flight Data
     */
    public NetworkGraph airportGraphInit(int filePath) {
        InputStream inputStream = activity.getResources().openRawResource(filePath);
        NetworkGraph airportGraph;
        
        try (BufferedInputStream bufferedInput = new BufferedInputStream(inputStream)) {
            airportGraph = new NetworkGraph(bufferedInput);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return airportGraph;
    }
}
