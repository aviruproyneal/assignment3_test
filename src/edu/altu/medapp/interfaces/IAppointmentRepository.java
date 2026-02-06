package edu.altu.medapp.interfaces;

import edu.altu.medapp.model.Appointment;
import java.util.List;

public interface IAppointmentRepository extends IRepository<Appointment> {
    List<Appointment> findByDoctorId(int doctorId);
    boolean isTimeSlotTaken(int doctorId, java.time.LocalDateTime time);
}