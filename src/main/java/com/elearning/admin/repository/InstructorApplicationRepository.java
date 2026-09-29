package com.elearning.admin.repository;

import com.elearning.admin.entity.InstructorApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstructorApplicationRepository extends JpaRepository<InstructorApplication, Long> {
    List<InstructorApplication> findByStatus(String status);
}
