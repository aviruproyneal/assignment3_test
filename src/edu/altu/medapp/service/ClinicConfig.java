package edu.altu.medapp.service;

import java.time.LocalTime;

public class ClinicConfig {
    private static ClinicConfig instance;

    private final LocalTime openingTime;
    private final LocalTime closingTime;
    private final int maxAppointmentsPerDay;
    private final int appointmentDurationMinutes;

    private ClinicConfig() {
        // Default clinic configuration
        this.openingTime = LocalTime.of(9, 0);     // 9:00 AM
        this.closingTime = LocalTime.of(17, 0);    // 5:00 PM
        this.maxAppointmentsPerDay = 20;
        this.appointmentDurationMinutes = 30;
    }

    public static synchronized ClinicConfig getInstance() {
        if (instance == null) {
            instance = new ClinicConfig();
        }
        return instance;
    }

    // Getters
    public LocalTime getOpeningTime() { return openingTime; }
    public LocalTime getClosingTime() { return closingTime; }
    public int getMaxAppointmentsPerDay() { return maxAppointmentsPerDay; }
    public int getAppointmentDurationMinutes() { return appointmentDurationMinutes; }

    // Business methods
    public boolean isWithinWorkingHours(LocalTime time) {
        return !time.isBefore(openingTime) && !time.isAfter(closingTime);
    }

    public String getWorkingHoursString() {
        return String.format("%s - %s", openingTime, closingTime);
    }
}