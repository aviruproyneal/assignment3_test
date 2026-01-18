package edu.altu.medapp.interfaces;

import edu.altu.medapp.model.Patient;
import java.util.List;
import java.util.Optional;

public interface IPatientRepository {
    void save(Patient patient);
    Optional<Patient> findById(int id);
    List<Patient> findAll();
    void update(Patient patient);
    void delete(int id);
    Optional<Patient> findByEmail(String email);
}