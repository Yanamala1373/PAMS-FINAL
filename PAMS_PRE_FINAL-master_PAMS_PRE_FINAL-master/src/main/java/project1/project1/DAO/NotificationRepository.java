package project1.project1.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import project1.project1.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientId(Long recipientId);
}

