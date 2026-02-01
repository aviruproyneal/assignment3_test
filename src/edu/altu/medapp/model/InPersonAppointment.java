package edu.altu.medapp.model;

import edu.altu.medapp.interfaces.IAppointmentType;
import java.time.LocalDateTime;

public class InPersonAppointment implements IAppointmentType {
    @Override
    public Appointment createAppointment(int patientId, int doctorId, LocalDateTime appointmentTime) {
        return new Appointment(
                patientId,
                doctorId,
                appointmentTime,
                "scheduled",
                Appointment.TYPE_IN_PERSON,
                getBaseFee()
        );
    }

    @Override
    public String getType() {
        return "In-Person Visit";
    }

    @Override
    public double getBaseFee() {
        return 100.0;
    }

    @Override
    public int getDurationMinutes() {
        return 45;
    }
}