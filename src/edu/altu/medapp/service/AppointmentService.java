package edu.altu.medapp.service;

import edu.altu.medapp.exceptions.AppointmentConflictException;
import edu.altu.medapp.exceptions.AppointmentNotFoundException;
import edu.altu.medapp.exceptions.DoctorUnavailableException;
import edu.altu.medapp.model.Appointment;
import edu.altu.medapp.model.AppointmentSummary;
import edu.altu.medapp.model.Doctor;
import edu.altu.medapp.repository.AppointmentRepository;
import edu.altu.medapp.repository.DoctorRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AppointmentService {
    private final AppointmentRepository appointmentRepo;
    private final DoctorRepository doctorRepo;

    public AppointmentService() {
        this.appointmentRepo = new AppointmentRepository();
        this.doctorRepo = new DoctorRepository();
    }

    public Appointment book(int patientId, int doctorId, LocalDateTime time, String type) {
        checkTime(time);
        checkDoctor(doctorId);
        checkSlot(doctorId, time);

        Appointment appointment = AppointmentFactory.create(type, patientId, doctorId, time);
        appointmentRepo.save(appointment);
        return appointment;
    }

    public void cancel(int id) {
        try {
            Appointment appointment = appointmentRepo.findById(id);
            appointment.setStatus("cancelled");
            appointmentRepo.update(appointment);
        } catch (RuntimeException e) {
            throw new AppointmentNotFoundException("Appointment " + id + " not found");
        }
    }

    public Result<AppointmentSummary> getSummary(int appointmentId) {
        try {
            Appointment appointment = appointmentRepo.findById(appointmentId);
            AppointmentSummary summary = new AppointmentSummary.Builder().appointment(appointment).build();
            return Result.success(summary);
        } catch (Exception e) {
            return Result.failure("Could not generate summary: " + e.getMessage());
        }
    }

    public List<Appointment> getDoctorSchedule(int doctorId) {
        return appointmentRepo.findByDoctorId(doctorId);
    }

    public List<Appointment> getUpcoming(int patientId) {
        LocalDateTime now = LocalDateTime.now();
        Predicate<Appointment> upcoming = appt ->
                appt.getTime().isAfter(now) && appt.getStatus().equals("scheduled");

        return appointmentRepo.findAll().stream()
                .filter(appt -> appt.getPatientId() == patientId)
                .filter(upcoming)
                .collect(Collectors.toList());
    }

    private void checkTime(LocalDateTime time) {
        ClinicConfig config = ClinicConfig.getInstance();
        if (!config.isOpen(time.toLocalTime())) {
            throw new RuntimeException("Clinic closed at " + time.toLocalTime());
        }
    }

    private void checkDoctor(int doctorId) {
        try {
            Doctor doctor = doctorRepo.findById(doctorId);
            if (!doctor.isAvailable()) {
                throw new DoctorUnavailableException("Doctor " + doctor.getName() + " unavailable");
            }
        } catch (RuntimeException e) {
            throw new DoctorUnavailableException("Doctor " + doctorId + " not found");
        }
    }

    private void checkSlot(int doctorId, LocalDateTime time) {
        if (appointmentRepo.isTimeSlotTaken(doctorId, time)) {
            throw new AppointmentConflictException("Time slot taken: " + time);
        }
    }
}