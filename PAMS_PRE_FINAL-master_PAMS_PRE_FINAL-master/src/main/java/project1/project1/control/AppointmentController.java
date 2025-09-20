package project1.project1.control;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import project1.project1.DAO.PatientRepository;
import project1.project1.model.Appointment;
import project1.project1.model.Patient;
import project1.project1.service.AppointmentService;
import project1.project1.service.DoctorAvailabilityService;
import project1.project1.service.DoctorService;
import project1.project1.service.NotificationService;
import project1.project1.service.PatientService;

@Controller
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorAvailabilityService doctorAvailabilityService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PatientRepository patientRepository;

    // Show booking form (no patientId param)
    @GetMapping("/book")
    public String showBookingForm(Model model, Authentication authentication) {
        String email = authentication.getName();
        Patient patient = patientRepository.findByEmail(email)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        model.addAttribute("appointment", new Appointment());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("patient", patient);
        model.addAttribute("availableSlots", new ArrayList<>()); // initially empty
        return "appointment-book";
    }

    // Fetch available slots for a doctor on a given date
    @GetMapping("/slots")
    @ResponseBody
    public List<String> getAvailableSlots(@RequestParam Long doctorId,
                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<LocalTime> slots = doctorAvailabilityService.getAvailableSlots(doctorId, date);
        return slots.stream().map(LocalTime::toString).toList();
    }

    // Book appointment (no patientId param)
    @PostMapping("/book")
    public String bookAppointment(@RequestParam Long doctorId,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appointmentDate,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime timeSlot,
                                  RedirectAttributes redirectAttributes,
                                  Authentication authentication) {

        // Get logged-in patient from authentication
        String email = authentication.getName();
        Patient patient = patientRepository.findByEmail(email)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Book the appointment
        Appointment saved = appointmentService.bookAppointment(
                patient.getPatientId(), doctorId, appointmentDate, timeSlot
        );

        // Send confirmation notification
        String message = "Your appointment with Dr. " + saved.getDoctor().getName() +
                         " on " + saved.getAppointmentDate() +
                         " at " + saved.getTimeSlot() + " is confirmed.";
        notificationService.sendConfirmationNotification(patient.getPatientId(), message);

        // Add flash message for dashboard
        redirectAttributes.addFlashAttribute("message", message);

        // Redirect directly to dashboard so it reloads with updated appointments
        return "redirect:/patient/dashboard";
    }


    @PostMapping("/cancel")
    public String cancelAppointment(@RequestParam Long appointmentId,
                                    RedirectAttributes redirectAttributes,
                                    Authentication authentication) {

        // Get logged-in patient
        String email = authentication.getName();
        Patient patient = patientRepository.findByEmail(email)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Cancel appointment with ownership check in service
        appointmentService.cancelAppointment(appointmentId, patient.getPatientId());

        // Add success message for dashboard
        redirectAttributes.addFlashAttribute("message", "Appointment cancelled successfully.");

        // Redirect to dashboard so updated data is shown
        return "redirect:/patient/dashboard";
    }

}
