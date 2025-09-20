package project1.project1.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import project1.project1.DAO.AdminRepository;
import project1.project1.DAO.AppointmentRepository;
import project1.project1.DAO.DoctorRepository;
import project1.project1.DAO.PatientRepository;
import project1.project1.model.Admin;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

//    public Admin addAdmin(Admin admin) {
//        return adminRepository.save(admin);
//    }
    
    @Transactional
    public Admin registerAdmin(Admin admin) {
        List<Admin> existing = adminRepository.findByEmail(admin.getEmail());
        if (!existing.isEmpty()) {
            throw new RuntimeException("An admin with this email already exists.");
        }
        admin.setPassword(passwordEncoder.encode(admin.getPassword())); // ✅ Encrypt password
        admin.setRole("ROLE_ADMIN"); // Assign role
        return adminRepository.save(admin);
    }


    
    public Admin loginAdmin(String email, String password) {
        // Step 1: Find admin by email
        List<Admin> admins = adminRepository.findByEmail(email);
        if (admins.isEmpty()) {
            throw new RuntimeException("Invalid email or password.");
        }

        Admin admin = admins.get(0); // Assuming email is unique

        // Step 2: Compare raw password with hashed password in DB
        if (passwordEncoder.matches(password, admin.getPassword())) {
            return admin; // ✅ Login successful
        } else {
            throw new RuntimeException("Invalid email or password.");
        }
    }



    public List<Object> manageUsers() {
        List<Object> users = new ArrayList<>();
        users.addAll(patientRepository.findAll());
        users.addAll(doctorRepository.findAll());
        return users;
    }

    public Map<String, Long> getSystemStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalPatients", patientRepository.count());
        stats.put("totalDoctors", doctorRepository.count());
        stats.put("totalAppointments", appointmentRepository.count());
        return stats;
    }

	public Admin getAdminById(Long id) {
		// TODO Auto-generated method stub
		return adminRepository.findById(id).orElse(null);
	}
}
