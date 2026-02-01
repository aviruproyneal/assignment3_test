package edu.altu.medapp.service;  // Make sure this is correct

// ADD THESE IMPORTS:
import edu.altu.medapp.interfaces.IAppointmentType;
import edu.altu.medapp.model.*;
import edu.altu.medapp.repository.AppointmentRepository;
import edu.altu.medapp.service.exceptions.AppointmentConflictException;
import edu.altu.medapp.service.exceptions.DoctorUnavailableException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class EnhancedAppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorAvailabilityService doctorAvailabilityService;
    private final ClinicConfig clinicConfig;

    public EnhancedAppointmentService() {
        this.appointmentRepository = new AppointmentRepository();
        this.doctorAvailabilityService = new DoctorAvailabilityService();
        this.clinicConfig = ClinicConfig.getInstance();
    }

    // Use Factory Pattern for different appointment types
    public Result<Appointment> bookAppointmentWithType(
            int patientId, int doctorId, LocalDateTime appointmentTime,
            AppointmentFactory.AppointmentType appointmentType) {

        try {
            // Validate using Singleton configuration
            if (!clinicConfig.isWithinWorkingHours(appointmentTime.toLocalTime())) {
                return Result.failure(
                        String.format("Clinic is closed at %s. Working hours: %s",
                                appointmentTime.toLocalTime(), clinicConfig.getWorkingHoursString())
                );
            }

            doctorAvailabilityService.checkDoctorAvailability(doctorId);

            if (appointmentRepository.isTimeSlotBooked(doctorId, appointmentTime)) {
                throw new AppointmentConflictException("Time slot already booked");
            }

            // Use Factory to create appropriate appointment type
            Appointment appointment = AppointmentFactory.createAppointment(
                    appointmentType, patientId, doctorId, appointmentTime
            );

            IAppointmentType type = AppointmentFactory.createAppointmentType(appointmentType);
            System.out.println("Booking " + type.getType() + " | Fee: $" + type.getBaseFee());

            appointmentRepository.save(appointment);
            return Result.success(appointment);

        } catch (DoctorUnavailableException | AppointmentConflictException e) {
            return Result.failure(e.getMessage());
        }
    }

    // Use Builder Pattern for appointment summary
    public Optional<AppointmentSummary> generateAppointmentSummary(int appointmentId) {
        return appointmentRepository.findById(appointmentId).map(appointment -> {
            // In real scenario, you would fetch patient and doctor names from repositories
            return new AppointmentSummary.Builder(
                    appointment.getId(),
                    "Patient #" + appointment.getPatientId(),  // Would be real name
                    "Doctor #" + appointment.getDoctorId(),    // Would be real name
                    appointment.getAppointmentTime(),
                    appointment.getStatus()
            )
                    .summaryNotes("Regular checkup")
                    .consultationFee(100.0)
                    .build();
        });
    }

    // Use Lambda for filtering
    public List<Appointment> findAppointments(Predicate<Appointment> filter) {
        return appointmentRepository.findAll().stream()
                .filter(filter)
                .toList();
    }

    // Example lambda usage
    public List<Appointment> getTodayAppointments() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59);

        return findAppointments(appt ->
                appt.getAppointmentTime().isAfter(startOfDay) &&
                        appt.getAppointmentTime().isBefore(endOfDay) &&
                        appt.getStatus().equals("scheduled")
        );
    }
}