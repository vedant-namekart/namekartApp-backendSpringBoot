package com.namekartapp.namekartappapi;

public class Domain {
    private String name;

    // Default constructor
    public Domain() {}

    // Constructor
    public Domain(String name) {
        this.name = name;
    }

    // Getter and Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}