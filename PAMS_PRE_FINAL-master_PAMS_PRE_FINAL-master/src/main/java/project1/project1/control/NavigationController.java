package project1.project1.control;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavigationController {

    // Homepage
    @GetMapping("/")
    public String home() {
        return "index"; // index.html
    }

    // Book Now → new/existing user choice
    @GetMapping("/book-appointment")
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
    public String showLoginForm() {
        return "login"; // login.html
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
