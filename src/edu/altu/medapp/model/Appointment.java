package edu.altu.medapp.model;

import java.time.LocalDateTime;

public class Appointment {
    public static final String STATUS_SCHEDULED = "scheduled";
    public static final String STATUS_CANCELLED = "cancelled";
    public static final String STATUS_COMPLETED = "completed";

    public static final String TYPE_ONLINE = "ONLINE";
    public static final String TYPE_IN_PERSON = "IN_PERSON";
    public static final String TYPE_FOLLOW_UP = "FOLLOW_UP";

    private int id;
    private int patientId;
    private int doctorId;
    private LocalDateTime appointmentTime;
    private String status;

    private String appointmentType;
    private double consultationFee;

    public Appointment() {}

    public Appointment(int patientId, int doctorId, LocalDateTime appointmentTime, String status) {
        this(patientId, doctorId, appointmentTime, status, TYPE_IN_PERSON, 100.0);
    }

    public Appointment(int patientId, int doctorId, LocalDateTime appointmentTime,
                       String status, String appointmentType, double consultationFee) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.appointmentType = appointmentType;
        this.consultationFee = consultationFee;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public LocalDateTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalDateTime appointmentTime) { this.appointmentTime = appointmentTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAppointmentType() { return appointmentType; }
    public void setAppointmentType(String appointmentType) { this.appointmentType = appointmentType; }

    public double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(double consultationFee) { this.consultationFee = consultationFee; }

    @Override
    public String toString() {
        return "Appointment{id=" + id + ", patientId=" + patientId + ", doctorId=" + doctorId +
                ", time=" + appointmentTime + ", status='" + status +
                "', type='" + appointmentType + "', fee=" + consultationFee + "}";
    }
}