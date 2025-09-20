package project1.project1.control;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import project1.project1.DAO.AdminRepository;
import project1.project1.model.Admin;
import project1.project1.service.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminRepository adminRepository;

    // Registration
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("admin", new Admin());
        return "admin_register"; // keep consistent with your template
    }

    @PostMapping("/register")
    public String registerAdmin(@ModelAttribute Admin admin, Model model) {
        try {
            adminService.registerAdmin(admin);
            return "SucessfullPage";
        } catch (RuntimeException e) {
            model.addAttribute("admin", admin);
            model.addAttribute("error", e.getMessage());
            return "admin_register";
        }
    }

    // Dashboard (no id in URL, uses Spring Security authentication)
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        String email = authentication.getName();

        Admin loggedInAdmin = adminRepository.findByEmail(email)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        Map<String, Long> stats = adminService.getSystemStats();
        model.addAttribute("admin", loggedInAdmin);
        model.addAttribute("stats", stats);
        return "admin-dashboard";
    }

    // Manage users (authentication-based)
    @GetMapping("/manageUsers")
    public String manageUsers(Model model, Authentication authentication) {
        String email = authentication.getName();

        Admin loggedInAdmin = adminRepository.findByEmail(email)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        model.addAttribute("admin", loggedInAdmin);
        model.addAttribute("users", adminService.manageUsers());
        return "admin-manage-users";
    }
}
