package project1.project1.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project1.project1.DAO.AppointmentRepository;
import project1.project1.DAO.DoctorRepository;
import project1.project1.DAO.DoctorAvailabilityRepository;
import project1.project1.model.Appointment;
import project1.project1.model.Doctor;
import project1.project1.model.DoctorAvailability;

@Service
public class DoctorAvailabilityService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorAvailabilityRepository availabilityRepo;

    public void updateAvailability(Long doctorId, LocalDate date, LocalTime start, LocalTime end) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
        DoctorAvailability availability = availabilityRepo
            .findByDoctor_DoctorIdAndDate(doctorId, date)
            .orElse(new DoctorAvailability());

        availability.setDoctor(doctor);
        availability.setDate(date);
        availability.setStartTime(start);
        availability.setEndTime(end);

        availabilityRepo.save(availability);
    }

    
    
    public List<LocalTime> getAvailableSlots(Long doctorId, LocalDate date) {
        // Try to find availability for the given doctor and date
        Optional<DoctorAvailability> optionalAvailability =
                availabilityRepo.findByDoctor_DoctorIdAndDate(doctorId, date);

        // If no availability is set, return an empty list instead of throwing
        if (optionalAvailability.isEmpty()) {
            return Collections.emptyList();
        }

        DoctorAvailability availability = optionalAvailability.get();
        LocalTime start = availability.getStartTime();
        LocalTime end = availability.getEndTime();

        // Define lunch break
        LocalTime lunchStart = LocalTime.of(13, 0);
        LocalTime lunchEnd = LocalTime.of(15, 0);

        // Get already booked slots for that doctor and date
        List<Appointment> bookedAppointments =
                appointmentRepository.findByDoctor_DoctorIdAndAppointmentDate(doctorId, date);

        Set<LocalTime> bookedSlots = bookedAppointments.stream()
                .map(Appointment::getTimeSlot)
                .collect(Collectors.toSet());

        // Build available slots list
        List<LocalTime> slots = new ArrayList<>();
        while (start.isBefore(end)) {
            boolean isLunchTime = !start.isBefore(lunchStart) && start.isBefore(lunchEnd);
            if (!bookedSlots.contains(start) && !isLunchTime) {
                slots.add(start);
            }
            start = start.plusMinutes(30);
        }

        return slots;
    }

}
