package com.example.pannel2.repository;

import com.example.pannel2.entity.ProductPackage;
import com.example.pannel2.entity.User;
import com.example.pannel2.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User ,Long> {
    List<User> findByParentId(Long parentId);
    Optional<User> findByEmail(String email);
    List<User> findByRole(UserRole role);

    List<User> findByAssignedPackage(ProductPackage assignedPackage);
}
