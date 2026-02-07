package edu.altu.medapp.exceptions;

public class AppointmentConflictException extends RuntimeException {
    public AppointmentConflictException(String message) {super(message);}
}