package project1.project1.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import project1.project1.DAO.AppointmentRepository;
import project1.project1.DAO.DoctorAvailabilityRepository;
import project1.project1.DAO.DoctorRepository;
import project1.project1.model.Appointment;
import project1.project1.model.Doctor;

@Service
public class DoctorService {
	
	@Autowired
	private DoctorAvailabilityRepository availabilityRepo;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Doctor registerDoctor(Doctor doctor) {
    	//check for doctor email existing or new user
        List<Doctor> existing = doctorRepository.findByEmail(doctor.getEmail());
        if (!existing.isEmpty()) {
            throw new RuntimeException("A doctor with this email already exists.");
        }
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword())); // ✅ Encrypt password
        doctor.setRole("ROLE_DOCTOR"); // Assign role
        return doctorRepository.save(doctor);
    }


    public Doctor loginDoctor(String email, String password) {
        List<Doctor> doctors = doctorRepository.findByEmail(email);
        if (doctors.isEmpty()) {
            throw new RuntimeException("Invalid email or password.");
        }

        Doctor doctor = doctors.get(0); // Assuming email is unique
        if (passwordEncoder.matches(password, doctor.getPassword())) {
            return doctor;
        } else {
            throw new RuntimeException("Invalid email or password.");
        }
    }

    
    public List<LocalTime> getAvailableSlots(Long doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
        String[] range = doctor.getAvailability().split("-");
        LocalTime start = LocalTime.parse(range[0]);
        LocalTime end = LocalTime.parse(range[1]);

        List<Appointment> booked = appointmentRepository.findByDoctor_DoctorIdAndAppointmentDate(doctorId, date);
        Set<LocalTime> bookedSlots = booked.stream().map(Appointment::getTimeSlot).collect(Collectors.toSet());

        List<LocalTime> availableSlots = new ArrayList<>();
        while (start.isBefore(end)) {
            if (!bookedSlots.contains(start)) {
                availableSlots.add(start);
            }
            start = start.plusMinutes(30);
        }
        return availableSlots;
    }

    
    public Doctor updateDoctorAvailability(Long doctorId, String availability) {
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if (doctor != null) {
            doctor.setAvailability(availability);
            return doctorRepository.save(doctor);
        }
        return null;
    }

    public List<Appointment> viewDoctorSchedule(Long doctorId) {
        return appointmentRepository.findByDoctor_DoctorId(doctorId);
    }

	public Doctor getDoctorById(Long id) {
		// TODO Auto-generated method stub
		return doctorRepository.findById(id).orElse(null);
	}

	public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
}

