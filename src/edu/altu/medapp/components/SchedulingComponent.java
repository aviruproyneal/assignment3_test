package edu.altu.medapp.components;

import edu.altu.medapp.model.Appointment;
import edu.altu.medapp.service.AppointmentService;
import java.time.LocalDateTime;
import java.util.List;

public class SchedulingComponent {
    private final AppointmentService appointmentService;

    public SchedulingComponent() {this.appointmentService = new AppointmentService();}

    public Appointment bookAppointment(int patientId, int doctorId, LocalDateTime time, String type) {
        return appointmentService.book(patientId, doctorId, time, type);
    }

    public void cancelAppointment(int appointmentId) {
        appointmentService.cancel(appointmentId);
    }

    public List<Appointment> getSchedule(int doctorId) {return appointmentService.getDoctorSchedule(doctorId);}
}