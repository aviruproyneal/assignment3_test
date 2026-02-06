package edu.altu.medapp.repository;

import edu.altu.medapp.db.DatabaseConnection;
import edu.altu.medapp.interfaces.IAppointmentRepository;
import edu.altu.medapp.model.Appointment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentRepository implements IAppointmentRepository {

    @Override
    public void save(Appointment appointment) {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_time, status, appointment_type, consultation_fee) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, appointment.getPatientId());
            stmt.setInt(2, appointment.getDoctorId());
            stmt.setTimestamp(3, Timestamp.valueOf(appointment.getTime()));
            stmt.setString(4, appointment.getStatus());
            stmt.setString(5, appointment.getType());
            stmt.setDouble(6, appointment.getFee());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                appointment.setId(rs.getInt(1));
            }

        } catch (Exception e) {
            throw new RuntimeException("Save failed", e);
        }
    }

    @Override
    public Appointment findById(int id) {
        String sql = "SELECT * FROM appointments WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapToAppointment(rs);
            }
            throw new RuntimeException("Appointment not found: " + id);

        } catch (Exception e) {
            throw new RuntimeException("Find failed", e);
        }
    }

    @Override
    public List<Appointment> findAll() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments ORDER BY appointment_time DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapToAppointment(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException("Find all failed", e);
        }

        return list;
    }

    @Override
    public List<Appointment> findByDoctorId(int doctorId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE doctor_id = ? ORDER BY appointment_time DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(mapToAppointment(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException("Find by doctor failed", e);
        }

        return list;
    }

    @Override
    public boolean isTimeSlotTaken(int doctorId, LocalDateTime time) {  // Changed from isTimeSlotBooked
        String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_time = ? AND status != 'cancelled'";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, doctorId);
            stmt.setTimestamp(2, Timestamp.valueOf(time));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            throw new RuntimeException("Check slot failed", e);
        }

        return false;
    }

    @Override
    public void update(Appointment appointment) {
        String sql = "UPDATE appointments SET patient_id = ?, doctor_id = ?, appointment_time = ?, status = ?, appointment_type = ?, consultation_fee = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, appointment.getPatientId());
            stmt.setInt(2, appointment.getDoctorId());
            stmt.setTimestamp(3, Timestamp.valueOf(appointment.getTime()));
            stmt.setString(4, appointment.getStatus());
            stmt.setString(5, appointment.getType());
            stmt.setDouble(6, appointment.getFee());
            stmt.setInt(7, appointment.getId());

            stmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Update failed", e);
        }
    }

    private Appointment mapToAppointment(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setId(rs.getInt("id"));
        appointment.setPatientId(rs.getInt("patient_id"));
        appointment.setDoctorId(rs.getInt("doctor_id"));
        appointment.setTime(rs.getTimestamp("appointment_time").toLocalDateTime());
        appointment.setStatus(rs.getString("status"));
        appointment.setType(rs.getString("appointment_type"));
        appointment.setFee(rs.getDouble("consultation_fee"));
        return appointment;
    }
}