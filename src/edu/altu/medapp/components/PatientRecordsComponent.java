package edu.altu.medapp.components;

import edu.altu.medapp.model.Appointment;
import edu.altu.medapp.service.AppointmentService;
import java.util.List;

public class PatientRecordsComponent {
    private final AppointmentService appointmentService;

    public PatientRecordsComponent() {
        this.appointmentService = new AppointmentService();
    }

    public List<Appointment> getAppointments(int patientId) {
        return appointmentService.getUpcoming(patientId);
    }
}