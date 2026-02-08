package edu.altu.medapp.SchedulingComponent.model;

import java.time.LocalDateTime;

public class Appointment {
    private int id;
    private int patientId;
    private int doctorId;
    private LocalDateTime time;
    private String status;
    private String type;
    private double fee;

    public Appointment() {}

    public Appointment(int patientId, int doctorId, LocalDateTime time, String type, double fee) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.time = time;
        this.status = "scheduled";
        this.type = type;
        this.fee = fee;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public int getPatientId() {return patientId;}
    public void setPatientId(int patientId) {this.patientId = patientId;}

    public int getDoctorId() {return doctorId;}
    public void setDoctorId(int doctorId) {this.doctorId = doctorId;}

    public LocalDateTime getTime() {return time;}
    public void setTime(LocalDateTime time) {this.time = time;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public double getFee() {return fee;}
    public void setFee(double fee) {this.fee = fee;}
}