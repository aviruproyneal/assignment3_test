package edu.altu.medapp;

import edu.altu.medapp.model.Appointment;
import edu.altu.medapp.model.Doctor;
import edu.altu.medapp.service.AppointmentService;
import edu.altu.medapp.service.DoctorAvailabilityService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AppointmentService appointmentService = new AppointmentService();
        DoctorAvailabilityService availabilityService = new DoctorAvailabilityService();

        System.out.println("=== Medical Appointment System ===\n");

        System.out.println("1. Available Doctors:");
        List<Doctor> doctors = availabilityService.getAvailableDoctors();
        if (doctors.isEmpty()) {
            System.out.println("   No available doctors");
        } else {
            for (Doctor doctor : doctors) {
                System.out.println("   ID: " + doctor.getId() + " - " + doctor.getName() + " (" + doctor.getSpecialization() + ")");
            }
        }

        System.out.println("\n2. Book Appointment Test:");
        if (!doctors.isEmpty()) {
            try {
                Appointment appointment = appointmentService.bookAppointment(
                        1,
                        doctors.get(0).getId(),
                        LocalDateTime.now().plusDays(2).withHour(10).withMinute(0)
                );
                System.out.println("   Success: Booked appointment ID " + appointment.getId());

                System.out.println("\n3. View Patient's Upcoming Appointments:");
                List<Appointment> upcoming = appointmentService.getPatientUpcomingAppointments(1);
                System.out.println("   Patient 1 has " + upcoming.size() + " upcoming appointments");

                System.out.println("\n4. View Doctor's Schedule:");
                List<Appointment> schedule = appointmentService.getDoctorSchedule(doctors.get(0).getId());
                System.out.println("   Doctor " + doctors.get(0).getName() + " has " + schedule.size() + " appointments");

                System.out.println("\n=== System Test Complete ===");

            } catch (Exception e) {
                System.out.println("   Error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}