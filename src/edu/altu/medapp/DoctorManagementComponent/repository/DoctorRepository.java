package edu.altu.medapp.DoctorManagementComponent.repository;

import edu.altu.medapp.Shared.db.DatabaseConnection;
import edu.altu.medapp.Shared.interfaces.IDoctorRepository;
import edu.altu.medapp.DoctorManagementComponent.model.Doctor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorRepository implements IDoctorRepository {

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
    public void save(Doctor doctor) {
        System.out.println("Not implemented as new don't save new doctor.");
    }

    @Override
    public List<Doctor> findAll() {
        System.out.println("Not  implemented as new don't return all doctors.");
        return new ArrayList<>();
    }

    @Override
    public void update(Doctor doctor) {
        System.out.println("Not  implemented as we do not update doctor information in database.");
    }
}