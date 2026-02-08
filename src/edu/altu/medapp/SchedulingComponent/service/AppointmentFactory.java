package edu.altu.medapp.SchedulingComponent.service;

import edu.altu.medapp.SchedulingComponent.model.Appointment;
import java.time.LocalDateTime;

public class AppointmentFactory {

    public static Appointment createOnline(int patientId, int doctorId, LocalDateTime time) {
        return new Appointment(patientId, doctorId, time, "ONLINE", 50.0);
    }

    public static Appointment createInPerson(int patientId, int doctorId, LocalDateTime time) {
        return new Appointment(patientId, doctorId, time, "IN_PERSON", 100.0);
    }

    public static Appointment createFollowUp(int patientId, int doctorId, LocalDateTime time) {
        return new Appointment(patientId, doctorId, time, "FOLLOW_UP", 75.0);
    }

    public static Appointment create(String type, int patientId, int doctorId, LocalDateTime time) {
        if (type == null) {
            return createInPerson(patientId, doctorId, time);
        }

        String normalizedType = type.trim().toUpperCase();

        if (normalizedType.equals("ONLINE")) {
            return createOnline(patientId, doctorId, time);
        } else if (normalizedType.equals("FOLLOW_UP") || normalizedType.equals("FOLLOWUP")) {
            return createFollowUp(patientId, doctorId, time);
        } else {
            return createInPerson(patientId, doctorId, time);
        }
    }
}