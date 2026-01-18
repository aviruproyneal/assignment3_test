package edu.altu.medapp.interfaces;

import edu.altu.medapp.model.Appointment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IAppointmentRepository {
    void save(Appointment appointment);
    Optional<Appointment> findById(int id);
    List<Appointment> findAll();
    void update(Appointment appointment);
    void delete(int id);
    List<Appointment> findByPatientId(int patientId);
    List<Appointment> findByDoctorId(int doctorId);
    boolean isTimeSlotBooked(int doctorId, LocalDateTime appointmentTime);
    List<Appointment> findUpcomingAppointments(int patientId);
}