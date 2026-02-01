package edu.altu.medapp;

import edu.altu.medapp.model.Appointment;
import edu.altu.medapp.model.Doctor;
import edu.altu.medapp.service.AppointmentService;
import edu.altu.medapp.service.DoctorAvailabilityService;

import edu.altu.medapp.service.*;
import edu.altu.medapp.model.AppointmentSummary;
import java.util.Optional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AppointmentService appointmentService = new AppointmentService();
        DoctorAvailabilityService availabilityService = new DoctorAvailabilityService();

        System.out.println("=== Medical Appointment System ===\n");

        while (true) {
            System.out.println("\n1. Book Appointment");
            System.out.println("2. Cancel Appointment");
            System.out.println("3. View Doctor's Schedule");
            System.out.println("4. View Patient's Upcoming Visits");
            System.out.println("5. Exit");
            System.out.println("6. Book Appointment with Type (Advanced)");
            System.out.println("7. Generate Appointment Summary");
            System.out.println("8. View Today's Appointments");
            System.out.println("9. Clinic Information");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    bookAppointment(scanner, appointmentService, availabilityService);
                    break;
                case "2":
                    cancelAppointment(scanner, appointmentService);
                    break;
                case "3":
                    viewDoctorSchedule(scanner, appointmentService);
                    break;
                case "4":
                    viewPatientUpcomingVisits(scanner, appointmentService);
                    break;
                case "5":
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                case "6":
                    bookAppointmentWithType(scanner);
                    break;
                case "7":
                    generateAppointmentSummary(scanner);
                    break;
                case "8":
                    viewTodayAppointments();
                    break;
                case "9":
                    showClinicInfo();
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private static void bookAppointment(Scanner scanner, AppointmentService appointmentService, DoctorAvailabilityService availabilityService) {
        System.out.println("\n--- Book Appointment ---");

        try {
            System.out.print("Enter patient ID: ");
            int patientId = Integer.parseInt(scanner.nextLine());

            System.out.println("Available Doctors:");
            List<Doctor> availableDoctors = availabilityService.getAvailableDoctors();
            if (availableDoctors.isEmpty()) {
                System.out.println("No available doctors");
                return;
            }

            for (Doctor doctor : availableDoctors) {
                System.out.println("ID: " + doctor.getId() + " - " + doctor.getName() + " (" + doctor.getSpecialization() + ")");
            }

            System.out.print("Enter doctor ID: ");
            int doctorId = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter date and time (YYYY-MM-DD HH:MM): ");
            String dateTimeStr = scanner.nextLine();
            LocalDateTime appointmentTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            Appointment appointment = appointmentService.bookAppointment(patientId, doctorId, appointmentTime);
            System.out.println("Appointment booked successfully! ID: " + appointment.getId());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void cancelAppointment(Scanner scanner, AppointmentService appointmentService) {
        System.out.println("\n--- Cancel Appointment ---");

        try {
            System.out.print("Enter appointment ID to cancel: ");
            int appointmentId = Integer.parseInt(scanner.nextLine());

            appointmentService.cancelAppointment(appointmentId);
            System.out.println("Appointment cancelled successfully");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewDoctorSchedule(Scanner scanner, AppointmentService appointmentService) {
        System.out.println("\n--- View Doctor's Schedule ---");

        try {
            System.out.print("Enter doctor ID: ");
            int doctorId = Integer.parseInt(scanner.nextLine());

            List<Appointment> schedule = appointmentService.getDoctorSchedule(doctorId);

            if (schedule.isEmpty()) {
                System.out.println("No appointments found");
            } else {
                System.out.println("Doctor's Schedule:");
                for (Appointment appointment : schedule) {
                    System.out.println("ID: " + appointment.getId() +
                            ", Patient ID: " + appointment.getPatientId() +
                            ", Time: " + appointment.getAppointmentTime() +
                            ", Status: " + appointment.getStatus());
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewPatientUpcomingVisits(Scanner scanner, AppointmentService appointmentService) {
        System.out.println("\n--- View Patient's Upcoming Visits ---");

        try {
            System.out.print("Enter patient ID: ");
            int patientId = Integer.parseInt(scanner.nextLine());

            List<Appointment> upcoming = appointmentService.getPatientUpcomingAppointments(patientId);

            if (upcoming.isEmpty()) {
                System.out.println("No upcoming appointments");
            } else {
                System.out.println("Upcoming Appointments:");
                for (Appointment appointment : upcoming) {
                    System.out.println("ID: " + appointment.getId() +
                            ", Doctor ID: " + appointment.getDoctorId() +
                            ", Time: " + appointment.getAppointmentTime() +
                            ", Status: " + appointment.getStatus());
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private static void bookAppointmentWithType(Scanner scanner) {
        System.out.println("\n--- Book Appointment with Type ---");

        EnhancedAppointmentService enhancedService = new EnhancedAppointmentService();

        try {
            System.out.print("Enter patient ID: ");
            int patientId = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter doctor ID: ");
            int doctorId = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter date and time (YYYY-MM-DD HH:MM): ");
            String dateTimeStr = scanner.nextLine();
            LocalDateTime appointmentTime = LocalDateTime.parse(dateTimeStr,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            System.out.println("\nAppointment Types:");
            System.out.println("1. Online Consultation");
            System.out.println("2. In-Person Visit");
            System.out.println("3. Follow-Up");
            System.out.print("Choose type (1-3): ");

            int typeChoice = Integer.parseInt(scanner.nextLine());
            AppointmentFactory.AppointmentType type;

            switch (typeChoice) {
                case 1: type = AppointmentFactory.AppointmentType.ONLINE; break;
                case 2: type = AppointmentFactory.AppointmentType.IN_PERSON; break;
                case 3: type = AppointmentFactory.AppointmentType.FOLLOW_UP; break;
                default:
                    System.out.println("Invalid type, defaulting to In-Person");
                    type = AppointmentFactory.AppointmentType.IN_PERSON;
            }

            Result<Appointment> result = enhancedService.bookAppointmentWithType(
                    patientId, doctorId, appointmentTime, type
            );

            if (result.isSuccess()) {
                System.out.println("Appointment booked successfully! ID: " +
                        result.getData().getId());
            } else {
                System.out.println("Failed to book appointment: " +
                        result.getErrorMessage());
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void generateAppointmentSummary(Scanner scanner) {
        System.out.println("\n--- Generate Appointment Summary ---");

        EnhancedAppointmentService enhancedService = new EnhancedAppointmentService();

        try {
            System.out.print("Enter appointment ID: ");
            int appointmentId = Integer.parseInt(scanner.nextLine());

            Optional<AppointmentSummary> summary =
                    enhancedService.generateAppointmentSummary(appointmentId);

            if (summary.isPresent()) {
                System.out.println("\n=== Appointment Summary ===");
                System.out.println(summary.get());
            } else {
                System.out.println("Appointment not found");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewTodayAppointments() {
        System.out.println("\n--- Today's Appointments ---");

        EnhancedAppointmentService enhancedService = new EnhancedAppointmentService();
        List<Appointment> todayAppointments = enhancedService.getTodayAppointments();

        if (todayAppointments.isEmpty()) {
            System.out.println("No appointments scheduled for today");
        } else {
            System.out.println("Today's Appointments (" + todayAppointments.size() + "):");
            todayAppointments.forEach(appt ->
                    System.out.println("ID: " + appt.getId() +
                            ", Doctor: " + appt.getDoctorId() +
                            ", Time: " + appt.getAppointmentTime().toLocalTime() +
                            ", Status: " + appt.getStatus())
            );
        }
    }

    private static void showClinicInfo() {
        System.out.println("\n--- Clinic Information ---");

        ClinicConfig config = ClinicConfig.getInstance();
        System.out.println("Working Hours: " + config.getWorkingHoursString());
        System.out.println("Appointment Duration: " + config.getAppointmentDurationMinutes() + " minutes");
        System.out.println("Max Appointments Per Day: " + config.getMaxAppointmentsPerDay());
    }
}