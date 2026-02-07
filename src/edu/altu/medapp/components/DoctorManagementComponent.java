package edu.altu.medapp.components;

import edu.altu.medapp.model.Doctor;
import edu.altu.medapp.repository.DoctorRepository;
import edu.altu.medapp.service.DoctorAvailabilityService;
import java.util.List;

public class DoctorManagementComponent {
    private final DoctorRepository doctorRepo;
    private final DoctorAvailabilityService availabilityService;

    public DoctorManagementComponent() {
        this.doctorRepo = new DoctorRepository();
        this.availabilityService = new DoctorAvailabilityService();
    }

    public List<Doctor> getAvailableDoctors() {return availabilityService.getAvailable();}
}