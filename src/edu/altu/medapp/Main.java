// Avirup and Vadim

package edu.altu.medapp;

import edu.altu.medapp.model.Appointment;
import edu.altu.medapp.model.Doctor;
import edu.altu.medapp.service.AppointmentService;
import edu.altu.medapp.service.DoctorAvailabilityService;

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
}