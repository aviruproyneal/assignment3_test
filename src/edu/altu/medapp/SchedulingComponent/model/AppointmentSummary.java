package edu.altu.medapp.SchedulingComponent.model;

public class AppointmentSummary {
    private final Appointment appointment;

    private AppointmentSummary(Builder builder) {this.appointment = builder.appointment;}

    @Override
    public String toString() {
        return String.format(
                "Appointment Summary:\n" +
                        "ID: %d\n" +
                        "Patient ID: %d\n" +
                        "Doctor ID: %d\n" +
                        "Time: %s\n" +
                        "Type: %s\n" +
                        "Fee: $%.2f\n" +
                        "Status: %s\n",
                appointment.getId(),
                appointment.getPatientId(),
                appointment.getDoctorId(),
                appointment.getTime(),
                appointment.getType(),
                appointment.getFee(),
                appointment.getStatus()
        );
    }

    public static class Builder {
        private Appointment appointment;

        public Builder appointment(Appointment appointment) {
            this.appointment = appointment;
            return this;
        }

        public AppointmentSummary build() {return new AppointmentSummary(this);}
    }
}