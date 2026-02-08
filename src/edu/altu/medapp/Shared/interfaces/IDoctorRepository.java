package edu.altu.medapp.Shared.interfaces;

import edu.altu.medapp.DoctorManagementComponent.model.Doctor;
import java.util.List;

public interface IDoctorRepository extends IRepository<Doctor> {
    List<Doctor> findAvailable();
}