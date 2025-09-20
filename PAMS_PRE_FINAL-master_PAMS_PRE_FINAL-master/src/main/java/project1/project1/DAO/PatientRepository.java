package project1.project1.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import project1.project1.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long>{

	Patient findByEmailAndPassword(String email, String password);
	List<Patient> findByEmail(String email);
	//List<Appointment> findByPatient_PatientIdAndStatus(Long patientId, Appointment.Status status);



}
