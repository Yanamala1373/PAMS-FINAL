package project1.project1.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import project1.project1.model.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Doctor findByEmailAndPassword(String email, String password);
    List<Doctor> findByEmail(String email);
}

