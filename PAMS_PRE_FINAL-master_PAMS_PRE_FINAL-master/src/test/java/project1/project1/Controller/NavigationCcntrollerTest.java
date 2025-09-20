package project1.project1.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import jakarta.servlet.http.HttpSession;
import project1.project1.control.NavigationController;

class NavigationControllerTest {

    private final NavigationController navigationController = new NavigationController();

    @Test
    void logout_invalidatesSession() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        HttpSession session = request.getSession();
        session.setAttribute("loggedInPatient", new Object());

        String view = navigationController.logout(request);

        assertNull(request.getSession(false)); // session invalidated
        assertEquals("redirect:/index", view);
    }
}
