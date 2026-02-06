package edu.altu.medapp;

import edu.altu.medapp.model.Appointment;
import edu.altu.medapp.model.AppointmentSummary;
import edu.altu.medapp.service.AppointmentService;
import edu.altu.medapp.service.ClinicConfig;
import edu.altu.medapp.service.Result;

import edu.altu.medapp.components.SchedulingComponent;
import edu.altu.medapp.components.PatientRecordsComponent;
import edu.altu.medapp.components.DoctorManagementComponent;
import edu.altu.medapp.components.BillingComponent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        SchedulingComponent scheduling = new SchedulingComponent();
        PatientRecordsComponent patients = new PatientRecordsComponent();
        DoctorManagementComponent doctors = new DoctorManagementComponent();
        BillingComponent billing = new BillingComponent();
        AppointmentService appointmentService = new AppointmentService();

        System.out.println("=== Medical Appointment System ===\n");

        while (true) {
            System.out.println("1. Book Appointment");
            System.out.println("2. View Schedule");
            System.out.println("3. View Patient Appointments");
            System.out.println("4. Cancel Appointment");
            System.out.println("5. View Available Doctors");
            System.out.println("6. Clinic Info");
            System.out.println("7. Today's Appointments");
            System.out.println("8. Revenue");
            System.out.println("9. View Appointment Summary");
            System.out.println("10. Exit");
            System.out.print("Choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleBookAppointment(scanner, scheduling);
                    break;
                case "2":
                    handleViewSchedule(scanner, scheduling);
                    break;
                case "3":
                    handlePatientAppointments(scanner, patients);
                    break;
                case "4":
                    handleCancelAppointment(scanner, scheduling);
                    break;
                case "5":
                    handleAvailableDoctors(doctors);
                    break;
                case "6":
                    handleClinicInfo();
                    break;
                case "7":
                    handleTodayAppointments(scheduling);
                    break;
                case "8":
                    handleRevenue(billing);
                    break;
                case "9":
                    viewAppointmentSummary(scanner, appointmentService);
                    break;
                case "10":
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private static void handleBookAppointment(Scanner scanner, SchedulingComponent scheduling) {
        try {
            System.out.print("Patient ID: ");
            int patientId = Integer.parseInt(scanner.nextLine());

            System.out.print("Doctor ID: ");
            int doctorId = Integer.parseInt(scanner.nextLine());

            System.out.print("Date and Time (YYYY-MM-DD HH:MM): ");
            String dateTimeStr = scanner.nextLine();
            LocalDateTime time = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            System.out.println("Types: 1.Online 2.In-Person 3.Follow-Up");
            System.out.print("Type (1-3): ");
            String typeChoice = scanner.nextLine();

            String type;
            switch (typeChoice) {
                case "1": type = "ONLINE"; break;
                case "2": type = "IN_PERSON"; break;
                case "3": type = "FOLLOW_UP"; break;
                default: type = "IN_PERSON";
            }

            Appointment appointment = scheduling.bookAppointment(patientId, doctorId, time, type);
            System.out.println("Booked! ID: " + appointment.getId());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleViewSchedule(Scanner scanner, SchedulingComponent scheduling) {
        try {
            System.out.print("Doctor ID: ");
            int doctorId = Integer.parseInt(scanner.nextLine());

            var schedule = scheduling.getSchedule(doctorId);

            if (schedule.isEmpty()) {
                System.out.println("No appointments");
            } else {
                System.out.println("Doctor's Schedule:");
                for (Appointment appt : schedule) {
                    System.out.printf("ID: %d | Time: %s | Type: %s | Fee: $%.2f%n",
                            appt.getId(), appt.getTime(), appt.getType(), appt.getFee());
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handlePatientAppointments(Scanner scanner, PatientRecordsComponent patients) {
        try {
            System.out.print("Patient ID: ");
            int patientId = Integer.parseInt(scanner.nextLine());

            var appointments = patients.getAppointments(patientId);

            if (appointments.isEmpty()) {
                System.out.println("No appointments");
            } else {
                System.out.println("Upcoming Appointments:");
                for (Appointment appt : appointments) {
                    System.out.printf("ID: %d | Doctor: %d | Time: %s | Type: %s%n",
                            appt.getId(), appt.getDoctorId(), appt.getTime(), appt.getType());
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleCancelAppointment(Scanner scanner, SchedulingComponent scheduling) {
        try {
            System.out.print("Appointment ID: ");
            int appointmentId = Integer.parseInt(scanner.nextLine());

            scheduling.cancelAppointment(appointmentId);
            System.out.println("Cancelled");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleAvailableDoctors(DoctorManagementComponent doctors) {
        var available = doctors.getAvailableDoctors();

        if (available.isEmpty()) {
            System.out.println("No doctors available");
        } else {
            System.out.println("Available Doctors:");
            for (var doc : available) {
                System.out.printf("ID: %d | Dr. %s | %s%n",
                        doc.getId(), doc.getName(), doc.getSpecialization());
            }
        }
    }

    private static void handleClinicInfo() {
        ClinicConfig config = ClinicConfig.getInstance();
        System.out.println("Clinic Hours: " + config.getHours());
        System.out.println("Appointment Types:");
        System.out.println("- Online: $50.00");
        System.out.println("- In-Person: $100.00");
        System.out.println("- Follow-Up: $75.00");
    }

    private static void handleTodayAppointments(SchedulingComponent scheduling) {
        var today = scheduling.getTodayAppointments();

        if (today.isEmpty()) {
            System.out.println("No appointments today");
        } else {
            System.out.println("Today's Appointments (" + today.size() + "):");
            for (Appointment appt : today) {
                System.out.printf("ID: %d | Doctor: %d | Time: %s%n",
                        appt.getId(), appt.getDoctorId(), appt.getTime().toLocalTime());
            }
        }
    }

    private static void handleRevenue(BillingComponent billing) {
        double revenue = billing.getTotalRevenue();
        System.out.printf("Total Revenue: $%.2f%n", revenue);
    }

    private static void viewAppointmentSummary(Scanner scanner, AppointmentService appointmentService) {
        System.out.println("\n--- Appointment Summary (Builder Pattern + Result<T> Demo) ---");

        try {
            System.out.print("Enter appointment ID: ");
            int appointmentId = Integer.parseInt(scanner.nextLine());

            Result<AppointmentSummary> result = appointmentService.getSummary(appointmentId);

            if (result.isSuccess()) {
                System.out.println("\n" + result.getValue());
                System.out.println("\nBuilt using Builder Pattern");
                System.out.println("Returned using Result generic type");
            } else {
                System.out.println(result.getError());
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}