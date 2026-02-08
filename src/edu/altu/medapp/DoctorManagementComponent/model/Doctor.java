package edu.altu.medapp.DoctorManagementComponent.model;

public class Doctor {
    private int id;
    private String name;
    private String specialization;
    private boolean available;

    public Doctor() {}

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getSpecialization() {return specialization;}
    public void setSpecialization(String specialization) {this.specialization = specialization;}

    public boolean isAvailable() {return available;}
    public void setAvailable(boolean available) {this.available = available;}
}