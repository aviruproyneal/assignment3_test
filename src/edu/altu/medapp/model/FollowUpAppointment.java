package edu.altu.medapp.model;

import edu.altu.medapp.interfaces.IAppointmentType;
import java.time.LocalDateTime;

public class FollowUpAppointment implements IAppointmentType {
    @Override
    public Appointment createAppointment(int patientId, int doctorId, LocalDateTime appointmentTime) {
        return new Appointment(
                patientId,
                doctorId,
                appointmentTime,
                "scheduled",
                Appointment.TYPE_FOLLOW_UP,
                getBaseFee()
        );
    }

    @Override
    public String getType() {
        return "Follow-Up";
    }

    @Override
    public double getBaseFee() {
        return 75.0;
    }

    @Override
    public int getDurationMinutes() {
        return 20;
    }
}