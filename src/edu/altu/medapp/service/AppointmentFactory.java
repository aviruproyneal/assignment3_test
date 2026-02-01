package edu.altu.medapp.service;

import edu.altu.medapp.interfaces.IAppointmentType;
import edu.altu.medapp.model.*;

public class AppointmentFactory {
    public enum AppointmentType {
        ONLINE, IN_PERSON, FOLLOW_UP
    }

    public static IAppointmentType createAppointmentType(AppointmentType type) {
        switch (type) {
            case ONLINE:
                return new OnlineAppointment();
            case IN_PERSON:
                return new InPersonAppointment();
            case FOLLOW_UP:
                return new FollowUpAppointment();
            default:
                throw new IllegalArgumentException("Unknown appointment type: " + type);
        }
    }

    public static Appointment createAppointment(AppointmentType type,
                                                int patientId, int doctorId,
                                                java.time.LocalDateTime appointmentTime) {
        IAppointmentType appointmentType = createAppointmentType(type);
        return appointmentType.createAppointment(patientId, doctorId, appointmentTime);
    }
}