package com.example.pannel2.repository;

import com.example.pannel2.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository  extends JpaRepository<Notification,Long> {
    Optional<Notification> findById (Long id);
}
