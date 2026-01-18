package edu.altu.medapp.repository;

import edu.altu.medapp.db.DatabaseConnection;
import edu.altu.medapp.model.Doctor;
import edu.altu.medapp.interfaces.IDoctorRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DoctorRepository implements IDoctorRepository {

    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Doctor mapToDoctor(ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setId(rs.getInt("id"));
        doctor.setName(rs.getString("name"));
        doctor.setSpecialization(rs.getString("specialization"));
        doctor.setAvailable(rs.getBoolean("is_available"));
        return doctor;
    }

    @Override
    public void save(Doctor doctor) {
        String sql = "INSERT INTO doctors (name, specialization, is_available) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, doctor.getName());
            pstmt.setString(2, doctor.getSpecialization());
            pstmt.setBoolean(3, doctor.isAvailable());
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) doctor.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving doctor", e);
        }
    }

    @Override
    public Optional<Doctor> findById(int id) {
        String sql = "SELECT * FROM doctors WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? Optional.of(mapToDoctor(rs)) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding doctor by ID", e);
        }
    }

    @Override
    public List<Doctor> findAll() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors ORDER BY name";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) doctors.add(mapToDoctor(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all doctors", e);
        }
        return doctors;
    }

    @Override
    public List<Doctor> findBySpecialization(String specialization) {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors WHERE specialization = ? AND is_available = true ORDER BY name";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, specialization);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) doctors.add(mapToDoctor(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding doctors by specialization", e);
        }
        return doctors;
    }

    @Override
    public List<Doctor> findAvailableDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors WHERE is_available = true ORDER BY name";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) doctors.add(mapToDoctor(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding available doctors", e);
        }
        return doctors;
    }

    @Override
    public void update(Doctor doctor) {
        String sql = "UPDATE doctors SET name = ?, specialization = ?, is_available = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, doctor.getName());
            pstmt.setString(2, doctor.getSpecialization());
            pstmt.setBoolean(3, doctor.isAvailable());
            pstmt.setInt(4, doctor.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating doctor", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM doctors WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting doctor", e);
        }
    }
}