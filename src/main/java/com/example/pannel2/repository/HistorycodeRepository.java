package com.example.pannel2.repository;

import com.example.pannel2.entity.Demande;
import com.example.pannel2.entity.HistoryCode;
import com.example.pannel2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface HistorycodeRepository  extends JpaRepository<HistoryCode, Long>{
    List<HistoryCode> findByUserId(Long userId);

    List<HistoryCode> findByUser_IdIn(List<Long> userIds);
}
