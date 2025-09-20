package project1.project1.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import project1.project1.DAO.PatientRepository;
import project1.project1.model.Patient;
import project1.project1.service.AppointmentService;
import project1.project1.service.PatientService;

@Controller
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientRepository patientRepository;

    // Registration
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "patient_register";
    }

    @PostMapping("/register")
    public String registerPatient(@ModelAttribute Patient patient, Model model) {
        try {
            patientService.registerPatient(patient);
            return "SucessfullPage";
        } catch (RuntimeException e) {
            model.addAttribute("patient", patient);
            model.addAttribute("error", e.getMessage());
            return "patient_register";
        }
    }

    // Dashboard (no id in URL, uses Spring Security authentication)
    @GetMapping("/dashboard")
    public String dashboard(Model model,
                            @ModelAttribute("message") String message,
                            Authentication authentication) {

        String email = authentication.getName();

        Patient loggedInPatient = patientRepository.findByEmail(email)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Long id = loggedInPatient.getPatientId();

        model.addAttribute("appointments", appointmentService.viewActiveAppointmentsByPatient(id));
        model.addAttribute("cancelledAppointments", appointmentService.viewCancelledAppointmentsByPatient(id));
        model.addAttribute("patient", loggedInPatient);
        model.addAttribute("message", message);

        return "patient-dashboard";
    }

    // Show update form (no id in URL, uses authentication)
    @GetMapping("/update")
    public String showUpdateForm(Model model, Authentication authentication) {
        String email = authentication.getName();

        Patient loggedInPatient = patientRepository.findByEmail(email)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        model.addAttribute("patient", loggedInPatient);
        return "patient-update";
    }

    // Update profile (ownership enforced via authentication)
    @PostMapping("/update")
    public String updateProfile(@ModelAttribute Patient formPatient,
                                RedirectAttributes redirectAttributes,
                                Authentication authentication) {

        String email = authentication.getName();

        Patient loggedInPatient = patientRepository.findByEmail(email)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Enforce ownership
        formPatient.setPatientId(loggedInPatient.getPatientId());

        patientService.updatePatientProfile(formPatient);

        redirectAttributes.addFlashAttribute("message", "Profile updated successfully!");
        return "redirect:/patient/dashboard";
    }
}
