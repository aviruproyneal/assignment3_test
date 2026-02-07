package edu.altu.medapp;

import edu.altu.medapp.model.Appointment;
import edu.altu.medapp.model.AppointmentSummary;
import edu.altu.medapp.service.AppointmentService;
import edu.altu.medapp.service.Result;

import edu.altu.medapp.components.SchedulingComponent;
import edu.altu.medapp.components.PatientRecordsComponent;
import edu.altu.medapp.components.DoctorManagementComponent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        SchedulingComponent scheduling = new SchedulingComponent();
        PatientRecordsComponent patients = new PatientRecordsComponent();
        DoctorManagementComponent doctors = new DoctorManagementComponent();
        AppointmentService appointmentService = new AppointmentService();

        System.out.println("=== Medical Appointment System ===\n");

        boolean running = true;
        while (running) {
            System.out.println("Menu:");
            System.out.println("1. Book new appointment");
            System.out.println("2. See doctor schedule");
            System.out.println("3. Check patient appointments");
            System.out.println("4. Cancel appointment");
            System.out.println("5. Show available doctors");
            System.out.println("6. Get appointment summary");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");

            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    bookAppointment(scanner, scheduling);
                    break;
                case "2":
                    viewDoctorSchedule(scanner, scheduling);
                    break;
                case "3":
                    viewPatientAppointments(scanner, patients);
                    break;
                case "4":
                    cancelAppointment(scanner, scheduling);
                    break;
                case "5":
                    showAvailableDoctors(doctors);
                    break;
                case "6":
                    getAppointmentSummary(scanner, appointmentService);
                    break;
                case "7":
                    System.out.println("Closing system...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option, try again");
            }
        }

        scanner.close();
        System.out.println("Goodbye!");
    }

    static void bookAppointment(Scanner scanner, SchedulingComponent scheduling) {
        System.out.println("\n--- Book Appointment ---");

        try {
            System.out.print("Enter patient ID: ");
            String patientInput = scanner.nextLine();
            int patientId = Integer.parseInt(patientInput);

            System.out.print("Enter doctor ID: ");
            String doctorInput = scanner.nextLine();
            int doctorId = Integer.parseInt(doctorInput);

            System.out.print("Enter date and time (YYYY-MM-DD HH:MM): ");
            String dateTimeInput = scanner.nextLine();
            LocalDateTime appointmentTime = LocalDateTime.parse(dateTimeInput,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            System.out.println("Choose appointment type:");
            System.out.println("1 - Online ($50)");
            System.out.println("2 - In-Person ($100)");
            System.out.println("3 - Follow-Up ($75)");
            System.out.print("Type (1-3): ");
            String typeChoice = scanner.nextLine();

            String type;
            if (typeChoice.equals("1")) {
                type = "ONLINE";
            } else if (typeChoice.equals("2")) {
                type = "IN_PERSON";
            } else if (typeChoice.equals("3")) {
                type = "FOLLOW_UP";
            } else {
                type = "IN_PERSON";
                System.out.println("Defaulting to In-Person");
            }

            Appointment appointment = scheduling.bookAppointment(patientId, doctorId, appointmentTime, type);
            System.out.println("Appointment booked! ID: " + appointment.getId());
            System.out.println("Fee: $" + appointment.getFee());

        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter valid numbers for IDs");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println();
    }

    static void viewDoctorSchedule(Scanner scanner, SchedulingComponent scheduling) {
        System.out.println("\n--- Doctor Schedule ---");

        try {
            System.out.print("Enter doctor ID: ");
            String doctorInput = scanner.nextLine();
            int doctorId = Integer.parseInt(doctorInput);

            var appointments = scheduling.getSchedule(doctorId);

            if (appointments.isEmpty()) {
                System.out.println("No appointments found for this doctor");
            } else {
                System.out.println("Appointments for doctor " + doctorId + ":");
                for (Appointment apt : appointments) {
                    System.out.println("ID: " + apt.getId() +
                            " | Time: " + apt.getTime() +
                            " | Patient: " + apt.getPatientId() +
                            " | Status: " + apt.getStatus());
                }
                System.out.println("Total: " + appointments.size() + " appointment(s)");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println();
    }

    static void viewPatientAppointments(Scanner scanner, PatientRecordsComponent patients) {
        System.out.println("\n--- Patient Appointments ---");

        try {
            System.out.print("Enter patient ID: ");
            String patientInput = scanner.nextLine();
            int patientId = Integer.parseInt(patientInput);

            var appointments = patients.getAppointments(patientId);

            if (appointments.isEmpty()) {
                System.out.println("No upcoming appointments for patient " + patientId);
            } else {
                System.out.println("Upcoming appointments for patient " + patientId + ":");
                for (Appointment apt : appointments) {
                    System.out.println("ID: " + apt.getId() +
                            " | Doctor: " + apt.getDoctorId() +
                            " | Time: " + apt.getTime() +
                            " | Type: " + apt.getType());
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println();
    }

    static void cancelAppointment(Scanner scanner, SchedulingComponent scheduling) {
        System.out.println("\n--- Cancel Appointment ---");

        try {
            System.out.print("Enter appointment ID to cancel: ");
            String appointmentInput = scanner.nextLine();
            int appointmentId = Integer.parseInt(appointmentInput);

            scheduling.cancelAppointment(appointmentId);
            System.out.println("Appointment " + appointmentId + " cancelled successfully");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println();
    }

    static void showAvailableDoctors(DoctorManagementComponent doctors) {
        System.out.println("\n--- Available Doctors ---");

        var availableDoctors = doctors.getAvailableDoctors();

        if (availableDoctors.isEmpty()) {
            System.out.println("No doctors available at the moment");
        } else {
            System.out.println("Doctors currently available:");
            for (var doctor : availableDoctors) {
                System.out.println("ID: " + doctor.getId() +
                        " - Dr. " + doctor.getName() +
                        " (" + doctor.getSpecialization() + ")");
            }
        }

        System.out.println();
    }

    static void getAppointmentSummary(Scanner scanner, AppointmentService appointmentService) {
        System.out.println("\n--- Appointment Summary ---");

        try {
            System.out.print("Enter appointment ID: ");
            String appointmentInput = scanner.nextLine();
            int appointmentId = Integer.parseInt(appointmentInput);

            Result<AppointmentSummary> result = appointmentService.getSummary(appointmentId);

            if (result.isSuccess()) {
                System.out.println("\n=== SUMMARY ===");
                System.out.println(result.getValue());
            } else {
                System.out.println("Error: " + result.getError());
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println();
    }
}