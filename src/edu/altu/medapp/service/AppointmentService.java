package edu.altu.medapp.service;

import edu.altu.medapp.model.Appointment;
import edu.altu.medapp.repository.AppointmentRepository;
import edu.altu.medapp.service.exceptions.AppointmentConflictException;
import edu.altu.medapp.service.exceptions.AppointmentNotFoundException;
import edu.altu.medapp.service.exceptions.DoctorUnavailableException;

import java.time.LocalDateTime;
import java.util.List;

public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorAvailabilityService doctorAvailabilityService;

    public AppointmentService() {
        this.appointmentRepository = new AppointmentRepository();
        this.doctorAvailabilityService = new DoctorAvailabilityService();
    }

    public Appointment bookAppointment(int patientId, int doctorId, LocalDateTime appointmentTime) {
        // Check if doctor is available
        doctorAvailabilityService.checkDoctorAvailability(doctorId);

        // Check if time slot is already booked
        if (appointmentRepository.isTimeSlotBooked(doctorId, appointmentTime)) {
            throw new AppointmentConflictException("Time slot " + appointmentTime + " is already booked for this doctor");
        }

        // Create and save appointment
        Appointment appointment = new Appointment(patientId, doctorId, appointmentTime, "scheduled");
        appointmentRepository.save(appointment);

        return appointment;
    }

    public void cancelAppointment(int appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with ID: " + appointmentId));

        appointment.setStatus("cancelled");
        appointmentRepository.update(appointment);
    }

    public List<Appointment> getDoctorSchedule(int doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    public List<Appointment> getPatientUpcomingAppointments(int patientId) {
        return appointmentRepository.findUpcomingAppointments(patientId);
    }

    public Appointment getAppointmentById(int appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with ID: " + appointmentId));
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public void rescheduleAppointment(int appointmentId, LocalDateTime newTime) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with ID: " + appointmentId));

        // Check if new time slot is available
        if (appointmentRepository.isTimeSlotBooked(appointment.getDoctorId(), newTime)) {
            throw new AppointmentConflictException("Time slot " + newTime + " is already booked");
        }

        appointment.setAppointmentTime(newTime);
        appointmentRepository.update(appointment);
    }
}