package edu.altu.medapp.model;

import edu.altu.medapp.interfaces.IAppointmentType;
import java.time.LocalDateTime;

public class InPersonAppointment implements IAppointmentType {
    @Override
    public Appointment createAppointment(int patientId, int doctorId, LocalDateTime appointmentTime) {
        Appointment appointment = new Appointment(patientId, doctorId, appointmentTime, "scheduled");
        // In-person might require physical room assignment in future
        return appointment;
    }

    @Override
    public String getType() {
        return "In-Person Visit";
    }

    @Override
    public double getBaseFee() {
        return 100.0; // Standard fee for in-person
    }

    @Override
    public int getDurationMinutes() {
        return 45;
    }
}