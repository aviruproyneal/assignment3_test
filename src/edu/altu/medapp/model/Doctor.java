package edu.altu.medapp.model;

public class Doctor {
    private int id;
    private String name;
    private String specialization;
    private boolean available;

    public Doctor() {}

    public Doctor(String name, String specialization, boolean available) {
        this.name = name;
        this.specialization = specialization;
        this.available = available;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialization() { return specialization; }  // Changed from 'getSpecialty'
    public void setSpecialization(String specialization) { this.specialization = specialization; }  // Changed

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}