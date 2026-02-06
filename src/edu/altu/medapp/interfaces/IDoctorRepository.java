package edu.altu.medapp.interfaces;

import edu.altu.medapp.model.Doctor;
import java.util.List;

public interface IDoctorRepository extends IRepository<Doctor> {
    List<Doctor> findAvailable();
}