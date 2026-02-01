package edu.altu.medapp.interfaces;

import edu.altu.medapp.model.Patient;
import java.util.Optional;

public interface IPatientRepository extends IRepository<Patient> {
    Optional<Patient> findByEmail(String email);
}