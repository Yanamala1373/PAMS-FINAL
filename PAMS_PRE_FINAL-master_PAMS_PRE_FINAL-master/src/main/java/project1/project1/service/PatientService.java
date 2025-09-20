package project1.project1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project1.project1.DAO.AppointmentRepository;
import project1.project1.DAO.PatientRepository;
import project1.project1.model.Admin;
import project1.project1.model.Appointment;
import project1.project1.model.Patient;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registers a new patient.
     */
   
    @Transactional
    public Patient registerPatient(Patient patient) {
        List<Patient> existing = patientRepository.findByEmail(patient.getEmail());
        if (!existing.isEmpty()) {
            throw new RuntimeException("A patient with this email already exists.");
        }
        patient.setPassword(passwordEncoder.encode(patient.getPassword())); // ✅ Encrypt password
        patient.setRole("ROLE_PATIENT"); // Assign role
        return patientRepository.save(patient);
    }

    
    /**
     * Authenticates a patient by email and password.
     */
    public Patient loginPatient(String email, String password) {
        List<Patient> patients = patientRepository.findByEmail(email);
        if (patients.isEmpty()) {
            throw new RuntimeException("Invalid email or password.");
        }

        Patient patient = patients.get(0);
        if (passwordEncoder.matches(password, patient.getPassword())) {
            return patient;
        } else {
            throw new RuntimeException("Invalid email or password.");
        }
    }


    /**
     * Updates an existing patient's profile.
     * Fetches the existing record to avoid transient instance issues.
     */
    @Transactional
    public Patient updatePatientProfile(Patient updatedPatient) {
        Patient existing = patientRepository.findById(updatedPatient.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + updatedPatient.getPatientId()));

        // Update only editable fields
        existing.setName(updatedPatient.getName());
        existing.setEmail(updatedPatient.getEmail());
        existing.setPhone(updatedPatient.getPhone());
        existing.setAddress(updatedPatient.getAddress());
        existing.setDob(updatedPatient.getDob());

        return patientRepository.save(existing);
    }

    /**
     * Returns all appointments for a given patient.
     */
    public List<Appointment> viewAppointmentHistory(Long patientId) {
        return appointmentRepository.findByPatient_PatientId(patientId);
    }

    /**
     * Fetches a patient by ID.
     */
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElse(null);
    }
}
