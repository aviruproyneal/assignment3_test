package edu.altu.medapp.model;

import edu.altu.medapp.interfaces.IAppointmentType;
import java.time.LocalDateTime;

public class OnlineAppointment implements IAppointmentType {
    @Override
    public Appointment createAppointment(int patientId, int doctorId, LocalDateTime appointmentTime) {
        Appointment appointment = new Appointment(patientId, doctorId, appointmentTime, "scheduled");
        // Online appointments might have specific fields in future
        return appointment;
    }

    @Override
    public String getType() {
        return "Online Consultation";
    }

    @Override
    public double getBaseFee() {
        return 50.0; // Lower fee for online appointments
    }

    @Override
    public int getDurationMinutes() {
        return 30;
    }
}