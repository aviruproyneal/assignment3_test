package edu.altu.medapp.SchedulingComponent.exceptions;

public class AppointmentConflictException extends RuntimeException {
    public AppointmentConflictException(String message) {super(message);}
}