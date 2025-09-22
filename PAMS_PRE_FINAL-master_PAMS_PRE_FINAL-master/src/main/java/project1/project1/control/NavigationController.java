package project1.project1.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class NavigationController {

    // Homepage
    @GetMapping("/")
    public String home() {
        return "index"; // index.html
    }

    // Book Now → new/existing user choice
    @GetMapping("/book-appointment")
    public String bookNow() {
        return "book_appointment"; // book_appointment.html
    }
    
 // Book Appointmnet → new/existing user choice
    @GetMapping("/bookAppointment")
    public String bookAppointment() {
        return "book_appointment"; // book_appointment.html
    }

    // New user → choose role
    @GetMapping("/register-choice")
    public String registerChoice() {
        return "middle_register"; // middle_register.html
    }

    // Show login form (Spring Security will handle POST /login)
    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,
                                Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid email or password or role Mismatch.\n Please try again.");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }
        return "login"; // This should match your login.html
    }


    // Logout (works alongside Spring Security's logout)
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/index";
    }
}
