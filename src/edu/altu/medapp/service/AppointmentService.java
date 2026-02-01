package edu.altu.medapp.service;

import edu.altu.medapp.model.Appointment;
import edu.altu.medapp.repository.AppointmentRepository;
import edu.altu.medapp.service.exceptions.AppointmentConflictException;
import edu.altu.medapp.service.exceptions.AppointmentNotFoundException;
import edu.altu.medapp.service.exceptions.DoctorUnavailableException;

import java.time.LocalDateTime;
import java.util.List;

import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorAvailabilityService doctorAvailabilityService;

    public AppointmentService() {
        this.appointmentRepository = new AppointmentRepository();
        this.doctorAvailabilityService = new DoctorAvailabilityService();
    }

    public Appointment bookAppointment(int patientId, int doctorId, LocalDateTime appointmentTime) {
        validateAppointment(doctorId, appointmentTime);
        Appointment appointment = new Appointment(patientId, doctorId, appointmentTime, "scheduled");
        appointmentRepository.save(appointment);
        return appointment;
    }

    public void cancelAppointment(int appointmentId) {
        Appointment appointment = findAppointment(appointmentId);
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
        return findAppointment(appointmentId);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public void rescheduleAppointment(int appointmentId, LocalDateTime newTime) {
        Appointment appointment = findAppointment(appointmentId);
        validateAppointment(appointment.getDoctorId(), newTime);
        appointment.setAppointmentTime(newTime);
        appointmentRepository.update(appointment);
    }

    private void validateAppointment(int doctorId, LocalDateTime time) {
        doctorAvailabilityService.checkDoctorAvailability(doctorId);
        if (appointmentRepository.isTimeSlotBooked(doctorId, time)) {
            throw new AppointmentConflictException("Time slot " + time + " is already booked");
        }
    }

    private Appointment findAppointment(int appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with ID: " + appointmentId));
    }


    public List<Appointment> filterAppointments(Predicate<Appointment> filterCondition) {
        return appointmentRepository.findAll().stream()
                .filter(filterCondition)
                .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsSorted(Comparator<Appointment> sorter) {
        return appointmentRepository.findAll().stream()
                .sorted(sorter)
                .collect(Collectors.toList());
    }

    public List<Appointment> findAppointmentsWithCondition(Predicate<Appointment> condition) {
        return appointmentRepository.findAll().stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    public List<Appointment> getHighPriorityAppointments() {
        return filterAppointments(appt ->
                appt.getAppointmentTime().isBefore(java.time.LocalDateTime.now().plusDays(1)) &&
                        appt.getStatus().equals("scheduled")
        );
    }

    public List<Appointment> getAppointmentsSortedByTime() {
        return getAppointmentsSorted(Comparator.comparing(Appointment::getAppointmentTime));
    }
}