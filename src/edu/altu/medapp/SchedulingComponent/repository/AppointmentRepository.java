package edu.altu.medapp.SchedulingComponent.repository;

import edu.altu.medapp.Shared.db.DatabaseConnection;
import edu.altu.medapp.Shared.interfaces.IAppointmentRepository;
import edu.altu.medapp.SchedulingComponent.model.Appointment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentRepository implements IAppointmentRepository {

    @Override
    public void save(Appointment appointment) {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_time, status, appointment_type, consultation_fee) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, appointment.getPatientId());
            stmt.setInt(2, appointment.getDoctorId());
            stmt.setTimestamp(3, Timestamp.valueOf(appointment.getTime()));
            stmt.setString(4, appointment.getStatus());
            stmt.setString(5, appointment.getType());
            stmt.setDouble(6, appointment.getFee());

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Insert failed");
            }

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                appointment.setId(keys.getInt(1));
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            throw new RuntimeException("Error saving appointment: " + e.getMessage());
        }
    }

    @Override
    public Appointment findById(int id) {
        String sql = "SELECT * FROM appointments WHERE id = ?";

        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            Appointment appointment = null;

            if (rs.next()) {
                appointment = new Appointment();
                appointment.setId(rs.getInt("id"));
                appointment.setPatientId(rs.getInt("patient_id"));
                appointment.setDoctorId(rs.getInt("doctor_id"));
                appointment.setTime(rs.getTimestamp("appointment_time").toLocalDateTime());
                appointment.setStatus(rs.getString("status"));
                appointment.setType(rs.getString("appointment_type"));
                appointment.setFee(rs.getDouble("consultation_fee"));
            }

            rs.close();
            stmt.close();
            conn.close();

            if (appointment == null) {
                throw new RuntimeException("Appointment " + id + " not found");
            }
            return appointment;

        } catch (Exception e) {
            throw new RuntimeException("Error finding appointment: " + e.getMessage());
        }
    }

    @Override
    public List<Appointment> findAll() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments ORDER BY appointment_time DESC";

        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setId(rs.getInt("id"));
                appointment.setPatientId(rs.getInt("patient_id"));
                appointment.setDoctorId(rs.getInt("doctor_id"));
                appointment.setTime(rs.getTimestamp("appointment_time").toLocalDateTime());
                appointment.setStatus(rs.getString("status"));
                appointment.setType(rs.getString("appointment_type"));
                appointment.setFee(rs.getDouble("consultation_fee"));
                appointments.add(appointment);
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            throw new RuntimeException("Error getting appointments: " + e.getMessage());
        }

        return appointments;
    }

    @Override
    public List<Appointment> findByDoctorId(int doctorId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE doctor_id = ? ORDER BY appointment_time DESC";

        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, doctorId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setId(rs.getInt("id"));
                appointment.setPatientId(rs.getInt("patient_id"));
                appointment.setDoctorId(rs.getInt("doctor_id"));
                appointment.setTime(rs.getTimestamp("appointment_time").toLocalDateTime());
                appointment.setStatus(rs.getString("status"));
                appointment.setType(rs.getString("appointment_type"));
                appointment.setFee(rs.getDouble("consultation_fee"));
                appointments.add(appointment);
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            throw new RuntimeException("Error finding doctor appointments: " + e.getMessage());
        }

        return appointments;
    }

    @Override
    public boolean isTimeSlotTaken(int doctorId, LocalDateTime time) {
        String sql = "SELECT id FROM appointments WHERE doctor_id = ? AND appointment_time = ? AND status != 'cancelled'";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, doctorId);
            stmt.setTimestamp(2, Timestamp.valueOf(time));

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            throw new RuntimeException("Error checking time slot: " + e.getMessage());
        }
    }

    @Override
    public void update(Appointment appointment) {
        String sql = "UPDATE appointments SET status = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, appointment.getStatus());
            stmt.setInt(2, appointment.getId());

            if (stmt.executeUpdate() == 0) {
                throw new RuntimeException("Appointment not found: " + appointment.getId());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error updating appointment: " + e.getMessage());
        }
    }
}