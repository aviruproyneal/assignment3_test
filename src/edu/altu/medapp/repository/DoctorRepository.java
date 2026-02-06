package edu.altu.medapp.repository;

import edu.altu.medapp.db.DatabaseConnection;
import edu.altu.medapp.interfaces.IDoctorRepository;
import edu.altu.medapp.model.Doctor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorRepository implements IDoctorRepository {

    @Override
    public void save(Doctor doctor) {
        String sql = "INSERT INTO doctors (name, specialization, is_available) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, doctor.getName());
            stmt.setString(2, doctor.getSpecialization());
            stmt.setBoolean(3, doctor.isAvailable());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                doctor.setId(rs.getInt(1));
            }

        } catch (Exception e) {
            throw new RuntimeException("Save doctor failed", e);
        }
    }

    @Override
    public Doctor findById(int id) {
        String sql = "SELECT * FROM doctors WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapToDoctor(rs);
            }
            throw new RuntimeException("Doctor not found: " + id);

        } catch (Exception e) {
            throw new RuntimeException("Find doctor failed", e);
        }
    }

    @Override
    public List<Doctor> findAll() {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT * FROM doctors ORDER BY name";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapToDoctor(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException("Find all doctors failed", e);
        }

        return list;
    }

    @Override
    public List<Doctor> findAvailable() {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT * FROM doctors WHERE is_available = true ORDER BY name";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapToDoctor(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException("Find available failed", e);
        }

        return list;
    }

    @Override
    public void update(Doctor doctor) {
        String sql = "UPDATE doctors SET name = ?, specialization = ?, is_available = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, doctor.getName());
            stmt.setString(2, doctor.getSpecialization());
            stmt.setBoolean(3, doctor.isAvailable());
            stmt.setInt(4, doctor.getId());

            stmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Update doctor failed", e);
        }
    }

    private Doctor mapToDoctor(ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setId(rs.getInt("id"));
        doctor.setName(rs.getString("name"));
        doctor.setSpecialization(rs.getString("specialization"));
        doctor.setAvailable(rs.getBoolean("is_available"));
        return doctor;
    }
}