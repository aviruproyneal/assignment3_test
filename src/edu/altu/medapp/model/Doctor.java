package edu.altu.medapp.model;

public class Doctor {
    private int id;
    private String name;
    private String specialization;
    private boolean isAvailable;

    public Doctor() {}

    public Doctor(String name, String specialization, boolean isAvailable) {
        this.name = name;
        this.specialization = specialization;
        this.isAvailable = isAvailable;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    @Override
    public String toString() {
        return "Doctor{id=" + id + ", name='" + name + "', specialization='" +
                specialization + "', available=" + isAvailable + "}";
    }
}