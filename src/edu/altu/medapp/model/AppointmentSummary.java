package edu.altu.medapp.model;

import java.time.LocalDateTime;

public class AppointmentSummary {
    private final int appointmentId;
    private final String patientName;
    private final String doctorName;
    private final LocalDateTime appointmentTime;
    private final String status;
    private final String summaryNotes;
    private final double consultationFee;

    private AppointmentSummary(Builder builder) {
        this.appointmentId = builder.appointmentId;
        this.patientName = builder.patientName;
        this.doctorName = builder.doctorName;
        this.appointmentTime = builder.appointmentTime;
        this.status = builder.status;
        this.summaryNotes = builder.summaryNotes;
        this.consultationFee = builder.consultationFee;
    }

    public int getAppointmentId() { return appointmentId; }
    public String getPatientName() { return patientName; }
    public String getDoctorName() { return doctorName; }
    public LocalDateTime getAppointmentTime() { return appointmentTime; }
    public String getStatus() { return status; }
    public String getSummaryNotes() { return summaryNotes; }
    public double getConsultationFee() { return consultationFee; }

    @Override
    public String toString() {
        return String.format(
                "Appointment #%d: %s with Dr. %s at %s | Status: %s | Fee: $%.2f | Notes: %s",
                appointmentId, patientName, doctorName, appointmentTime, status,
                consultationFee, summaryNotes
        );
    }

    public static class Builder {
        private int appointmentId;
        private String patientName;
        private String doctorName;
        private LocalDateTime appointmentTime;
        private String status;
        private String summaryNotes = "";
        private double consultationFee = 0.0;

        public Builder(int appointmentId, String patientName, String doctorName,
                       LocalDateTime appointmentTime, String status) {
            this.appointmentId = appointmentId;
            this.patientName = patientName;
            this.doctorName = doctorName;
            this.appointmentTime = appointmentTime;
            this.status = status;
        }

        public Builder summaryNotes(String summaryNotes) {
            this.summaryNotes = summaryNotes;
            return this;
        }

        public Builder consultationFee(double consultationFee) {
            this.consultationFee = consultationFee;
            return this;
        }

        public AppointmentSummary build() {
            return new AppointmentSummary(this);
        }
    }
}