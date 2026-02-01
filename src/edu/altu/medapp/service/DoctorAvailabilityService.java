package edu.altu.medapp.service;

import edu.altu.medapp.model.Doctor;
import edu.altu.medapp.repository.DoctorRepository;
import edu.altu.medapp.service.exceptions.DoctorUnavailableException;

import java.util.List;

public class DoctorAvailabilityService {
    private final DoctorRepository doctorRepository;

    public DoctorAvailabilityService() {
        this.doctorRepository = new DoctorRepository();
    }

    public List<Doctor> getAvailableDoctors() {
        return doctorRepository.findAvailableDoctors();
    }

    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization);
    }

    public void checkDoctorAvailability(int doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new DoctorUnavailableException("Doctor not found with ID: " + doctorId));

        if (!doctor.isAvailable()) {
            throw new DoctorUnavailableException("Doctor " + doctor.getName() + " is currently unavailable");
        }
    }

    public void updateDoctorAvailability(int doctorId, boolean isAvailable) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new DoctorUnavailableException("Doctor not found with ID: " + doctorId));

        doctor.setAvailable(isAvailable);
        doctorRepository.update(doctor);
    }
}