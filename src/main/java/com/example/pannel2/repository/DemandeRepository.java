package com.example.pannel2.repository;
import com.example.pannel2.entity.Demande;
import com.example.pannel2.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DemandeRepository extends JpaRepository<Demande, Long>{
    List<Demande> findByUser_Id(Long userId);
    Optional<Demande> findById(Long demandeId);
    List<Demande> findByUser_IdIn(List<Long> userIds);
}
