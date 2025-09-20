package project1.project1.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import project1.project1.DAO.NotificationRepository;
import project1.project1.model.Notification;
import project1.project1.model.Notification.NotificationType;
import project1.project1.model.Patient;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    private JavaMailSender mailSender;

    public Notification sendConfirmationNotification(Long recipientId, String message) {
        Notification notification = new Notification();
        notification.setRecipientId(recipientId);
        notification.setMessage(message);
        notification.setNotificationType(NotificationType.CONFIRMATION);
        notification.setSentDate(LocalDateTime.now());
        Notification saved = notificationRepository.save(notification);

        Patient patient = patientService.getPatientById(recipientId);
        if (patient != null && patient.getEmail() != null && !patient.getEmail().isEmpty()) {
            sendEmail(patient.getEmail(), "Appointment Confirmation", message);
        } else {
            System.err.println("No valid email found for patient ID: " + recipientId);
        }

        return saved;
    }

    public Notification sendAppointmentReminder(Long recipientId, String message) {
        Notification notification = new Notification();
        notification.setRecipientId(recipientId);
        notification.setMessage(message);
        notification.setNotificationType(NotificationType.REMINDER);
        notification.setSentDate(LocalDateTime.now());
        Notification saved = notificationRepository.save(notification);

        Patient patient = patientService.getPatientById(recipientId);
        if (patient != null && patient.getEmail() != null && !patient.getEmail().isEmpty()) {
            sendEmail(patient.getEmail(), "Appointment Reminder", message);
        } else {
            System.err.println("No valid email found for patient ID: " + recipientId);
        }

        return saved;
    }

    public List<Notification> getNotificationsForUser(Long recipientId) {
        return notificationRepository.findByRecipientId(recipientId);
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            System.out.println("Attempting to send email to: " + to);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(to);
            mailMessage.setSubject(subject);
            mailMessage.setText(body);
            mailSender.send(mailMessage);
            System.out.println("Email sent successfully to: " + to);
        } catch (Exception e) {
            System.err.println("Email sending failed to " + to + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}


//@Service
//public class NotificationService {
//
//    @Autowired
//    private NotificationRepository notificationRepository;
//
//    @Autowired
//    private PatientService patientService;
//
//    @Autowired
//    private JavaMailSender mailSender; // Requires email config in application.properties
//    public Notification sendConfirmationNotification(Long recipientId, String message) {
//        Notification notification = new Notification();
//        notification.setRecipientId(recipientId);
//        notification.setMessage(message);
//        notification.setNotificationType(NotificationType.CONFIRMATION);
//        notification.setSentDate(LocalDateTime.now());
//        Notification saved = notificationRepository.save(notification);
//
//        try {
//            Patient patient = patientService.getPatientById(recipientId);
//            if (patient != null && patient.getEmail() != null && !patient.getEmail().isEmpty()) {
//                sendEmail(patient.getEmail(), "Appointment Confirmation", message);
//            }
//        } catch (Exception e) {
//            System.err.println("Email sending failed: " + e.getMessage());
//        }
//
//        return saved;
//    }
//
//
//
//
//    public Notification sendAppointmentReminder(Long recipientId, String message) {
//        Notification notification = new Notification();
//        notification.setRecipientId(recipientId);
//        notification.setMessage(message);
//        notification.setNotificationType(NotificationType.REMINDER);
//        notification.setSentDate(LocalDateTime.now());
//        Notification saved = notificationRepository.save(notification);
//
//        Patient patient = patientService.getPatientById(recipientId);
//        if (patient != null && patient.getEmail() != null && !patient.getEmail().isEmpty()) {
//            sendEmail(patient.getEmail(), "Appointment Reminder", message);
//        }
//
//        return saved;
//    }
//
//    public List<Notification> getNotificationsForUser(Long recipientId) {
//        return notificationRepository.findByRecipientId(recipientId);
//    }
//
//    private void sendEmail(String to, String subject, String body) {
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setTo(to);
//        mailMessage.setSubject(subject);
//        mailMessage.setText(body);
//        mailSender.send(mailMessage);
//    }
//}
//
