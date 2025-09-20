package project1.project1.DAO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import project1.project1.model.DoctorAvailability;

public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Long> {

	Optional<DoctorAvailability> findByDoctor_DoctorIdAndDate(Long doctorId, LocalDate date);
    List<DoctorAvailability> findByDoctor_DoctorId(Long doctorId);
}
