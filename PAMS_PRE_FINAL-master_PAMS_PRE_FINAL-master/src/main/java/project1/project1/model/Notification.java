package project1.project1.model;

import java.time.LocalDateTime;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table (name  = "Notification")

public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    private Long recipientId;
    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private LocalDateTime sentDate = LocalDateTime.now();

    public enum NotificationType {
        CONFIRMATION, REMINDER
    }

    
	public Notification() {
		super();
	}


	public Notification(Long notificationId, Long recipientId, String message, NotificationType notificationType,
			LocalDateTime sentDate) {
		super();
		this.notificationId = notificationId;
		this.recipientId = recipientId;
		this.message = message;
		this.notificationType = notificationType;
		this.sentDate = sentDate;
	}


	public Long getNotificationId() {
		return notificationId;
	}


	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}


	public Long getRecipientId() {
		return recipientId;
	}


	public void setRecipientId(Long recipientId2) {
		this.recipientId = recipientId2;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public NotificationType getNotificationType() {
		return notificationType;
	}


	public void setNotificationType(NotificationType notificationType) {
		this.notificationType = notificationType;
	}


	public LocalDateTime getSentDate() {
		return sentDate;
	}


	public void setSentDate(LocalDateTime sentDate) {
		this.sentDate = sentDate;
	}

    
    // Getters and Setters
}

