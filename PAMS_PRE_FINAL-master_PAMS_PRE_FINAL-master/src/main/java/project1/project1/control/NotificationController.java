package project1.project1.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import project1.project1.service.NotificationService;

@Controller
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/user")
    public String viewNotifications(@RequestParam Long recipientId, Model model) {
        model.addAttribute("notifications", notificationService.getNotificationsForUser(recipientId));
        return "notification-list";
    }
}

