package com.example.pannel2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pannel2.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}