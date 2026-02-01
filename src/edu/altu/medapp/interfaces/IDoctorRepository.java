package edu.altu.medapp.interfaces;

import edu.altu.medapp.model.Doctor;
import java.util.List;

public interface IDoctorRepository extends IRepository<Doctor> {  // No ID parameter
    // Keep existing specific methods
    List<Doctor> findBySpecialization(String specialization);
    List<Doctor> findAvailableDoctors();
}