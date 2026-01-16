package edu.altu.medapp;

import edu.altu.medapp.model.*;
import edu.altu.medapp.service.AppointmentService;
import edu.altu.medapp.service.DoctorAvailabilityService;
import edu.altu.medapp.service.exceptions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Medical Appointment Booking System ===\n");

        try {
            // Initialize services
            AppointmentService appointmentService = new AppointmentService();
            DoctorAvailabilityService availabilityService = new DoctorAvailabilityService();

            // Demo 1: Book an appointment
            System.out.println("1. Booking an appointment...");
            try {
                Appointment appointment = appointmentService.bookAppointment(
                        1, // patientId (assuming exists)
                        1, // doctorId (assuming exists)
                        LocalDateTime.now().plusDays(1).withHour(10).withMinute(0)
                );
                System.out.println("   Appointment booked: " + appointment);
            } catch (AppointmentConflictException | DoctorUnavailableException e) {
                System.out.println("   Error: " + e.getMessage());
            }

            // Demo 2: View doctor's schedule
            System.out.println("\n2. Viewing doctor's schedule...");
            List<Appointment> doctorSchedule = appointmentService.getDoctorSchedule(1);
            System.out.println("   Doctor ID 1 has " + doctorSchedule.size() + " appointments");

            // Demo 3: View patient's upcoming appointments
            System.out.println("\n3. Viewing patient's upcoming appointments...");
            List<Appointment> patientAppointments = appointmentService.getPatientUpcomingAppointments(1);
            System.out.println("   Patient ID 1 has " + patientAppointments.size() + " upcoming appointments");

            // Demo 4: Cancel an appointment
            System.out.println("\n4. Cancelling an appointment...");
            try {
                appointmentService.cancelAppointment(1);
                System.out.println("   Appointment cancelled successfully");
            } catch (AppointmentNotFoundException e) {
                System.out.println("   Error: " + e.getMessage());
            }

            // Demo 5: Check available doctors
            System.out.println("\n5. Checking available doctors...");
            List<edu.altu.medapp.model.Doctor> availableDoctors = availabilityService.getAvailableDoctors();
            System.out.println("   There are " + availableDoctors.size() + " available doctors");

            System.out.println("\n=== Demo Completed Successfully ===");

        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}