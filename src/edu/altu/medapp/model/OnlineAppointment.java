package edu.altu.medapp.model;

import edu.altu.medapp.interfaces.IAppointmentType;
import java.time.LocalDateTime;

public class OnlineAppointment implements IAppointmentType {
    @Override
    public Appointment createAppointment(int patientId, int doctorId, LocalDateTime appointmentTime) {
        return new Appointment(
                patientId,
                doctorId,
                appointmentTime,
                "scheduled",
                Appointment.TYPE_ONLINE,
                getBaseFee()
        );
    }

    @Override
    public String getType() {
        return "Online Consultation";
    }

    @Override
    public double getBaseFee() {
        return 50.0;
    }

    @Override
    public int getDurationMinutes() {
        return 30;
    }
}