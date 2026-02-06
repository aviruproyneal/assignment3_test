package edu.altu.medapp.service;

import edu.altu.medapp.model.Doctor;
import edu.altu.medapp.repository.DoctorRepository;
import java.util.List;

public class DoctorAvailabilityService {
    private final DoctorRepository doctorRepo;

    public DoctorAvailabilityService() {
        this.doctorRepo = new DoctorRepository();
    }

    public List<Doctor> getAvailable() {
        return doctorRepo.findAvailable();
    }
}