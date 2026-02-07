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
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            String sql = "INSERT INTO doctors (name, specialization, is_available) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, doctor.getName());
            stmt.setString(2, doctor.getSpecialization());
            stmt.setBoolean(3, doctor.isAvailable());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                doctor.setId(rs.getInt(1));
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("Error saving doctor: " + e.getMessage());
        }
    }

    @Override
    public Doctor findById(int id) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT * FROM doctors WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(rs.getInt("id"));
                doctor.setName(rs.getString("name"));
                doctor.setSpecialization(rs.getString("specialization"));
                doctor.setAvailable(rs.getBoolean("is_available"));

                rs.close();
                stmt.close();
                conn.close();

                return doctor;
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("Error finding doctor: " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Doctor> findAll() {
        List<Doctor> doctors = new ArrayList<>();

        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM doctors ORDER BY name";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(rs.getInt("id"));
                doctor.setName(rs.getString("name"));
                doctor.setSpecialization(rs.getString("specialization"));
                doctor.setAvailable(rs.getBoolean("is_available"));
                doctors.add(doctor);
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("Error getting doctors: " + e.getMessage());
        }

        return doctors;
    }

    @Override
    public List<Doctor> findAvailable() {
        List<Doctor> doctors = new ArrayList<>();

        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM doctors WHERE is_available = true ORDER BY name";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(rs.getInt("id"));
                doctor.setName(rs.getString("name"));
                doctor.setSpecialization(rs.getString("specialization"));
                doctor.setAvailable(rs.getBoolean("is_available"));
                doctors.add(doctor);
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("Error finding available doctors: " + e.getMessage());
        }

        return doctors;
    }

    @Override
    public void update(Doctor doctor) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            String sql = "UPDATE doctors SET name=?, specialization=?, is_available=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, doctor.getName());
            stmt.setString(2, doctor.getSpecialization());
            stmt.setBoolean(3, doctor.isAvailable());
            stmt.setInt(4, doctor.getId());

            stmt.executeUpdate();

            stmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("Error updating doctor: " + e.getMessage());
        }
    }
}