package edu.altu.medapp.repository.interfaces;

import edu.altu.medapp.model.Doctor;
import java.util.List;
import java.util.Optional;

public interface IDoctorRepository {
    void save(Doctor doctor);
    Optional<Doctor> findById(int id);
    List<Doctor> findAll();
    void update(Doctor doctor);
    void delete(int id);
    List<Doctor> findBySpecialization(String specialization);
    List<Doctor> findAvailableDoctors();
}