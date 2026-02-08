package edu.altu.medapp.SchedulingComponent.service;

import edu.altu.medapp.SchedulingComponent.exceptions.AppointmentConflictException;
import edu.altu.medapp.SchedulingComponent.exceptions.AppointmentNotFoundException;
import edu.altu.medapp.DoctorManagementComponent.exceptions.DoctorUnavailableException;
import edu.altu.medapp.SchedulingComponent.model.Appointment;
import edu.altu.medapp.SchedulingComponent.model.AppointmentSummary;
import edu.altu.medapp.DoctorManagementComponent.model.Doctor;
import edu.altu.medapp.SchedulingComponent.repository.AppointmentRepository;
import edu.altu.medapp.DoctorManagementComponent.repository.DoctorRepository;

import java.time.LocalDateTime;
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
            if (appointment == null) {
                throw new AppointmentNotFoundException("Appointment " + id + " not found");
            }
            appointment.setStatus("cancelled");
            appointmentRepo.update(appointment);
        } catch (RuntimeException e) {
            throw new AppointmentNotFoundException("Appointment " + id + " not found");
        }
    }

    public Result<AppointmentSummary> getSummary(int appointmentId) {
        try {
            Appointment appointment = appointmentRepo.findById(appointmentId);
            if (appointment == null) {
                return Result.failure("Appointment not found");
            }
            AppointmentSummary summary = new AppointmentSummary.Builder()
                    .appointment(appointment)
                    .build();
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
        Predicate<Appointment> isUpcoming = apt ->
                apt.getTime().isAfter(now) && "scheduled".equals(apt.getStatus());

        return appointmentRepo.findAll().stream()
                .filter(apt -> apt.getPatientId() == patientId)
                .filter(isUpcoming)
                .collect(Collectors.toList());
    }

    private void checkTime(LocalDateTime time) {
        ClinicConfig config = ClinicConfig.getInstance();
        if (!config.isOpen(time.toLocalTime())) {
            throw new RuntimeException("Clinic closed at " + time.toLocalTime());
        }
    }

    private void checkDoctor(int doctorId) {
        Doctor doctor = doctorRepo.findById(doctorId);
        if (doctor == null) {
            throw new DoctorUnavailableException("Doctor " + doctorId + " not found");
        }
        if (!doctor.isAvailable()) {
            throw new DoctorUnavailableException("Doctor " + doctor.getName() + " unavailable");
        }
    }

    private void checkSlot(int doctorId, LocalDateTime time) {
        if (appointmentRepo.isTimeSlotTaken(doctorId, time)) {
            throw new AppointmentConflictException("Time slot taken: " + time);
        }
    }
}