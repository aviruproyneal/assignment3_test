package edu.altu.medapp.components;

import edu.altu.medapp.model.Appointment;
import edu.altu.medapp.repository.AppointmentRepository;

public class BillingComponent {
    private final AppointmentRepository appointmentRepo;

    public BillingComponent() {
        this.appointmentRepo = new AppointmentRepository();
    }

    public double getTotalRevenue() {
        return appointmentRepo.findAll().stream()
                .filter(appt -> appt.getStatus().equals("completed"))
                .mapToDouble(Appointment::getFee)
                .sum();
    }
}