package edu.altu.medapp.repository;

import edu.altu.medapp.db.DatabaseConnection;
import edu.altu.medapp.model.Appointment;
import edu.altu.medapp.repository.interfaces.IAppointmentRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppointmentRepository implements IAppointmentRepository {

    @Override
    public void save(Appointment appointment) {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_time, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, appointment.getPatientId());
            pstmt.setInt(2, appointment.getDoctorId());
            pstmt.setTimestamp(3, Timestamp.valueOf(appointment.getAppointmentTime()));
            pstmt.setString(4, appointment.getStatus());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    appointment.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error saving appointment", e);
        }
    }

    @Override
    public Optional<Appointment> findById(int id) {
        String sql = "SELECT * FROM appointments WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToAppointment(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding appointment by ID", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Appointment> findAll() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments ORDER BY appointment_time DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding all appointments", e);
        }

        return appointments;
    }

    @Override
    public void update(Appointment appointment) {
        String sql = "UPDATE appointments SET patient_id = ?, doctor_id = ?, appointment_time = ?, status = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, appointment.getPatientId());
            pstmt.setInt(2, appointment.getDoctorId());
            pstmt.setTimestamp(3, Timestamp.valueOf(appointment.getAppointmentTime()));
            pstmt.setString(4, appointment.getStatus());
            pstmt.setInt(5, appointment.getId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating appointment", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM appointments WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting appointment", e);
        }
    }

    @Override
    public List<Appointment> findByPatientId(int patientId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE patient_id = ? ORDER BY appointment_time DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding appointments by patient ID", e);
        }

        return appointments;
    }

    @Override
    public List<Appointment> findByDoctorId(int doctorId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE doctor_id = ? ORDER BY appointment_time DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding appointments by doctor ID", e);
        }

        return appointments;
    }

    @Override
    public boolean isTimeSlotBooked(int doctorId, LocalDateTime appointmentTime) {
        String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_time = ? AND status != 'cancelled'";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, doctorId);
            pstmt.setTimestamp(2, Timestamp.valueOf(appointmentTime));

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error checking time slot availability", e);
        }

        return false;
    }

    @Override
    public List<Appointment> findUpcomingAppointments(int patientId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE patient_id = ? AND appointment_time >= NOW() AND status = 'scheduled' ORDER BY appointment_time ASC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding upcoming appointments", e);
        }

        return appointments;
    }

    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setId(rs.getInt("id"));
        appointment.setPatientId(rs.getInt("patient_id"));
        appointment.setDoctorId(rs.getInt("doctor_id"));

        Timestamp timestamp = rs.getTimestamp("appointment_time");
        if (timestamp != null) {
            appointment.setAppointmentTime(timestamp.toLocalDateTime());
        }

        appointment.setStatus(rs.getString("status"));
        return appointment;
    }
}