package edu.altu.medapp.repository;

import edu.altu.medapp.db.DatabaseConnection;
import edu.altu.medapp.model.Patient;
import edu.altu.medapp.interfaces.IPatientRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientRepository implements IPatientRepository {

    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Patient mapToPatient(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setId(rs.getInt("id"));
        patient.setName(rs.getString("name"));
        patient.setEmail(rs.getString("email"));
        patient.setPhone(rs.getString("phone"));
        Date dob = rs.getDate("date_of_birth");
        if (dob != null) patient.setDateOfBirth(dob.toLocalDate());
        return patient;
    }

    @Override
    public void save(Patient patient) {
        String sql = "INSERT INTO patients (name, email, phone, date_of_birth) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, patient.getName());
            pstmt.setString(2, patient.getEmail());
            pstmt.setString(3, patient.getPhone());
            pstmt.setDate(4, Date.valueOf(patient.getDateOfBirth()));
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) patient.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving patient", e);
        }
    }

    @Override
    public Optional<Patient> findById(int id) {
        String sql = "SELECT * FROM patients WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? Optional.of(mapToPatient(rs)) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding patient by ID", e);
        }
    }

    @Override
    public List<Patient> findAll() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY name";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) patients.add(mapToPatient(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all patients", e);
        }
        return patients;
    }

    @Override
    public Optional<Patient> findByEmail(String email) {
        String sql = "SELECT * FROM patients WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? Optional.of(mapToPatient(rs)) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding patient by email", e);
        }
    }

    @Override
    public void update(Patient patient) {
        String sql = "UPDATE patients SET name = ?, email = ?, phone = ?, date_of_birth = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, patient.getName());
            pstmt.setString(2, patient.getEmail());
            pstmt.setString(3, patient.getPhone());
            pstmt.setDate(4, Date.valueOf(patient.getDateOfBirth()));
            pstmt.setInt(5, patient.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating patient", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM patients WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting patient", e);
        }
    }
}