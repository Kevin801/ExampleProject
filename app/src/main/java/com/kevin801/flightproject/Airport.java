package com.kevin801.flightproject;

public class Airport {
    private String name;
    private String description;

    public Airport(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int compare(Airport a1, Airport a2) {
        return (a1.name.toString().toLowerCase()).compareTo(a2.name.toString().toLowerCase());
    }
}
