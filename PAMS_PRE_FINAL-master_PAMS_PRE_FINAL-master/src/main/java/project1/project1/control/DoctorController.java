package project1.project1.control;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import project1.project1.DAO.DoctorRepository;
import project1.project1.model.Appointment;
import project1.project1.model.Doctor;
import project1.project1.service.AppointmentService;
import project1.project1.service.DoctorAvailabilityService;
import project1.project1.service.DoctorService;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorAvailabilityService doctorAvailabilityService;

    @Autowired
    private DoctorRepository doctorRepository;

    // Registration
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("doctor", new Doctor());
        return "doctor_register"; // keep consistent with your template naming
    }

    @PostMapping("/register")
    public String registerDoctor(@ModelAttribute Doctor doctor, Model model) {
        try {
            doctorService.registerDoctor(doctor);
            return "SucessfullPage";
        } catch (RuntimeException e) {
            model.addAttribute("doctor", doctor);
            model.addAttribute("error", e.getMessage());
            return "doctor_register";
        }
    }

    // Dashboard (no id in URL, uses Spring Security authentication)
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        String email = authentication.getName();

        Doctor loggedInDoctor = doctorRepository.findByEmail(email)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Long doctorId = loggedInDoctor.getDoctorId();
        List<Appointment> appointments = doctorService.viewDoctorSchedule(doctorId);

        model.addAttribute("doctor", loggedInDoctor);
        model.addAttribute("appointments", appointments);
        return "doctor-dashboard";
    }

    // Show availability update form (no id in URL, uses authentication)
    @GetMapping("/updateAvailability")
    public String showAvailabilityForm(Model model, Authentication authentication) {
        String email = authentication.getName();

        Doctor loggedInDoctor = doctorRepository.findByEmail(email)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        model.addAttribute("doctorId", loggedInDoctor.getDoctorId());
        return "doctor-update-availability";
    }

    // Handle availability submission (ownership enforced via authentication)
    @PostMapping("/updateAvailability")
    public String updateAvailability(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime,
                                     RedirectAttributes redirectAttributes,
                                     Authentication authentication) {
        String email = authentication.getName();

        Doctor loggedInDoctor = doctorRepository.findByEmail(email)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Long doctorId = loggedInDoctor.getDoctorId();
        doctorAvailabilityService.updateAvailability(doctorId, date, startTime, endTime);

        redirectAttributes.addFlashAttribute("message", "Availability updated successfully!");
        return "redirect:/doctor/dashboard";
    }
}
