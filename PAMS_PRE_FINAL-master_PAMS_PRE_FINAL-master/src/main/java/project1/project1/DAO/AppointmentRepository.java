package project1.project1.DAO;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import project1.project1.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // All appointments for a patient
    List<Appointment> findByPatient_PatientId(Long patientId);

    // All appointments for a patient filtered by status
    List<Appointment> findByPatient_PatientIdAndStatus(Long patientId, Appointment.Status status);

    // All appointments for a doctor
    List<Appointment> findByDoctor_DoctorId(Long doctorId);

    // All appointments for a doctor on a specific date
    List<Appointment> findByDoctor_DoctorIdAndAppointmentDate(Long doctorId, LocalDate date);

    // All appointments for a doctor on a specific date filtered by status
    List<Appointment> findByDoctor_DoctorIdAndAppointmentDateAndStatus(Long doctorId, LocalDate date, Appointment.Status status);
}
