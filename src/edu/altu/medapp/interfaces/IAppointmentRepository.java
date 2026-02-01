package edu.altu.medapp.interfaces;

import edu.altu.medapp.model.Appointment;
import java.time.LocalDateTime;
import java.util.List;

public interface IAppointmentRepository extends IRepository<Appointment> {  // No ID parameter
    // Keep existing specific methods
    List<Appointment> findByPatientId(int patientId);
    List<Appointment> findByDoctorId(int doctorId);
    boolean isTimeSlotBooked(int doctorId, LocalDateTime appointmentTime);
    List<Appointment> findUpcomingAppointments(int patientId);
}