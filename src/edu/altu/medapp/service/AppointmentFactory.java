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

    public static Appointment createAppointment(AppointmentType type, int patientId, int doctorId, java.time.LocalDateTime appointmentTime) {
        IAppointmentType appointmentType = createAppointmentType(type);

        String typeString;
        switch (type) {
            case ONLINE: typeString = Appointment.TYPE_ONLINE; break;
            case IN_PERSON: typeString = Appointment.TYPE_IN_PERSON; break;
            case FOLLOW_UP: typeString = Appointment.TYPE_FOLLOW_UP; break;
            default: typeString = Appointment.TYPE_IN_PERSON;
        }

        Appointment appointment = new Appointment(patientId, doctorId, appointmentTime, "scheduled", typeString, appointmentType.getBaseFee());
        return appointment;
    }
}