package edu.altu.medapp.model;

import edu.altu.medapp.interfaces.IAppointmentType;
import java.time.LocalDateTime;

public class FollowUpAppointment implements IAppointmentType {
    @Override
    public Appointment createAppointment(int patientId, int doctorId, LocalDateTime appointmentTime) {
        Appointment appointment = new Appointment(patientId, doctorId, appointmentTime, "scheduled");
        // Follow-up might link to previous appointment in future
        return appointment;
    }

    @Override
    public String getType() {
        return "Follow-Up";
    }

    @Override
    public double getBaseFee() {
        return 75.0; // Discounted for follow-ups
    }

    @Override
    public int getDurationMinutes() {
        return 20;
    }
}