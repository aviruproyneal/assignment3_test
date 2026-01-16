package edu.altu.medapp.model;

import java.time.LocalDate;

public class Patient {
    private int id;
    private String name;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;

    public Patient() {}

    public Patient(String name, String email, String phone, LocalDate dateOfBirth) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    @Override
    public String toString() {
        return "Patient{id=" + id + ", name='" + name + "', email='" + email + "', phone='" + phone + "'}";
    }
}