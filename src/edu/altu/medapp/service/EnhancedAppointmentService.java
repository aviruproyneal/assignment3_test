package edu.altu.medapp.service;

import edu.altu.medapp.interfaces.IAppointmentType;
import edu.altu.medapp.model.*;
import edu.altu.medapp.repository.AppointmentRepository;
import edu.altu.medapp.service.exceptions.AppointmentConflictException;
import edu.altu.medapp.service.exceptions.DoctorUnavailableException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EnhancedAppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorAvailabilityService doctorAvailabilityService;
    private final ClinicConfig clinicConfig;

    public EnhancedAppointmentService() {
        this.appointmentRepository = new AppointmentRepository();
        this.doctorAvailabilityService = new DoctorAvailabilityService();
        this.clinicConfig = ClinicConfig.getInstance();
    }

    public Result<Appointment> bookAppointmentWithType(
            int patientId, int doctorId, LocalDateTime appointmentTime,
            AppointmentFactory.AppointmentType appointmentType) {

        try {
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

            IAppointmentType appointmentTypeObj = AppointmentFactory.createAppointmentType(appointmentType);
            String typeName = appointmentTypeObj.getType();
            double fee = appointmentTypeObj.getBaseFee();

            System.out.println("Booking " + typeName + " | Fee: $" + fee);

            Appointment appointment = AppointmentFactory.createAppointment(
                    appointmentType, patientId, doctorId, appointmentTime
            );

            appointmentRepository.save(appointment);
            return Result.success(appointment);

        } catch (DoctorUnavailableException | AppointmentConflictException e) {
            return Result.failure(e.getMessage());
        }
    }

    public Optional<AppointmentSummary> generateAppointmentSummary(int appointmentId) {
        return appointmentRepository.findById(appointmentId).map(appointment -> {
            String typeDisplay = "In-Person Visit"; // Default
            if (Appointment.TYPE_ONLINE.equals(appointment.getAppointmentType())) {
                typeDisplay = "Online Consultation";
            } else if (Appointment.TYPE_FOLLOW_UP.equals(appointment.getAppointmentType())) {
                typeDisplay = "Follow-Up";
            }

            return new AppointmentSummary.Builder(
                    appointment.getId(),
                    "Patient #" + appointment.getPatientId(),
                    "Doctor #" + appointment.getDoctorId(),
                    appointment.getAppointmentTime(),
                    appointment.getStatus()
            )
                    .summaryNotes(typeDisplay + " appointment")
                    .consultationFee(appointment.getConsultationFee())
                    .build();
        });
    }

    public List<Appointment> findAppointments(Predicate<Appointment> filter) {
        return appointmentRepository.findAll().stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    public List<Appointment> getTodayAppointments() {
        LocalDate today = LocalDate.now();

        return appointmentRepository.findAll().stream()
                .filter(appt ->
                        appt.getAppointmentTime().toLocalDate().equals(today) &&
                                Appointment.STATUS_SCHEDULED.equals(appt.getStatus())
                )
                .collect(Collectors.toList());
    }
}