package edu.altu.medapp.interfaces;

import edu.altu.medapp.model.Appointment;
import java.time.LocalDateTime;

public interface IAppointmentType {
    Appointment createAppointment(int patientId, int doctorId, LocalDateTime appointmentTime);
    String getType();
    double getBaseFee();
    int getDurationMinutes();
}