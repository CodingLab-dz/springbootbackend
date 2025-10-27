package com.example.pannel2.repository;

import com.example.pannel2.entity.Historic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HistoricRepository extends JpaRepository<Historic, Long> {
    List<Historic> findByUser_Id(Long userId);
    Optional<Historic> findById(Long historicId);
    List<Historic> findByUser_IdIn(List<Long> userIds);
}
