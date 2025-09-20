package project1.project1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import project1.project1.DAO.AdminRepository;
import project1.project1.DAO.DoctorRepository;
import project1.project1.DAO.PatientRepository;
import project1.project1.model.Admin;
import project1.project1.model.Doctor;
import project1.project1.model.Patient;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Try Admin
        Admin admin = adminRepository.findByEmail(email).stream().findFirst().orElse(null);
        if (admin != null) {
            return User.withUsername(admin.getEmail())
                       .password(admin.getPassword()) // BCrypt hash from DB
                       .roles(stripRolePrefix(admin.getRole()))
                       .build();
        }

        // Try Doctor
        Doctor doctor = doctorRepository.findByEmail(email).stream().findFirst().orElse(null);
        if (doctor != null) {
            return User.withUsername(doctor.getEmail())
                       .password(doctor.getPassword())
                       .roles(stripRolePrefix(doctor.getRole()))
                       .build();
        }

        // Try Patient
        Patient patient = patientRepository.findByEmail(email).stream().findFirst().orElse(null);
        if (patient != null) {
            return User.withUsername(patient.getEmail())
                       .password(patient.getPassword())
                       .roles(stripRolePrefix(patient.getRole()))
                       .build();
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }

    /**
     * Removes "ROLE_" prefix if present so Spring Security doesn't double-prefix.
     */
    private String stripRolePrefix(String role) {
        if (role != null && role.startsWith("ROLE_")) {
            return role.substring(5); // remove "ROLE_"
        }
        return role;
    }
}
