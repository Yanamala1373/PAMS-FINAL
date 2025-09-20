package project1.project1.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import project1.project1.DAO.AppointmentRepository;
import project1.project1.DAO.DoctorRepository;
import project1.project1.DAO.PatientRepository;
import project1.project1.model.Appointment;
import project1.project1.model.Doctor;
import project1.project1.model.Patient;

@Service
public class AppointmentService {
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;  

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private NotificationService notificationService;

    /**
     * Books a new appointment and sets status to BOOKED.
     */
    @Transactional
    public Appointment bookAppointment(Long patientId, Long doctorId, LocalDate date, LocalTime timeSlot) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(date);
        appointment.setTimeSlot(timeSlot);
        appointment.setStatus(Appointment.Status.BOOKED);

        Appointment saved = appointmentRepository.save(appointment);

        notificationService.sendConfirmationNotification(
                patient.getPatientId(),
                "Appointment booked successfully."
        );

        return saved;
    }

    /**
     * Cancels an appointment by marking its status as CANCELLED (soft delete).
     */
    @Transactional
    public void cancelAppointment(Long appointmentId, Long patientId) {
        Appointment appt = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (!appt.getPatient().getPatientId().equals(patientId)) {
            throw new SecurityException("You cannot cancel another patient's appointment");
        }

        appt.setStatus(Appointment.Status.CANCELLED);
        appointmentRepository.save(appt);
    }

    
//    public void cancelAppointment(Long appointmentId) {
//        Appointment appointment = appointmentRepository.findById(appointmentId)
//                .orElseThrow(() -> new RuntimeException("Appointment not found"));
//
//        appointment.setStatus(Appointment.Status.CANCELLED);
//        appointmentRepository.save(appointment);
//
//        notificationService.sendConfirmationNotification(
//                appointment.getPatient().getPatientId(),
//                "Appointment cancelled successfully."
//        );
//    }

    /**
     * Returns all appointments for a given patient (BOOKED + CANCELLED + COMPLETED).
     */
    public List<Appointment> viewAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatient_PatientId(patientId);
    }

    /**
     * Returns only active (BOOKED) appointments for a given patient.
     */
    public List<Appointment> viewActiveAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatient_PatientIdAndStatus(patientId, Appointment.Status.BOOKED);
    }

    /**
     * Returns only cancelled appointments for a given patient.
     */
    public List<Appointment> viewCancelledAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatient_PatientIdAndStatus(patientId, Appointment.Status.CANCELLED);
    }

    /**
     * Returns all appointments for a given doctor.
     */
    public List<Appointment> viewAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctor_DoctorId(doctorId);
    }
}
